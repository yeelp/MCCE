package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BeehiveBlockEntity.BeeData;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.component.type.AttributeModifiersComponent.Builder;
import net.minecraft.component.type.BlocksAttacksComponent.DamageReduction;
import net.minecraft.component.type.BlocksAttacksComponent.ItemDamage;
import net.minecraft.component.type.SuspiciousStewEffectsComponent.StewEffect;
import net.minecraft.component.type.ToolComponent.Rule;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.*;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.SwingAnimationType;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ComponentCompensationEffect extends AbstractInstantChaosEffect {

    @FunctionalInterface
    private interface TriFunction<U, V, W, R> {
        R apply(U u, V v, W w);
    }

    private interface ComponentModifier {
        Optional<List<Text>> setComponent(ItemStack stack, Random rand, World world);

        boolean canBeApplied(ItemStack stack);

        String getComponentString();

        default boolean shouldLorePersist() {
            return true;
        }
    }

    private record BasicComponentModification<C>(ComponentType<? super C> type, String name,
                                                 TriFunction<ItemStack, Random, World, C> applyFunc,
                                                 Predicate<ItemStack> applicable,
                                                 Function<C, List<Text>> loreAppender) implements ComponentModifier {

        BasicComponentModification(ComponentType<? super C> type, String name, TriFunction<ItemStack, Random, World, C> applyFunc, Predicate<ItemStack> applicable) {
            this(type, name, applyFunc, applicable, null);
        }

        BasicComponentModification(ComponentType<? super C> type, String name, C value, Predicate<ItemStack> applicable, Function<C, List<Text>> loreAppender) {
            this(type, name, (itemStack, random, world) -> random.nextFloat() < NO_COMP_CHANCE ? null : value, applicable, loreAppender);
        }

        BasicComponentModification(ComponentType<? super C> type, String name, C value, Predicate<ItemStack> applicable) {
            this(type, name, value, applicable, null);
        }

        @Override
        public Optional<List<Text>> setComponent(ItemStack stack, Random rand, World world) {
            C comp = this.applyFunc.apply(stack, rand, world);
            if(comp != null) {
                stack.set(this.type, comp);
                return Optional.ofNullable(this.loreAppender == null ? null : this.loreAppender().apply(comp));
            }
            else {
                stack.remove(this.type);
                return Optional.ofNullable(this.loreAppender == null ? null : Lists.newArrayList());
            }
        }

        @Override
        public boolean canBeApplied(ItemStack stack) {
            return this.applicable.test(stack);
        }

        @Override
        public String getComponentString() {
            return this.name();
        }
    }

    private enum ToolRules {
        PICKAXE(BlockTags.PICKAXE_MINEABLE, "pickaxe"),
        AXE(BlockTags.AXE_MINEABLE, "axe"),
        HOE(BlockTags.HOE_MINEABLE, "hoe"),
        SHOVEL(BlockTags.SHOVEL_MINEABLE, "shovel");

        private final TagKey<Block> tag;
        private final String toolType;

        ToolRules(TagKey<Block> tag, String toolType) {
            this.tag = tag;
            this.toolType = toolType;
        }

        @SuppressWarnings("deprecation")
        ToolComponent.Rule createRule(Random rand) {
            return new Rule(RegistryEntryList.of(Registries.BLOCK, this.tag), Optional.of(rand.nextFloat(MAX_MINE_SPEED)), Optional.of(rand.nextBoolean()));
        }

        String getToolType() {
            return this.toolType;
        }

        static Optional<ToolRules> getToolRule(TagKey<Block> tag) {
            for(ToolRules rule : ToolRules.values()) {
                if(rule.tag == tag) {
                    return Optional.of(rule);
                }
            }
            return Optional.empty();
        }


    }

    private static Identifier createIdentifier(String name) {
        return MCCE.createIdentifier("%s%s".formatted(NAME, name));
    }


    private static final int EFFECT_STRENGTH = 10;
    private static final List<ComponentCompensationEffect.ComponentModifier> COMPONENT_OPTIONS = Lists.newArrayList();
    private static final List<AttributeModifierOption> ATTRIBUTE_MODIFIER_OPTIONS = Lists.newArrayList(
            new AttributeModifierOption(EntityAttributes.STEP_HEIGHT, createIdentifier("stephieght"), -0.5, 1.6, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.BLOCK_BREAK_SPEED, MCCE.createIdentifier("blockbreakspeed"), -1, 1, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.BLOCK_INTERACTION_RANGE, MCCE.createIdentifier("blockrange"), -4, 64, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.BURNING_TIME, MCCE.createIdentifier("burningtime"), -0.8, 2, Operation.ADD_MULTIPLIED_BASE),
            new AttributeModifierOption(EntityAttributes.GRAVITY, MCCE.createIdentifier("gravity"), -0.5, 0.8, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.SCALE, MCCE.createIdentifier("scale"), -0.5, 1.5, Operation.ADD_MULTIPLIED_BASE),
            new AttributeModifierOption(EntityAttributes.MAX_HEALTH, MCCE.createIdentifier("maxhealth"), -5, 15, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.MOVEMENT_EFFICIENCY, createIdentifier("movementefficiency"), 0, 1, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.SNEAKING_SPEED, createIdentifier("sneakspeed"), -0.3, 0.7, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.ENTITY_INTERACTION_RANGE, createIdentifier("entityrange"), -3, 32, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.ATTACK_DAMAGE, createIdentifier("damage"), -1, 10, Operation.ADD_VALUE),
            new AttributeModifierOption(EntityAttributes.ATTACK_KNOCKBACK, createIdentifier("knockback"), 0, 5, Operation.ADD_VALUE)
    );
    private static final List<ConsumableOption> CONSUMABLE_OPTIONS = Lists.newArrayList(
            new ConsumableOption(new TeleportRandomlyConsumeEffect()),
            new ConsumableOption(new ClearAllEffectsConsumeEffect()),
            new ConsumableOption(new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.SATURATION, 1, EFFECT_STRENGTH)))),
            new ConsumableOption(null)
    );
    private static final int CUSTOM_NAME_MAX_LENGTH_EXCLUSIVE = 21, UNICODE_MIN = 32, UNICODE_MAX = 384, CONTROL_BLOCK_START = 127, CONTROL_BLOCK_END = 159;
    private static final int DEATH_PROTECTION_RESISTANCE_DURATION = 200;
    private static final float NO_COMP_CHANCE = 0.1f;
    private static final float MAX_MINE_SPEED = 6.0f;
    private static final int MAX_DAMAGE_PER_USE = 20;
    private static final int MAX_ENCHANTS_EXCLUSIVE = 8;
    private static final int FOOD_MAX_EXCLUSIVE = 21;
    private static final float SAT_MOD_MAX = 3.0f;
    private static final int RANDOM_LORE_MIN = 2, RANDOM_LORE_MAX_EXCLUSIVE = 31, RANDOM_LORE_LENGTH = 50;
    private static final int MAX_PAGES = 32;
    private static final int STACK_MAX_EXCLUSIVE = 100;
    private static final float MAX_COOLDOWN = 480;
    private static final int SWING_DURATION_MAX = 20;
    private static final float EAT_DURATION_MIN = 0.5f, EAT_DURATION_MAX = 5.5f;
    private static final int KINETIC_STARTUP_TIME_MAX = 15, KINETIC_CONDITION_DURATION_MAX = 500;
    private static final String NAME = "componentcompensation";
    private static final Identifier COOLDOWN_GROUP_A = MCCE.createIdentifier("cooldown_a"), COOLDOWN_GROUP_B = MCCE.createIdentifier("cooldown_b");
    private static final Set<UseAction> USEABLE_ACTIONS = EnumSet.complementOf(EnumSet.of(UseAction.NONE, UseAction.BUNDLE, UseAction.TOOT_HORN));

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !player.getInventory().isEmpty();
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getInventoryIterator(player).forEachRemaining((stack) -> {
            if(stack.isEmpty()) {
                return;
            }
            applyRandomComponentsExcluding(stack, player.getEntityWorld(), this.getRNG());
        });
    }

    private static void applyRandomComponentsExcluding(ItemStack stack, World world, Random rand, String... exclusions) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();
        stack.remove(DataComponentTypes.LORE);
        Map<ComponentModifier, List<Text>> newLore = Maps.newHashMap();
        Map<String, List<Text>> allLore = Maps.newHashMap();
        int times = rand.nextInt(3);
        if(customData != null) {
            nbt.getKeys().forEach((key) -> {
                List<Text> l = Lists.newArrayList();
                nbt.getList(key).ifPresent((list) -> list.forEach((s) -> s.asString().map(Text::of).ifPresent(l::add)));
                allLore.put(key, l);
            });
        }
        while(times > 0) {
            ComponentModifier mod = getRandomComponentExcluding(rand, exclusions);
            if(mod.canBeApplied(stack)) {
                mod.setComponent(stack, rand, world).ifPresent((lst) -> {
                    newLore.put(mod, lst);
                    allLore.put(mod.getComponentString(), lst);
                });
                times--;
            }
        }

        List<Text> totalLore = allLore.values().stream().filter((l) -> l != null && !l.isEmpty()).reduce(Lists.newArrayList(), (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        });
        if(!totalLore.isEmpty() || customData != null) {
            newLore.forEach((mod, loreText) -> {
                if(mod.shouldLorePersist()) {
                    NbtList lst = new NbtList();
                    loreText.forEach((t) -> lst.add(NbtString.of(t.getString())));
                    nbt.put(mod.getComponentString(), lst);
                }
            });
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
            if(!totalLore.isEmpty()) {
                stack.set(DataComponentTypes.LORE, new LoreComponent(totalLore));
            }
        }
    }

    @NotNull
    private static ComponentModifier getRandomComponentExcluding(Random rand, String... exclusions) {
        Set<String> blacklist = Arrays.stream(exclusions).collect(Collectors.toSet());
        ComponentModifier mod;
        for(mod = ChaosLib.getRandomElementFrom(COMPONENT_OPTIONS, rand); blacklist.contains(mod.getComponentString()); mod = ChaosLib.getRandomElementFrom(COMPONENT_OPTIONS, rand));
        return mod;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return "Component Compensation";
    }

    private record AttributeModifierOption(RegistryEntry<EntityAttribute> attribute, Identifier id, double minInclusive, double maxExclusive, Operation op) {

        EntityAttributeModifier createModifier() {
            return new EntityAttributeModifier(this.id, ChaosLib.getStaticRandomInstance().nextDouble(this.minInclusive, this.maxExclusive), this.op);
        }
    }

    private record ConsumableOption(ConsumeEffect effect) {

        ConsumableComponent createComponent() {
            ConsumableComponent.Builder builder = ConsumableComponent.builder().consumeSeconds(ChaosLib.getStaticRandomInstance().nextFloat(EAT_DURATION_MIN, EAT_DURATION_MAX)).useAction(ChaosLib.getRandomElementFrom(UseAction.values()));
            if(this.effect != null) {
                builder = builder.consumeEffect(this.effect());
            }
            return builder.build();
        }
    }

    static {
        COMPONENT_OPTIONS.add(new BasicComponentModification<AttributeModifiersComponent>(DataComponentTypes.ATTRIBUTE_MODIFIERS, "attributes", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            Builder builder = AttributeModifiersComponent.builder();
            AttributeModifierOption option = ChaosLib.getRandomElementFrom(ATTRIBUTE_MODIFIER_OPTIONS, rand);
            builder.add(option.attribute(), option.createModifier(), AttributeModifierSlot.ANY);
            if(rand.nextBoolean()) {
                option = ChaosLib.getRandomElementFrom(ATTRIBUTE_MODIFIER_OPTIONS, rand);
                builder.add(option.attribute(), option.createModifier(), AttributeModifierSlot.ANY);
            }
            return builder.build();
        }, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<BeesComponent>(DataComponentTypes.BEES, "bees", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new BeesComponent(Stream.generate(() -> new BeeData(TypedEntityData.create(EntityType.BEE, new NbtCompound()), 0, 0)).limit(rand.nextInt(10)).collect(Collectors.toList())), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<BlocksAttacksComponent>(DataComponentTypes.BLOCKS_ATTACKS, "blocks attack", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            //noinspection MagicNumber
            DamageReduction reduction = new DamageReduction(rand.nextFloat(360.0f), Optional.of(RegistryEntryList.of(Lists.newArrayList(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getIndexedEntries()))), 0, 1);
            ItemDamage damage = new ItemDamage(0, rand.nextFloat(MAX_DAMAGE_PER_USE), rand.nextFloat(MAX_DAMAGE_PER_USE));
            Iterator<Optional<RegistryEntry<SoundEvent>>> it = getRandomElementsFromRegistry(rand, world, RegistryKeys.SOUND_EVENT, 2);
            return new BlocksAttacksComponent(0, 1, List.of(reduction), damage, Optional.empty(), it.next(), it.next());
        }, ItemStack::isDamageable, (comp) -> List.of(Text.of(String.format("Can block attacks (%.2f° range, costs %.2f X damage + %.2f durability)", comp.damageReductions().getFirst().horizontalBlockingAngle(), comp.itemDamage().factor(), comp.itemDamage().base())))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ConsumableComponent>(DataComponentTypes.CONSUMABLE, "consumable", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : ChaosLib.getRandomElementFrom(CONSUMABLE_OPTIONS, rand).createComponent(), Predicates.alwaysTrue(), (comp) -> {
            List<Text> text = Lists.newArrayList();
            text.add(Text.of("Edible"));
            StringBuilder sb = new StringBuilder("Can be eaten ");
            ConsumeEffect effect = comp.onConsumeEffects().isEmpty() ? null : comp.onConsumeEffects().getFirst();
            switch(effect) {
                case TeleportRandomlyConsumeEffect ignored -> sb.append("to teleport randomly!");
                case ClearAllEffectsConsumeEffect ignored -> sb.append("to clear all potion effects!");
                case ApplyEffectsConsumeEffect ignored -> sb.append("to heal!");
                case null -> sb.append("for no effect...");
                default -> throw new IllegalStateException("Unexpected value: " + effect);
            }
            text.add(Text.of(sb.toString()));
            return text;
        }));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Text>(DataComponentTypes.CUSTOM_NAME, "name", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : Text.of(randomString(rand.nextInt(1, CUSTOM_NAME_MAX_LENGTH_EXCLUSIVE))), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.DAMAGE, "damage", (stack, rand, world) -> rand.nextInt(0, stack.getMaxDamage()), (stack) -> stack.contains(DataComponentTypes.MAX_DAMAGE) && !stack.contains(DataComponentTypes.UNBREAKABLE)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<DeathProtectionComponent>(DataComponentTypes.DEATH_PROTECTION, "death protection", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE? null : new DeathProtectionComponent(List.of(new ClearAllEffectsConsumeEffect(), new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.SATURATION, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.RESISTANCE, DEATH_PROTECTION_RESISTANCE_DURATION, EFFECT_STRENGTH))))), Predicates.alwaysTrue(), (comp) -> List.of(Text.of("Will protect from death"))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<DyedColorComponent>(DataComponentTypes.DYED_COLOR, "dyed", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new DyedColorComponent(ChaosLib.getRandomColour()), (stack) -> stack.isIn(ItemTags.DYEABLE)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ItemEnchantmentsComponent>(DataComponentTypes.ENCHANTMENTS, "enchantments", (stack, rand, world) -> {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
            getListOfElementsFromRegistry(world, RegistryKeys.ENCHANTMENT, rand.nextInt(MAX_ENCHANTS_EXCLUSIVE), Functions.identity()).forEach((enchant) -> builder.add(enchant, rand.nextInt(1, EFFECT_STRENGTH)));
            return builder.build();
        }, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<EquippableComponent>(DataComponentTypes.EQUIPPABLE, "equippable", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                stack.remove(DataComponentTypes.GLIDER);
                return null;
            }
            return EquippableComponent.builder(ChaosLib.getRandomElementFrom(new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}, rand)).build();
        }, Predicates.alwaysTrue(), (comp) -> Collections.singletonList(Text.of("Wearable on: %s".formatted(comp.slot().asString())))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<FoodComponent>(DataComponentTypes.FOOD, "food", (stack, rand, world) -> {
            FoodComponent.Builder builder = new FoodComponent.Builder().nutrition(rand.nextInt(FOOD_MAX_EXCLUSIVE)).saturationModifier(rand.nextFloat(SAT_MOD_MAX));
            if(rand.nextBoolean()) {
                builder.alwaysEdible();
            }
            return builder.build();
        }, (stack) -> stack.get(DataComponentTypes.CONSUMABLE) != null, (comp) -> List.of(Text.of("%d hunger, %.2f saturation restored when eaten.%s".formatted(comp.nutrition(), comp.saturation(), comp.canAlwaysEat() ? " (always edible!)" : "")))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<>(DataComponentTypes.GLIDER, "glider", Unit.INSTANCE, (stack) -> stack.get(DataComponentTypes.EQUIPPABLE) != null, (comp) -> List.of(Text.of("Can glide when equipped!"))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Identifier>(DataComponentTypes.ITEM_MODEL, "model", (stack, rand, world) -> {
            Registry<Item> items = world.getRegistryManager().getOrThrow(RegistryKeys.ITEM);
            return items.getId(items.getRandom(world.getRandom()).map(Reference::value).orElse(stack.getItem()));
        }, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<KineticWeaponComponent>(DataComponentTypes.KINETIC_WEAPON, "kinetic weapon", (stack, rand, world) -> {
            Iterator<Optional<RegistryEntry<SoundEvent>>> it = getRandomElementsFromRegistry(rand, world, RegistryKeys.SOUND_EVENT, 2);
            return new KineticWeaponComponent(0, rand.nextInt(KINETIC_STARTUP_TIME_MAX), KineticWeaponComponent.Condition.ofMinRelativeSpeed(rand.nextInt(KINETIC_CONDITION_DURATION_MAX), 0.0f), KineticWeaponComponent.Condition.ofMinRelativeSpeed(rand.nextInt(KINETIC_CONDITION_DURATION_MAX), 0.0f), KineticWeaponComponent.Condition.ofMinRelativeSpeed(rand.nextInt(KINETIC_CONDITION_DURATION_MAX), 0.0f), rand.nextFloat(), 1.0f, it.next(), it.next());
        }, Predicates.alwaysTrue(), (comp) -> List.of(Text.of(String.format("Kinetic (%d startup time)", comp.delayTicks())))));
        COMPONENT_OPTIONS.add(new ComponentCompensationEffect.ComponentModifier() {
            @Override
            public Optional<List<Text>> setComponent(ItemStack stack, Random rand, World world) {
                List<Text> text = Lists.newArrayList();
                for(int lines = rand.nextInt(RANDOM_LORE_MIN, RANDOM_LORE_MAX_EXCLUSIVE); lines > 0; lines--) {
                    Text t = Text.of(randomString(rand.nextInt(RANDOM_LORE_LENGTH)));
                    Style s = t.getStyle().withObfuscated(rand.nextBoolean()).withBold(rand.nextBoolean()).withUnderline(rand.nextBoolean()).withStrikethrough(rand.nextBoolean()).withItalic(rand.nextBoolean()).withColor(ChaosLib.getRandomColour());
                    text.addAll(t.getWithStyle(s));
                }
                return Optional.of(text);
            }

            @Override
            public boolean canBeApplied(ItemStack stack) {
                return true;
            }

            @Override
            public String getComponentString() {
                return "lore";
            }

            @Override
            public boolean shouldLorePersist() {
                return false;
            }
        });
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.MAX_DAMAGE, "max damage", (stack, rand, world) -> rand.nextInt(Integer.MAX_VALUE), Predicates.alwaysTrue(), (damage) -> List.of(Text.of("Has %d total uses".formatted(damage)))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.MAX_STACK_SIZE, "max stack size", (stack, rand, world) -> rand.nextInt(1, STACK_MAX_EXCLUSIVE), ItemStack::isStackable, (size) -> List.of(Text.of("Stacks up to %s".formatted(size)))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<PiercingWeaponComponent>(DataComponentTypes.PIERCING_WEAPON, "piercing", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            Iterator<Optional<RegistryEntry<SoundEvent>>> it = getRandomElementsFromRegistry(rand, world, RegistryKeys.SOUND_EVENT, 2);
            return new PiercingWeaponComponent(rand.nextBoolean(), rand.nextBoolean(), it.next(), it.next());
        }, Predicates.alwaysTrue(), (comp) -> {
            StringBuilder sb = new StringBuilder("Piercing");
            List<String> props = Lists.newArrayList();
            if(comp.dealsKnockback()) {
                props.add("Knockback");
            }
            if(comp.dismounts()) {
                props.add("Dismounts");
            }
            if(!props.isEmpty()) {
                sb.append(": ");
                //noinspection UnnecessaryToStringCall
                sb.append(props.stream().collect(StringBuilder::new, (b, s) -> b.append(s).append(", "), StringBuilder::append).toString());
            }
            return List.of(Text.of(sb.toString()));
        }));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Rarity>(DataComponentTypes.RARITY, "rarity", (stack, rand, world) -> ChaosLib.getRandomElementFrom(Rarity.values(), rand), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<SuspiciousStewEffectsComponent>(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, "stew effects", (stack, rand, world) -> new SuspiciousStewEffectsComponent(getListOfElementsFromRegistry(world, RegistryKeys.STATUS_EFFECT, rand.nextInt(1, 5), (ref) -> new StewEffect(ref, SuspiciousStewEffectsComponent.DEFAULT_DURATION))), (stack) -> stack.isOf(Items.SUSPICIOUS_STEW)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<SwingAnimationComponent>(DataComponentTypes.SWING_ANIMATION, "swing type", (stack, rand, world) -> new SwingAnimationComponent(ChaosLib.getRandomElementFrom(SwingAnimationType.values()), rand.nextInt(SWING_DURATION_MAX)), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ToolComponent>(DataComponentTypes.TOOL, "tool", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            Set<ToolRules> rules = Sets.newHashSet();
            do {
                rules.add(ChaosLib.getRandomElementFrom(ToolRules.values(), rand));
            } while(rand.nextBoolean());
            return new ToolComponent(rules.stream().map((rule) -> rule.createRule(rand)).collect(Collectors.toList()), rand.nextFloat(MAX_MINE_SPEED), rand.nextInt(MAX_DAMAGE_PER_USE), true);
        }, ItemStack::isDamageable, (comp) -> {
            List<String> info = Lists.newArrayList();
            info.add("Works as: ");
            //noinspection DataFlowIssue
            comp.rules().stream().map((r) -> r.blocks().getTagKey().flatMap(ToolRules::getToolRule).map(ToolRules::getToolType).map((s) -> String.format("  %s (%.2f speed, %s)", s, r.speed().orElse(1.0f), r.correctForDrops().orElse(false))).orElse("")).filter(Predicates.not(String::isEmpty)).forEach(info::add);
            info.add(String.format("  %d durability per use", comp.damagePerBlock()));
            return info.stream().map(Text::of).collect(Collectors.toList());
        }));
        COMPONENT_OPTIONS.add(new BasicComponentModification<TooltipDisplayComponent>(DataComponentTypes.TOOLTIP_DISPLAY, "hide tooltip", (stack, rand, world) -> new TooltipDisplayComponent(true, ReferenceSortedSets.emptySet()), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Identifier>(DataComponentTypes.TOOLTIP_STYLE, "tooltip style", (stack, rand, world) -> MCCE.createIdentifier("intentionally_missing"), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ArmorTrim>(DataComponentTypes.TRIM, "trim", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            return getRandomElementsFromRegistry(rand, world, RegistryKeys.TRIM_MATERIAL, 1).next().flatMap(mat -> getRandomElementsFromRegistry(rand, world, RegistryKeys.TRIM_PATTERN, 1).next().map((pat) -> new ArmorTrim(mat, pat))).orElse(null);
        }, (stack) -> stack.isIn(ItemTags.TRIMMABLE_ARMOR)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<>(DataComponentTypes.UNBREAKABLE, "unbreakable", Unit.INSTANCE, ItemStack::isDamageable));
        //noinspection DataFlowIssue
        COMPONENT_OPTIONS.add(new BasicComponentModification<UseCooldownComponent>(DataComponentTypes.USE_COOLDOWN, "cooldown", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new UseCooldownComponent(rand.nextFloat(MAX_COOLDOWN), Optional.of(rand.nextBoolean() ? COOLDOWN_GROUP_A : COOLDOWN_GROUP_B)), Predicates.alwaysTrue(), (comp) -> List.of(Text.of("In Cooldown Group %s (%.2f seconds)".formatted(comp.cooldownGroup().map(Functions.compose((s) -> Character.toTitleCase(s.charAt(s.length() - 1)), Functions.toStringFunction())).orElse('?'), comp.seconds())))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<UseEffectsComponent>(DataComponentTypes.USE_EFFECTS, "use effects", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new UseEffectsComponent(rand.nextBoolean(), true, rand.nextFloat()), (stack) -> stack.contains(DataComponentTypes.CONSUMABLE) || stack.contains(DataComponentTypes.BLOCKS_ATTACKS) || stack.contains(DataComponentTypes.KINETIC_WEAPON) || USEABLE_ACTIONS.contains(stack.getUseAction()), (comp) -> {
            List<String> list = Lists.newArrayList();
            list.add(String.format("%.2f speed penalty when using", 1 - comp.speedMultiplier()));
            if(comp.canSprint()) {
                list.add("Can sprint while using");
            }
            return list.stream().map(Text::of).collect(Collectors.toList());
        }));
        COMPONENT_OPTIONS.add(new BasicComponentModification<UseRemainderComponent>(DataComponentTypes.USE_REMAINDER, "use remainder", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            return world.getRegistryManager().getOrThrow(RegistryKeys.ITEM).getRandom(world.getRandom()).map(Reference::value).map((i) -> {
                ItemStack convert = new ItemStack(i);
                if(convert.isEmpty() || convert.isOf(Items.AIR)) {
                    return null;
                }
                applyRandomComponentsExcluding(convert, world, rand, rand.nextBoolean() ? "use remainder" : "");
                return new UseRemainderComponent(convert);
            }).orElse(null);
        }, Predicates.alwaysTrue(), (comp) -> List.of(Text.of(String.format("When used: turns into %s", comp.convertInto().getName().getString())))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<WeaponComponent>(DataComponentTypes.WEAPON, "weapon", (stack, rand, world) -> new WeaponComponent(rand.nextInt(MAX_DAMAGE_PER_USE)), ItemStack::isDamageable, (comp) -> List.of(Text.of(String.format("Uses %s durability per attack", comp.itemDamagePerAttack())))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<WrittenBookContentComponent>(DataComponentTypes.WRITTEN_BOOK_CONTENT, "written book", (stack, rand, world) -> {
            int generation = rand.nextInt(3);
            MinecraftServer server = world.getServer();
            String author;
            if(server != null && rand.nextBoolean()) {
                author = ChaosLib.getRandomElementFrom(world.getServer().getPlayerNames(), rand);
            }
            else {
                author = randomString(rand.nextInt(1,10));
            }
            String title = rand.nextBoolean() ? randomString(rand.nextInt(1, CUSTOM_NAME_MAX_LENGTH_EXCLUSIVE)) : "";
            List<String> text = Lists.newArrayList();
            if(stack.isOf(Items.WRITTEN_BOOK)) {
                for(int i = rand.nextInt(1, MAX_PAGES); i > 0; i--) {
                    text.add(randomString(rand.nextInt(100)));
                }
            }
            return new WrittenBookContentComponent(RawFilteredPair.of(title), author, generation, text.stream().map(Functions.compose(RawFilteredPair::of, Text::of)).collect(Collectors.toList()), false);
        }, Predicates.alwaysTrue()));
    }

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        do {
            int c = ChaosLib.getStaticRandomInstance().nextInt(UNICODE_MIN, UNICODE_MAX);
            if(c < CONTROL_BLOCK_START || c > CONTROL_BLOCK_END) {
                sb.append((char) c);
                length--;
            }
        } while(length > 0);
        return sb.toString();
    }

    private static <T> Iterator<Optional<RegistryEntry<T>>> getRandomElementsFromRegistry(Random rand, World world, RegistryKey<? extends Registry<T>> key, int amount) {
        Registry<T> registry = world.getRegistryManager().getOrThrow(key);
        Set<Identifier> ids = registry.getIds();
        Stream.Builder<Identifier> builder = Stream.builder();
        for(; amount > 0; amount--, builder.add(ChaosLib.getRandomElementFrom(ids, rand)));
        return builder.build().map((id) -> registry.getEntry(id).map((r) -> (RegistryEntry<T>) r)).iterator();
    }

    private static <U, T> List<T> getListOfElementsFromRegistry(World world, RegistryKey<? extends Registry<U>> key, int amount, Function<Reference<U>, T> converter) {
        Registry<U>  registry = world.getRegistryManager().getOrThrow(key);
        List<T> lst = Lists.newArrayList();
        AtomicInteger num = new AtomicInteger(amount), fails = new AtomicInteger();
        do {
            registry.getRandom(world.getRandom()).ifPresentOrElse((ref) -> {
                lst.add(converter.apply(ref));
                num.getAndDecrement();
            }, fails::getAndIncrement);
        }while(num.get() > 0 && fails.get() < 5);
        return lst;
    }
}
