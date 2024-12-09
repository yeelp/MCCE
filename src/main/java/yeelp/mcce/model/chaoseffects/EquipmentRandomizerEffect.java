package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Sets;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.potion.Potion;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.AttributeUtils;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.*;
import java.util.stream.Stream;

public final class EquipmentRandomizerEffect extends AbstractIntervalChaosEffect {

    private static final Identifier SPYGLASS_FOLLOW_RANGE = MCCE.createIdentifier("spyglassfollowrange");
    private static final float ENCHANT_LOCAL_DIFF_THRESHOLD = 5.0f;
    private static final int DURATION_MIN = 1800;
    private static final int DURATION_MAX = 2800;
    private static final int INTERVAL_MIN = 40;
    private static final int INTERVAL_MAX = 60;

    private enum BeaconBoost {
        ARMOR("armor", 6.0, Operation.ADD_VALUE) {
            @Override
            RegistryEntry<EntityAttribute> getAttribute() {
                return EntityAttributes.ARMOR;
            }
        },
        SPEED("speed", 0.3, Operation.ADD_MULTIPLIED_TOTAL) {
            @Override
            RegistryEntry<EntityAttribute> getAttribute() {
                return EntityAttributes.MOVEMENT_SPEED;
            }
        },
        KNOCKBACK("attack_knockback", 3.0, Operation.ADD_VALUE) {
            @Override
            RegistryEntry<EntityAttribute> getAttribute() {
                return EntityAttributes.ATTACK_KNOCKBACK;
            }
        },
        KNOCKBACK_RESIST("knockback_resist", 1.0, Operation.ADD_VALUE) {
            @Override
            RegistryEntry<EntityAttribute> getAttribute() {
                return EntityAttributes.KNOCKBACK_RESISTANCE;
            }
        };

        private final double amount;
        private final Operation op;
        private final Identifier id;

        BeaconBoost(String name, double amount, Operation op) {
            this.amount = amount;
            this.op = op;
            this.id = MCCE.createIdentifier(String.format("beacon%s", name));
        }

        EntityAttributeModifier createModifier() {
            return new EntityAttributeModifier(this.id, this.amount, this.op);
        }

        abstract RegistryEntry<EntityAttribute> getAttribute();
    }

    private enum SlotItems {
        //@formatter:off
		HELMET(Items.TURTLE_HELMET, 
				Items.LEATHER_HELMET, 
				Items.GOLDEN_HELMET,
				Items.CHAINMAIL_HELMET, 
				Items.IRON_HELMET, 
				Items.DIAMOND_HELMET, 
				Items.NETHERITE_HELMET, 
				Items.BEACON, 
				Items.DRAGON_HEAD) {
			//@formatter:on
            @SuppressWarnings("FeatureEnvy")
            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                SlotItems.applyArmorModifications(world, stack, rand);
                if (stack.getItem() == Items.BEACON) {
                    BeaconBoost boost = ChaosLib.getRandomElementFrom(BeaconBoost.values(), rand);
                    AttributeUtils.addAttributeModifier(stack, boost.getAttribute(), boost.createModifier(), AttributeModifierSlot.HEAD);
                }
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.HEAD;
            }
        },
        //@formatter:off
		CHESTPLATE(Items.ELYTRA, 
				Items.LEATHER_CHESTPLATE, 
				Items.GOLDEN_CHESTPLATE, 
				Items.CHAINMAIL_CHESTPLATE, 
				Items.IRON_CHESTPLATE, 
				Items.DIAMOND_CHESTPLATE, 
				Items.NETHERITE_CHESTPLATE) {
			//@formatter:on
            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                SlotItems.applyArmorModifications(world, stack, rand);
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.CHEST;
            }
        },
        //@formatter:off
		LEGGINGS(Items.LEATHER_LEGGINGS, 
				Items.GOLDEN_LEGGINGS, 
				Items.CHAINMAIL_LEGGINGS, 
				Items.IRON_LEGGINGS, 
				Items.DIAMOND_LEGGINGS, 
				Items.NETHERITE_LEGGINGS) {
			//@formatter:on
            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                SlotItems.applyArmorModifications(world, stack, rand);
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.LEGS;
            }
        },
        //@formatter:off
		BOOTS(Items.LEATHER_BOOTS, 
				Items.GOLDEN_BOOTS, 
				Items.CHAINMAIL_BOOTS, 
				Items.IRON_BOOTS, 
				Items.DIAMOND_BOOTS, 
				Items.NETHERITE_BOOTS) {
			//@formatter:on
            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                SlotItems.applyArmorModifications(world, stack, rand);
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.FEET;
            }
        },
        //@formatter:off
		OFFHAND(Items.SHIELD,
				Items.TOTEM_OF_UNDYING, 
				Items.TIPPED_ARROW, 
				Items.DEBUG_STICK) {
			//@formatter:on

            private final Set<RegistryEntry<Potion>> effects = Sets.newHashSet();

            {
                //@formatter:off
                Stream.of("strong_harming", "long_weakness", "strong_poison", "strong_slowness", "turtle_master")
                        .map(Identifier::ofVanilla)
                        .map((id) -> Registries.POTION.getEntry(id).get())
                        .forEach(effects::add);
                //@formatter:on
            }

            @Override
            final ItemStack createRandomStack(World world, LocalDifficulty local, Random rand) {
                if (rand.nextDouble() < 0.9) {
                    return super.createRandomStack(world, local, rand);
                }
                return MAINHAND.createRandomStack(world, local, rand);
            }

            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                if (stack.getItem() == this.items[2]) {
                    stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(ChaosLib.getRandomElementFrom(this.effects, rand)));
                }
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.OFFHAND;
            }
        },
        //@formatter:off
		MAINHAND(Items.WOODEN_AXE,
				Items.WOODEN_HOE,
				Items.WOODEN_PICKAXE,
				Items.WOODEN_SHOVEL,
				Items.WOODEN_SWORD,
				Items.STONE_AXE,
				Items.STONE_HOE,
				Items.STONE_PICKAXE,
				Items.STONE_SHOVEL,
				Items.STONE_SWORD,
				Items.GOLDEN_AXE,
				Items.GOLDEN_HOE,
				Items.GOLDEN_PICKAXE,
				Items.GOLDEN_SHOVEL,
				Items.GOLDEN_SWORD,
				Items.IRON_AXE,
				Items.IRON_HOE,
				Items.IRON_PICKAXE,
				Items.IRON_SHOVEL,
				Items.IRON_SWORD,
				Items.DIAMOND_AXE,
				Items.DIAMOND_HOE,
				Items.DIAMOND_PICKAXE,
				Items.DIAMOND_SHOVEL,
				Items.DIAMOND_SWORD,
				Items.NETHERITE_AXE,
				Items.NETHERITE_HOE,
				Items.NETHERITE_PICKAXE,
				Items.NETHERITE_SHOVEL,
				Items.NETHERITE_SWORD,
				Items.BOW,
				Items.TRIDENT,
				Items.TOTEM_OF_UNDYING,
				Items.SPYGLASS) {
			//@formatter:on
            @Override
            protected void applyModifications(World world, ItemStack stack, Random rand) {
                if (Objects.equals(stack.getItem(), Items.SPYGLASS)) {
                    AttributeUtils.addAttributeModifier(stack, EntityAttributes.FOLLOW_RANGE, new EntityAttributeModifier(SPYGLASS_FOLLOW_RANGE, 3.5, Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.MAINHAND);
                }
            }

            @Override
            EquipmentSlot getSlot() {
                return EquipmentSlot.MAINHAND;
            }
        };

        protected final Item[] items;

        SlotItems(Item... items) {
            this.items = items;
        }

        @SuppressWarnings("MagicNumber")
        ItemStack createRandomStack(World world, LocalDifficulty local, Random rand) {
            ItemStack stack = new ItemStack(ChaosLib.getRandomElementFrom(this.items, rand));
            this.applyModifications(world, stack, rand);
            if (local.isHarderThan(rand.nextFloat(ENCHANT_LOCAL_DIFF_THRESHOLD))) {
                EnchantmentHelper.enchant(world.getRandom(), stack, (int) (rand.nextInt((int) (25 + local.getLocalDifficulty() / 2.0f)) + local.getLocalDifficulty()), world.getRegistryManager(), Optional.empty());
            }
            return stack;
        }

        abstract EquipmentSlot getSlot();

        protected abstract void applyModifications(World world, ItemStack stack, Random rand);

        protected static void applyArmorModifications(World world, ItemStack stack, Random rand) {
            if (stack.isIn(ItemTags.DYEABLE)) {
                dyeArmor(stack, rand);
            }
            if (stack.isIn(ItemTags.TRIMMABLE_ARMOR)) {
                applyTrims(world, stack, rand);
            }

        }

        @SuppressWarnings("MagicNumber")
        private static void dyeArmor(ItemStack stack, Random rand) {
            stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(Math.abs(rand.nextInt()) & 0x00FFFFFF, true));
        }

        private static void applyTrims(World world, ItemStack stack, Random rand) {
            Registry<ArmorTrimPattern> patternRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.TRIM_PATTERN);
            if (rand.nextInt(patternRegistry.size() + 1) == 0) {
                return;
            }
            //@formatter:off
            patternRegistry.getRandom(world.getRandom()).ifPresent(
                    (pattern) -> world.getRegistryManager().getOrThrow(RegistryKeys.TRIM_MATERIAL).getRandom(world.getRandom()).ifPresent(
                            (material) -> stack.set(DataComponentTypes.TRIM, new ArmorTrim(material, pattern))));
            //@formatter:on
        }
    }

    private static final List<Class<? extends MobEntity>> TARGETS = Lists.newArrayList();
    private static final int RADIUS = 20;
    private static final double PERCENT_CHANCE = 0.1;

    static {
        TARGETS.add(ZombieEntity.class);
        TARGETS.add(ZombieVillagerEntity.class);
        TARGETS.add(DrownedEntity.class);
        TARGETS.add(HuskEntity.class);
        TARGETS.add(SkeletonEntity.class);
        TARGETS.add(StrayEntity.class);
        TARGETS.add(WitherSkeletonEntity.class);
        TARGETS.add(PiglinEntity.class);
        TARGETS.add(PiglinBruteEntity.class);
        TARGETS.add(ZombifiedPiglinEntity.class);
        TARGETS.add(GiantEntity.class);
        TARGETS.add(BoggedEntity.class);
    }

    public EquipmentRandomizerEffect() {
        super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        World world = player.getWorld();
        LocalDifficulty local = world.getLocalDifficulty(player.getBlockPos());
        TARGETS.forEach((clazz) -> world.getEntitiesByClass(clazz, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> true).forEach((mob) -> {
            for (SlotItems slot : SlotItems.values()) {
                if (this.getRNG().nextDouble() >= PERCENT_CHANCE) {
                    mob.equipStack(slot.getSlot(), slot.createRandomStack(world, local, this.getRNG()));
                } else {
                    mob.equipStack(slot.getSlot(), ItemStack.EMPTY);
                }
            }
        }));
    }

    @Override
    public String getName() {
        return "equipmentrandomizer";
    }

    @Override
    public void registerCallbacks() {
        //empty
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return PlayerUtils.isPlayerInDimension(player, World.END);
    }

}
