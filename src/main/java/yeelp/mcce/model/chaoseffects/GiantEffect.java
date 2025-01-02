package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import yeelp.mcce.util.MCCESpawnCap;
import yeelp.mcce.util.PlayerUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class GiantEffect extends AbstractInstantChaosEffect {

	private static final float EQUIPMENT_CHANCE = 0.5f, ENCHANT_THRESHOLD_MAX = 0.4f;
	private static final double WEAPON_CHANCE = 0.3, SWORD_CHANCE = 0.2;
	private static final int ENCHANT_LEVEL_MIN = 10, ENCHANT_LEVEL_MAX = 40;
	private enum EquipmentStrength {
		LEATHER(45) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
                return switch (slot) {
                    case HEAD -> Items.LEATHER_HELMET;
                    case CHEST -> Items.LEATHER_CHESTPLATE;
                    case LEGS -> Items.LEATHER_LEGGINGS;
                    case FEET -> Items.LEATHER_BOOTS;
                    default -> throw new IllegalArgumentException("Unexpected value: " + slot);
                };
			}
		},
		GOLD(25) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
                return switch (slot) {
                    case HEAD -> Items.GOLDEN_HELMET;
                    case CHEST -> Items.GOLDEN_CHESTPLATE;
                    case LEGS -> Items.GOLDEN_LEGGINGS;
                    case FEET -> Items.GOLDEN_BOOTS;
                    default -> throw new IllegalArgumentException("Unexpected value: " + slot);
                };
			}
		},
		IRON(15) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
                return switch (slot) {
                    case HEAD -> Items.IRON_HELMET;
                    case CHEST -> Items.IRON_CHESTPLATE;
                    case LEGS -> Items.IRON_LEGGINGS;
                    case FEET -> Items.IRON_BOOTS;
                    default -> throw new IllegalArgumentException("Unexpected value: " + slot);
                };
			}
		},
		DIAMOND(10) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
                return switch (slot) {
                    case HEAD -> Items.DIAMOND_HELMET;
                    case CHEST -> Items.DIAMOND_CHESTPLATE;
                    case LEGS -> Items.DIAMOND_LEGGINGS;
                    case FEET -> Items.DIAMOND_BOOTS;
                    default -> throw new IllegalArgumentException("Unexpected value: " + slot);
                };
			}
		},
		NETHERITE(5) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
                return switch (slot) {
                    case HEAD -> Items.NETHERITE_HELMET;
                    case CHEST -> Items.NETHERITE_CHESTPLATE;
                    case LEGS -> Items.NETHERITE_LEGGINGS;
                    case FEET -> Items.NETHERITE_BOOTS;
                    default -> throw new IllegalArgumentException("Unexpected value: " + slot);
                };
			}
		};
		
		private final int weight;
		
		EquipmentStrength(int weight) {
			this.weight = weight;
		}
		
		abstract Item getItemForSlot(EquipmentSlot slot);
	}
	
	private static final List<EquipmentStrength> STRENGTHS = Lists.newArrayList(EquipmentStrength.values());
	
	@Override
	public void applyEffect(PlayerEntity player) {
		GiantEntity giant = new GiantEntity(EntityType.GIANT, player.getWorld());
		giant.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0.0f, 0.0f);
		LocalDifficulty local = player.getWorld().getLocalDifficulty(player.getBlockPos());
		boolean enchant = local.isHarderThan(this.getRNG().nextFloat(ENCHANT_THRESHOLD_MAX));
		if(this.getRNG().nextFloat() < EQUIPMENT_CHANCE) {
			Collections.shuffle(STRENGTHS, this.getRNG());
			int chance = this.getRNG().nextInt(100);
			for(EquipmentStrength equipmentStrength : STRENGTHS) {
				chance -= equipmentStrength.weight;
				if(chance <= 0) {
					int pieces = this.getRNG().nextInt(1, 5);
					for(EquipmentSlot slot : new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
						pieces--;
						ItemStack stack = new ItemStack(equipmentStrength.getItemForSlot(slot));
						if(enchant) {
							EnchantmentHelper.enchant(player.getWorld().getRandom(), stack, this.getRNG().nextInt(ENCHANT_LEVEL_MIN, ENCHANT_LEVEL_MAX), player.getRegistryManager(), Optional.empty());
						}
						giant.equipStack(slot, stack);
						if(pieces == 0) {
							break;
						}
					}
					break;
				}
			}
			if(this.getRNG().nextDouble() < WEAPON_CHANCE) {
				ItemStack weapon = new ItemStack(this.getRNG().nextDouble() < SWORD_CHANCE ? Items.IRON_SWORD : Items.IRON_SHOVEL);
				if(enchant) {
					EnchantmentHelper.enchant(player.getWorld().getRandom(), weapon, this.getRNG().nextInt(ENCHANT_LEVEL_MIN, ENCHANT_LEVEL_MAX), player.getRegistryManager(), Optional.empty());
				}
				giant.equipStack(EquipmentSlot.MAINHAND, weapon);
			}
			if(this.getRNG().nextBoolean()) {
				giant.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
			}
		}
		MCCESpawnCap.MOB.attemptEntitySpawn(player.getWorld(), giant);
	}

	@Override
	public String getName() {
		return "giant";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return PlayerUtils.doesPlayerHaveValidPosition(player);
	}

}
