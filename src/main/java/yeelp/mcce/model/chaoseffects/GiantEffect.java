package yeelp.mcce.model.chaoseffects;

import java.util.Collections;
import java.util.List;

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

public class GiantEffect extends AbstractInstantChaosEffect {
	
	private enum EquipmentStrength {
		LEATHER(45) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
				switch(slot) {
					case HEAD:
						return Items.LEATHER_HELMET;
					case CHEST:
						return Items.LEATHER_CHESTPLATE;
					case LEGS:
						return Items.LEATHER_LEGGINGS;
					case FEET:
						return Items.LEATHER_BOOTS;
					default:
						throw new IllegalArgumentException("Unexpected value: " + slot);
				}
			}
		},
		GOLD(25) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
				switch(slot) {
					case HEAD:
						return Items.GOLDEN_HELMET;
					case CHEST:
						return Items.GOLDEN_CHESTPLATE;
					case LEGS:
						return Items.GOLDEN_LEGGINGS;
					case FEET:
						return Items.GOLDEN_BOOTS;
					default:
						throw new IllegalArgumentException("Unexpected value: " + slot);
				}
			}
		},
		IRON(15) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
				switch(slot) {
					case HEAD:
						return Items.IRON_HELMET;
					case CHEST:
						return Items.IRON_CHESTPLATE;
					case LEGS:
						return Items.IRON_LEGGINGS;
					case FEET:
						return Items.IRON_BOOTS;
					default:
						throw new IllegalArgumentException("Unexpected value: " + slot);
				}
			}
		},
		DIAMOND(10) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
				switch(slot) {
					case HEAD:
						return Items.DIAMOND_HELMET;
					case CHEST:
						return Items.DIAMOND_CHESTPLATE;
					case LEGS:
						return Items.DIAMOND_LEGGINGS;
					case FEET:
						return Items.DIAMOND_BOOTS;
					default:
						throw new IllegalArgumentException("Unexpected value: " + slot);
				}
			}
		},
		NETHERITE(5) {
			@Override
			Item getItemForSlot(EquipmentSlot slot) {
				switch(slot) {
					case HEAD:
						return Items.NETHERITE_HELMET;
					case CHEST:
						return Items.NETHERITE_CHESTPLATE;
					case LEGS:
						return Items.NETHERITE_LEGGINGS;
					case FEET:
						return Items.NETHERITE_BOOTS;
					default:
						throw new IllegalArgumentException("Unexpected value: " + slot);
				}
			}
		};
		
		private int weight;
		
		EquipmentStrength(int weight) {
			this.weight = weight;
		}
		
		abstract Item getItemForSlot(EquipmentSlot slot);
	}
	
	private static final List<EquipmentStrength> STRENGTHS = Lists.newArrayList(EquipmentStrength.values());
	
	@Override
	public void applyEffect(PlayerEntity player) {
		GiantEntity giant = new GiantEntity(EntityType.GIANT, player.getWorld());
		giant.setPos(player.getX(), player.getY(), player.getZ());
		LocalDifficulty local = player.getWorld().getLocalDifficulty(player.getBlockPos());
		boolean enchant = local.isHarderThan(this.getRNG().nextFloat(4.0f));
		if(this.getRNG().nextFloat() < 0.5f) {
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
							EnchantmentHelper.enchant(player.getWorld().getRandom(), stack, this.getRNG().nextInt(10, 40), true);
						}
						giant.equipStack(slot, stack);
						if(pieces == 0) {
							break;
						}
					}
					break;
				}
			}
		}
		player.getWorld().spawnEntity(giant);
	}

	@Override
	public String getName() {
		return "giant";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
