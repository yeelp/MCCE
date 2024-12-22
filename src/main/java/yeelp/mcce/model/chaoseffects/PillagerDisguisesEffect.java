package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableSet;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.EnchantmentUtils;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public final class PillagerDisguisesEffect extends SimpleTimedChaosEffect {

	private static final Set<Class<? extends LivingEntity>> TARGETS = ImmutableSet.of(VillagerEntity.class, AllayEntity.class, IronGolemEntity.class);
	private static final float DIAMOND_EQUIPMENT_THRESHOLD = 2.4f;
	private static final float ENCHANT_RAND_THRESHOLD = 4.0f;
	private static final int ENCHANT_LEVEL_LOWER_BOUND = 10;
	private static final int ENCHANT_LEVEL_UPPER_BOUND = 40;
	private static final int DURATION_MIN = 4000, DURATION_MAX = 8000;

	private enum PillagerChoices {
		PILLAGER {
			@Override
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				PillagerEntity entity = new PillagerEntity(EntityType.PILLAGER, world);
				ItemStack stack = new ItemStack(Items.CROSSBOW);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), entity.getRegistryManager(), Optional.empty());
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
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				VindicatorEntity entity = new VindicatorEntity(EntityType.VINDICATOR, world);
				ItemStack stack = new ItemStack(local.isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_AXE : Items.IRON_AXE);
				DynamicRegistryManager manager = entity.getRegistryManager();
				RegistryEntry<Enchantment> sharpness = EnchantmentUtils.getEntry(Enchantments.SHARPNESS, manager);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), manager, Optional.empty());
					if(EnchantmentHelper.getLevel(sharpness, stack) == 0 && rng.nextFloat(7) < local.getLocalDifficulty()) {
						stack.addEnchantment(sharpness, rng.nextInt(MathHelper.clamp((int) local.getLocalDifficulty() - 3, 1, 5)));
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
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				return new EvokerEntity(EntityType.EVOKER, world);
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		ILLUSIONER {
			@Override
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				IllusionerEntity entity = new IllusionerEntity(EntityType.ILLUSIONER, world);
				ItemStack stack = new ItemStack(Items.BOW);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), entity.getRegistryManager(), Optional.empty());
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
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				return new WitchEntity(EntityType.WITCH, world);
			}

			@Override
			protected boolean valid(LivingEntity entity) {
				return VILLAGER_TEST.test(entity);
			}
		},
		VEX {
			@Override
			MobEntity create(World world, LocalDifficulty local, Random rng) {
				VexEntity vex = new VexEntity(EntityType.VEX, world);
				ItemStack stack = new ItemStack(local.isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_SWORD : Items.IRON_SWORD);
				if(shouldEnchant(local, rng)) {
					EnchantmentHelper.enchant(world.getRandom(), stack, rng.nextInt(ENCHANT_LEVEL_LOWER_BOUND, ENCHANT_LEVEL_UPPER_BOUND), vex.getRegistryManager(), Optional.empty());
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
			MobEntity create(World world, LocalDifficulty local, Random rng) {
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
		private static final Predicate<LivingEntity> VILLAGER_TEST = (v) -> v instanceof VillagerEntity && !v.isBaby();

		abstract MobEntity create(World world, LocalDifficulty local, Random rng);

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
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		world.getEntitiesByClass(MobEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 10), (e) -> TARGETS.contains(e.getClass())).forEach((e) -> {
			PillagerChoices pc = PillagerChoices.getPillagerChoice(e, this.getRNG());
			if(pc == null) {
				return;
			}
			BlockPos pos = player.getBlockPos();
			MobEntity mob;
			setUpEntity(mob = pc.create(world, world.getLocalDifficulty(pos), this.getRNG()), e);
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
		MCCEAPI.mutator.copyDespawnTimerTo(villagerLike, pillagerLike);
		villagerLike.discard();
	}

}
