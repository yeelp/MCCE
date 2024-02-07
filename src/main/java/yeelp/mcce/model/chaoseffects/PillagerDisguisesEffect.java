package yeelp.mcce.model.chaoseffects;

import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import yeelp.mcce.util.ChaosLib;

public final class PillagerDisguisesEffect extends SimpleTimedChaosEffect {

	private static final Set<Class<? extends LivingEntity>> TARGETS = ImmutableSet.of(VillagerEntity.class, AllayEntity.class, IronGolemEntity.class);
	private static final float DIAMOND_EQUIPMENT_THRESHOLD = 2.4f;
	private static final float ENCHANT_RAND_THRESHOLD = 4.0f;
	private static final int ENCHANT_LEVEL_LOWER_BOUND = 10;
	private static final int ENCHANT_LEVEL_UPPER_BOUND = 40;
	private static final boolean ENCHANT_ALLOW_TREASURE = true;

	private static enum PillagerChoices {
		PILLAGER {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				PillagerEntity entity = new PillagerEntity(EntityType.PILLAGER, world);
				ItemStack stack = new ItemStack(Items.CROSSBOW);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), ENCHANT_ALLOW_TREASURE);
				}
				entity.equipStack(EquipmentSlot.MAINHAND, stack);
				return entity;
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		VINDICATOR {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				VindicatorEntity entity = new VindicatorEntity(EntityType.VINDICATOR, world);
				ItemStack stack = new ItemStack(local.isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_AXE : Items.IRON_AXE);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), ENCHANT_ALLOW_TREASURE);
					if(EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) == 0 && rng.nextFloat(7) < local.getLocalDifficulty()) {
						stack.addEnchantment(Enchantments.SHARPNESS, rng.nextInt(MathHelper.clamp((int) local.getLocalDifficulty() - 3, 1, 5)));
					}
				}
				entity.equipStack(EquipmentSlot.MAINHAND, stack);
				return entity;
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		EVOKER {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				return new EvokerEntity(EntityType.EVOKER, world);
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		ILLUSIONER {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				IllusionerEntity entity = new IllusionerEntity(EntityType.ILLUSIONER, world);
				ItemStack stack = new ItemStack(Items.BOW);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), ENCHANT_ALLOW_TREASURE);
				}
				entity.equipStack(EquipmentSlot.MAINHAND, stack);
				return entity;
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		WITCH {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				return new WitchEntity(EntityType.WITCH, world);
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		VEX {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				VexEntity vex = new VexEntity(EntityType.VEX, world);
				ItemStack stack = new ItemStack(local.isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_SWORD : Items.IRON_SWORD);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), false);
				}
				vex.equipStack(EquipmentSlot.MAINHAND, stack);
				return vex;
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return entity instanceof AllayEntity;
			}
		},
		RAVAGER {
			@Override
			MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng) {
				return new RavagerEntity(EntityType.RAVAGER, world);
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return entity instanceof IronGolemEntity && !((IronGolemEntity) entity).isPlayerCreated();
			}
		};

		private static final PillagerChoices[] VILLAGER_CHOICES = {
				PILLAGER,
				VINDICATOR,
				EVOKER,
				ILLUSIONER,
				WITCH};
		private static final PillagerChoices[] NON_VILLAGER_CHOICES = {
				VEX,
				RAVAGER};
		private static final Predicate<LivingEntity> VILLAGER_TEST = (v) -> v instanceof VillagerEntity && !((VillagerEntity) v).isBaby();

		abstract MobEntity create(BlockPos ref, World world, LocalDifficulty local, Random rng);

		protected abstract boolean valid(LivingEntity entity);

		private static boolean shouldEnchant(LocalDifficulty local, Random rng) {
			return local.isHarderThan(rng.nextFloat(ENCHANT_RAND_THRESHOLD));
		}

		static @Nullable PillagerChoices getPillagerChoice(LivingEntity entity, Random rng) {
			PillagerChoices choice;
			if((choice = ChaosLib.getRandomElementFrom(VILLAGER_CHOICES, rng)).valid(entity)) {
				return choice;
			}
			for(PillagerChoices pillagerChoices : NON_VILLAGER_CHOICES) {
				if(pillagerChoices.valid(entity)) {
					return pillagerChoices;
				}
			}
			return null;
		}
	}

	public PillagerDisguisesEffect() {
		super(4000, 8000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Box range = ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 10);
		World world = player.getWorld();
		world.getEntitiesByClass(MobEntity.class, range, (e) -> TARGETS.contains(e.getClass())).forEach((e) -> {
			PillagerChoices pc = PillagerChoices.getPillagerChoice(e, this.getRNG());
			if(pc == null) {
				return;
			}
			BlockPos pos = player.getBlockPos();
			MobEntity mob;
			setUpEntity(mob = pc.create(pos, world, world.getLocalDifficulty(pos), this.getRNG()), e);
			world.spawnEntity(mob);
		});
	}

	@Override
	public String getName() {
		return "pillagerdisguises";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.OVERWORLD;
	}

	private static void setUpEntity(MobEntity pillagerLike, MobEntity villagerLike) {
		pillagerLike.setPosition(villagerLike.getPos());
		pillagerLike.setVelocity(villagerLike.getVelocity());
		pillagerLike.setPersistent();
		villagerLike.discard();
	}

}
