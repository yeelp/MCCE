package yeelp.mcce.model.chaoseffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class EquipmentRandomizerEffect extends IntervalChaosEffect {

	private static final UUID SPYGLASS_FOLLOW_RANGE = UUID.fromString("5d3c1939-beb6-41ce-9a1e-7bf36d49f51d");

	private enum BeaconBoost {
		ARMOR("armor", "8be13a82-e38f-44fb-a438-1fe864d41562", 6.0, Operation.ADDITION) {
			@Override
			EntityAttribute getAttribute() {
				return EntityAttributes.GENERIC_ARMOR;
			}
		},
		SPEED("speed", "4b9f3e68-c1c9-43ae-b3a6-55f1817c2f16", 1.3, Operation.MULTIPLY_TOTAL) {
			@Override
			EntityAttribute getAttribute() {
				return EntityAttributes.GENERIC_MOVEMENT_SPEED;
			}
		},
		KNOCKBACK("attack_knockback", "de935749-ae67-4fbf-838a-fa6feda7d7b2", 3.0, Operation.ADDITION) {
			@Override
			EntityAttribute getAttribute() {
				return EntityAttributes.GENERIC_ATTACK_KNOCKBACK;
			}
		},
		KNOCKBACK_RESIST("knockback_resist", "780173c2-2888-4b2b-a23f-182676791175", 1.0, Operation.ADDITION) {
			@Override
			EntityAttribute getAttribute() {
				return EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE;
			}
		};

		private final UUID uuid;
		private final double amount;
		private final Operation op;
		private final String name;

		BeaconBoost(String name, String uuid, double amount, Operation op) {
			this.uuid = UUID.fromString(uuid);
			this.amount = amount;
			this.op = op;
			this.name = String.format("Beacon Head Boost %s", name);
		}

		EntityAttributeModifier createModifier() {
			return new EntityAttributeModifier(this.uuid, this.name, this.amount, this.op);
		}

		abstract EntityAttribute getAttribute();
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
			@Override
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				SlotItems.applyArmorModifications(stack, rand);
				if(stack.getItem() == this.items[7]) {
					BeaconBoost boost = BeaconBoost.values()[rand.nextInt(BeaconBoost.values().length)];
					stack.addAttributeModifier(boost.getAttribute(), boost.createModifier(), EquipmentSlot.HEAD);
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
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				SlotItems.applyArmorModifications(stack, rand);
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
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				SlotItems.applyArmorModifications(stack, rand);
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
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				SlotItems.applyArmorModifications(stack, rand);
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

			private final Potion[] effects = {
					Registries.POTION.get(new Identifier("minecraft", "strong_harming")),
					Registries.POTION.get(new Identifier("minecraft", "long_weakness")),
					Registries.POTION.get(new Identifier("minecraft", "strong_poison")),
					Registries.POTION.get(new Identifier("minecraft", "strong_slowness")),
					Registries.POTION.get(new Identifier("minecraft", "turtle_master"))};

			@Override
			final ItemStack createRandomStack(World world, LocalDifficulty local, Random rand) {
				if(rand.nextDouble() < 0.9) {
					return super.createRandomStack(world, local, rand);
				}
				return MAINHAND.createRandomStack(world, local, rand);
			}

			@Override
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				if(stack.getItem() == this.items[2]) {
					PotionUtil.setPotion(stack, this.effects[rand.nextInt(this.effects.length)]);
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
			protected void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand) {
				if(stack.getItem() == Items.SPYGLASS) {
					stack.addAttributeModifier(EntityAttributes.GENERIC_FOLLOW_RANGE, new EntityAttributeModifier(SPYGLASS_FOLLOW_RANGE, "Spyglass Follow Boost", 3.5, Operation.MULTIPLY_BASE), EquipmentSlot.MAINHAND);
				}
			}

			@Override
			EquipmentSlot getSlot() {
				return EquipmentSlot.MAINHAND;
			}
		};

		private static final String MATERIAL_KEY = "material", PATTERN_KEY = "pattern";
		private static final String[] TRIM_MATERIALS = {
				"minecraft:amethyst",
				"minecraft:copper",
				"minecraft:diamond",
				"minecraft:emerald",
				"minecraft:gold",
				"minecraft:iron",
				"minecraft:lapis",
				"minecraft:netherite",
				"minecraft:quartz",
				"minecraft:redstone"};

		private static final String[] TRIM_PATTERNS = {
				"minecraft:coast",
				"minecraft:dune",
				"minecraft:eye",
				"minecraft:rib",
				"minecraft:sentry",
				"minecraft:snout",
				"minecraft:spire",
				"minecraft:tide",
				"minecraft:vex",
				"minecraft:ward",
				"minecraft:wild"};
		Item[] items;

		SlotItems(Item... items) {
			this.items = items;
		}

		ItemStack createRandomStack(World world, LocalDifficulty local, Random rand) {
			ItemStack stack = new ItemStack(this.items[rand.nextInt(this.items.length)]);
			this.applyModifications(world, local, stack, rand);
			if(local.isHarderThan(rand.nextFloat(5.0f))) {
				EnchantmentHelper.enchant(world.getRandom(), stack, (int) (rand.nextInt((int) (25 + local.getLocalDifficulty() / 2.0f)) + local.getLocalDifficulty()), true);
			}
			return stack;
		}

		abstract EquipmentSlot getSlot();

		protected abstract void applyModifications(World world, LocalDifficulty local, ItemStack stack, Random rand);

		protected static void applyArmorModifications(ItemStack stack, Random rand) {
			if(stack.getItem() instanceof DyeableArmorItem) {
				dyeArmor(stack, rand);
			}
			if(stack.isIn(ItemTags.TRIMMABLE_ARMOR)) {
				applyTrims(stack, rand);
			}

		}

		private static void dyeArmor(ItemStack stack, Random rand) {
			NbtCompound nbt = stack.getOrCreateNbt();
			NbtCompound display = nbt.getCompound(ItemStack.DISPLAY_KEY);
			display.putInt(ItemStack.COLOR_KEY, Math.abs(rand.nextInt()) & 0x00FFFFFF);
			nbt.put(ItemStack.DISPLAY_KEY, display);
		}

		private static void applyTrims(ItemStack stack, Random rand) {
			NbtCompound nbt = stack.getOrCreateNbt();
			NbtCompound trim = new NbtCompound();
			trim.putString(MATERIAL_KEY, TRIM_MATERIALS[rand.nextInt(TRIM_MATERIALS.length)]);
			trim.putString(PATTERN_KEY, TRIM_PATTERNS[rand.nextInt(TRIM_PATTERNS.length)]);
			nbt.put(ArmorTrim.NBT_KEY, trim);
		}
	}

	private static final List<Class<? extends MobEntity>> TARGETS = new ArrayList<Class<? extends MobEntity>>();

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
	}

	public EquipmentRandomizerEffect() {
		super(1800, 2800, 40, 60);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Box range = new Box(player.getBlockPos().north(10).east(10).down(10), player.getBlockPos().south(10).west(10).up(10));
		World world = player.getWorld();
		LocalDifficulty local = world.getLocalDifficulty(player.getBlockPos());
		TARGETS.forEach((clazz) -> {
			world.getEntitiesByClass(clazz, range, (entity) -> true).forEach((mob) -> {
				for(SlotItems slot : SlotItems.values()) {
					mob.equipStack(slot.getSlot(), slot.createRandomStack(world, local, this.getRNG()));
				}
			});
		});

	}

	@Override
	public String getName() {
		return "equipmentrandomizer";
	}

	@Override
	public void registerCallbacks() {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
