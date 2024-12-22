package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.component.type.AttributeModifiersComponent.Builder;
import net.minecraft.component.type.SuspiciousStewEffectsComponent.StewEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ComponentCompensationEffect extends AbstractInstantChaosEffect {

    @FunctionalInterface
    private interface TriFunction<U, V, W, R> {
        R apply(U u, V v, W w);
    }

    private interface ComponentModifier {
        void setComponent(ItemStack stack, Random rand, World world);

        boolean canBeApplied(ItemStack stack);

        String getComponentString();
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
        public void setComponent(ItemStack stack, Random rand, World world) {
            C comp = this.applyFunc.apply(stack, rand, world);
            if(comp != null) {
                stack.set(this.type, comp);
            }
            else {
                stack.remove(this.type);
            }
            if(this.loreAppender != null) {
                NbtComponent nbt = stack.get(DataComponentTypes.CUSTOM_DATA);
                if(nbt == null) {
                    nbt = NbtComponent.of(new NbtCompound());
                    stack.set(DataComponentTypes.CUSTOM_DATA, nbt);
                }
                NbtCompound tag = nbt.copyNbt();
                if(comp != null) {
                    LoreComponent lore = stack.get(DataComponentTypes.LORE);
                    List<Text> text = Lists.newArrayList();
                    if(lore != null) {
                        text.addAll(lore.lines());
                    }
                    List<Text> specificLore = this.loreAppender.apply(comp);
                    text.addAll(specificLore);
                    stack.set(DataComponentTypes.LORE, new LoreComponent(text));
                    NbtList lst = new NbtList();
                    specificLore.forEach((t) -> lst.add(NbtString.of(t.getString())));
                    tag.put(this.getComponentString(), lst);
                }
                else {
                    tag.remove(this.getComponentString());
                }
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
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



    private static final int EFFECT_STRENGTH = 10;
    private static final List<ComponentCompensationEffect.ComponentModifier> COMPONENT_OPTIONS = Lists.newArrayList();
    private static final List<AttributeModifierOption> ATTRIBUTE_MODIFIER_OPTIONS = Lists.newArrayList(
            new AttributeModifierOption(EntityAttributes.STEP_HEIGHT, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationstephieght"), 1.5, Operation.ADD_VALUE)),
            new AttributeModifierOption(EntityAttributes.BLOCK_BREAK_SPEED, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationblockbreakspeed"), 1, Operation.ADD_VALUE)),
            new AttributeModifierOption(EntityAttributes.BLOCK_INTERACTION_RANGE, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationblockrange"), 64, Operation.ADD_VALUE)),
            new AttributeModifierOption(EntityAttributes.BURNING_TIME, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationburningtime"), -0.5, Operation.ADD_MULTIPLIED_BASE)),
            new AttributeModifierOption(EntityAttributes.GRAVITY, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationgravity"), -0.2, Operation.ADD_VALUE)),
            new AttributeModifierOption(EntityAttributes.SCALE, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationscale"), -0.5, Operation.ADD_MULTIPLIED_BASE)),
            new AttributeModifierOption(EntityAttributes.MAX_HEALTH, new EntityAttributeModifier(MCCE.createIdentifier("componentcompensationmaxhealth"), -5, Operation.ADD_VALUE))
    );
    private static final List<ConsumableOption> CONSUMABLE_OPTIONS = Lists.newArrayList(
            new ConsumableOption(2.4f, UseAction.BRUSH, new TeleportRandomlyConsumeEffect()),
            new ConsumableOption(1.6f, UseAction.DRINK, new ClearAllEffectsConsumeEffect()),
            new ConsumableOption(5.0f, UseAction.SPEAR, new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.SATURATION, 1, EFFECT_STRENGTH)))),
            new ConsumableOption(0.5f, UseAction.BLOCK, null)
    );
    private static final int CUSTOM_NAME_MAX_LENGTH_EXCLUSIVE = 21, UNICODE_MIN = 32, UNICODE_MAX = 384, CONTROL_BLOCK_START = 127, CONTROL_BLOCK_END = 159;
    private static final int DEATH_PROTECTION_RESISTANCE_DURATION = 200;
    private static final float NO_COMP_CHANCE = 0.1f;
    private static final int MAX_ENCHANTS_EXCLUSIVE = 8;
    private static final int FOOD_MAX_EXCLUSIVE = 21;
    private static final float SAT_MOD_MAX = 4.0f;
    private static final int RANDOM_LORE_MIN = 2, RANDOM_LORE_MAX_EXCLUSIVE = 31, RANDOM_LORE_LENGTH = 50;
    private static final int COLOR_MASK = 0x00FFFFFF;
    private static final int STACK_MAX_EXCLUSIVE = 100;
    private static final float MAX_COOLDOWN = 480;
    private static final Identifier COOLDOWN_GROUP_A = MCCE.createIdentifier("cooldown_a"), COOLDOWN_GROUP_B = MCCE.createIdentifier("cooldown_b");

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
            NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
            NbtCompound nbt = customData != null ? customData.copyNbt() : null;
            stack.remove(DataComponentTypes.LORE);
            Set<String> compsAdded = Sets.newHashSet();
            int times = this.getRNG().nextInt(3);
            while(times > 0) {
                ComponentCompensationEffect.ComponentModifier mod = ChaosLib.getRandomElementFrom(COMPONENT_OPTIONS, this.getRNG());
                if(mod.canBeApplied(stack)) {
                    mod.setComponent(stack, this.getRNG(), player.getWorld());
                    compsAdded.add(mod.getComponentString());
                    times--;
                }
            }
            if(nbt != null) {
                NbtComponent temp = stack.get(DataComponentTypes.CUSTOM_DATA);
                final NbtComponent newData = temp == null ? NbtComponent.DEFAULT : temp;
                List<Text> newText = Lists.newArrayList();
                nbt.getKeys().forEach((key) -> {
                    if(newData.contains(key) && !compsAdded.contains(key)) {
                        //data persisted so it wasn't removed, but it wasn't added this time, so copy lore data over from last time
                        nbt.getList(key, NbtString.STRING_TYPE).forEach((s) -> newText.add(Text.of(s.asString())));
                    }
                });
                LoreComponent lore = stack.get(DataComponentTypes.LORE);
                if(lore != null) {
                    newText.addAll(lore.lines());
                }
                stack.set(DataComponentTypes.LORE, new LoreComponent(newText));
            }
        });
    }

    @Override
    public String getName() {
        return "componentcompensation";
    }

    private record AttributeModifierOption(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier mod) {

    }

    private record ConsumableOption(float seconds, UseAction animation, ConsumeEffect effect) {

        ConsumableComponent createComponent() {
            ConsumableComponent.Builder builder = ConsumableComponent.builder().consumeSeconds(this.seconds()).useAction(this.animation());
            if(this.effect != null) {
                builder = builder.consumeEffect(this.effect());
            }
            return builder.build();
        }
    }

    static {
        COMPONENT_OPTIONS.add(new ComponentCompensationEffect.BasicComponentModification<AttributeModifiersComponent>(DataComponentTypes.ATTRIBUTE_MODIFIERS, "attributes", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            Builder builder = AttributeModifiersComponent.builder();
            AttributeModifierOption option = ChaosLib.getRandomElementFrom(ATTRIBUTE_MODIFIER_OPTIONS, rand);
            builder.add(option.attribute(), option.mod(), AttributeModifierSlot.ANY);
            if(rand.nextBoolean()) {
                option = ChaosLib.getRandomElementFrom(ATTRIBUTE_MODIFIER_OPTIONS, rand);
                builder.add(option.attribute(), option.mod(), AttributeModifierSlot.ANY);
            }
            return builder.build();
        }, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new ComponentCompensationEffect.BasicComponentModification<ConsumableComponent>(DataComponentTypes.CONSUMABLE, "consumable", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : ChaosLib.getRandomElementFrom(CONSUMABLE_OPTIONS, rand).createComponent(), Predicates.alwaysTrue(), (comp) -> {
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
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.DAMAGE, "damage", (stack, rand, world) -> rand.nextInt(0, stack.getMaxDamage()), ItemStack::isDamageable));
        COMPONENT_OPTIONS.add(new BasicComponentModification<DeathProtectionComponent>(DataComponentTypes.DEATH_PROTECTION, "death protection", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE? null : new DeathProtectionComponent(List.of(new TeleportRandomlyConsumeEffect(), new ClearAllEffectsConsumeEffect(), new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.SATURATION, 1, EFFECT_STRENGTH), new StatusEffectInstance(StatusEffects.RESISTANCE, DEATH_PROTECTION_RESISTANCE_DURATION, EFFECT_STRENGTH))))), Predicates.alwaysTrue(), (comp) -> List.of(Text.of("Will protect from death"))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<DyedColorComponent>(DataComponentTypes.DYED_COLOR, "dyed", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new DyedColorComponent(Math.abs(rand.nextInt()) & COLOR_MASK, true), (stack) -> stack.isIn(ItemTags.DYEABLE)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ItemEnchantmentsComponent>(DataComponentTypes.ENCHANTMENTS, "enchantments", (stack, rand, world) -> {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
            AtomicInteger enchants = new AtomicInteger(rand.nextInt(MAX_ENCHANTS_EXCLUSIVE)), fails = new AtomicInteger();
            Registry<Enchantment> enchantmentRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            do {
                enchantmentRegistry.getRandom(world.getRandom()).ifPresentOrElse((e) -> {
                    builder.add(e, rand.nextInt(1, EFFECT_STRENGTH));
                    enchants.getAndDecrement();
                }, fails::getAndIncrement);
            } while(enchants.get() > 0 && fails.get() < 5);
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
        COMPONENT_OPTIONS.add(new BasicComponentModification<>(DataComponentTypes.HIDE_TOOLTIP, "hide tooltip", Unit.INSTANCE, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Identifier>(DataComponentTypes.ITEM_MODEL, "model", (stack, rand, world) -> {
            Registry<Item> items = world.getRegistryManager().getOrThrow(RegistryKeys.ITEM);
            return items.getId(items.getRandom(world.getRandom()).map(Reference::value).orElse(stack.getItem()));
        }, Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new ComponentCompensationEffect.ComponentModifier() {
            @Override
            public void setComponent(ItemStack stack, Random rand, World world) {
                LoreComponent comp = stack.get(DataComponentTypes.LORE);
                List<Text> text = comp == null ? Lists.newArrayList() : Lists.newArrayList(comp.lines());
                for(int lines = rand.nextInt(RANDOM_LORE_MIN, RANDOM_LORE_MAX_EXCLUSIVE); lines > 0; lines--) {
                    Text t = Text.of(randomString(rand.nextInt(RANDOM_LORE_LENGTH)));
                    Style s = t.getStyle().withObfuscated(rand.nextBoolean()).withBold(rand.nextBoolean()).withUnderline(rand.nextBoolean()).withStrikethrough(rand.nextBoolean()).withItalic(rand.nextBoolean()).withColor(Math.abs(rand.nextInt()) & COLOR_MASK);
                    text.addAll(t.getWithStyle(s));
                }
                stack.set(DataComponentTypes.LORE, new LoreComponent(text));
            }

            @Override
            public boolean canBeApplied(ItemStack stack) {
                return true;
            }

            @Override
            public String getComponentString() {
                return "lore";
            }
        });
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.MAX_DAMAGE, "max damage", (stack, rand, world) -> rand.nextInt(Integer.MAX_VALUE), ItemStack::isDamageable, (damage) -> List.of(Text.of("Has %d total uses".formatted(damage)))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Integer>(DataComponentTypes.MAX_STACK_SIZE, "max stack size", (stack, rand, world) -> rand.nextInt(1, STACK_MAX_EXCLUSIVE), ItemStack::isStackable, (size) -> List.of(Text.of("Stacks up to %s".formatted(size)))));
        COMPONENT_OPTIONS.add(new BasicComponentModification<Rarity>(DataComponentTypes.RARITY, "rarity", (stack, rand, world) -> ChaosLib.getRandomElementFrom(Rarity.values(), rand), Predicates.alwaysTrue()));
        COMPONENT_OPTIONS.add(new BasicComponentModification<SuspiciousStewEffectsComponent>(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, "stew effects", (stack, rand, world) -> {
            List<StewEffect> effects = Lists.newArrayList();
            AtomicInteger numEffects = new AtomicInteger(rand.nextInt(1, 5)), fails = new AtomicInteger();
            Registry<StatusEffect> registry = world.getRegistryManager().getOrThrow(RegistryKeys.STATUS_EFFECT);
            do {
                registry.getRandom(world.getRandom()).ifPresentOrElse((ref) -> {
                    effects.add(new StewEffect(ref, SuspiciousStewEffectsComponent.DEFAULT_DURATION));
                    numEffects.getAndDecrement();
                }, fails::getAndIncrement);
            } while(numEffects.get() > 0 && fails.get() < 5);
            return new SuspiciousStewEffectsComponent(effects);
        }, (stack) -> stack.isOf(Items.SUSPICIOUS_STEW)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<ArmorTrim>(DataComponentTypes.TRIM, "trim", (stack, rand, world) -> {
            if(rand.nextFloat() < NO_COMP_CHANCE) {
                return null;
            }
            DynamicRegistryManager manager = world.getRegistryManager();
            return manager.getOrThrow(RegistryKeys.TRIM_MATERIAL).getRandom(world.getRandom()).flatMap((mat) -> manager.getOrThrow(RegistryKeys.TRIM_PATTERN).getRandom(world.getRandom()).map((pat) -> new ArmorTrim(mat, pat))).orElse(null);
        }, (stack) -> stack.isIn(ItemTags.TRIMMABLE_ARMOR)));
        COMPONENT_OPTIONS.add(new BasicComponentModification<>(DataComponentTypes.UNBREAKABLE, "unbreakable", new UnbreakableComponent(true), ItemStack::isDamageable));
        COMPONENT_OPTIONS.add(new BasicComponentModification<UseCooldownComponent>(DataComponentTypes.USE_COOLDOWN, "cooldown", (stack, rand, world) -> rand.nextFloat() < NO_COMP_CHANCE ? null : new UseCooldownComponent(rand.nextFloat(MAX_COOLDOWN), Optional.of(rand.nextBoolean() ? COOLDOWN_GROUP_A : COOLDOWN_GROUP_B)), Predicates.alwaysTrue(), (comp) -> List.of(Text.of("In Cooldown Group %s (%.2f seconds)".formatted(comp.cooldownGroup().map(Functions.compose((s) -> Character.toTitleCase(s.charAt(s.length() - 1)), Functions.toStringFunction())).orElse('?'), comp.seconds())))));
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
}
