package yeelp.mcce.model.chaoseffects;

import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

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

public final class PillagerDisguisesEffect extends SimpleTimedChaosEffect {

	private static final List<EntityType<? extends LivingEntity>> CHOICES = ImmutableList.of(EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.WITCH);
	private static final float DIAMOND_EQUIPMENT_THRESHOLD = 2.4f;
	
	
	protected PillagerDisguisesEffect() {
		super(4000, 8000);
	}


	@Override
	public void applyEffect(PlayerEntity player) {
		Box range = new Box(player.getBlockPos().north(10).east(10).down(10), player.getBlockPos().south(10).west(10).up(10));
		World world = player.getWorld();
		world.getEntitiesByClass(VillagerEntity.class, range, (villager) -> !villager.isBaby()).forEach((villager) -> {
			MobEntity entity = this.getPillagerEntity(player.getBlockPos(), world);
			setUpEntity(entity, villager);
			world.spawnEntity(entity);
		});
		world.getEntitiesByClass(AllayEntity.class, range, Predicates.alwaysTrue()).forEach((allay) -> {
			VexEntity vex = new VexEntity(EntityType.VEX, world);
			setUpEntity(vex, allay);
			ItemStack stack = new ItemStack(world.getLocalDifficulty(player.getBlockPos()).isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_SWORD : Items.IRON_SWORD);
			if(this.getRNG().nextFloat() < 0.3f) {
				EnchantmentHelper.enchant(world.getRandom(), stack, this.getRNG().nextInt(10, 40), false);
			}
			vex.equipStack(EquipmentSlot.MAINHAND, stack);
			world.spawnEntity(vex);
		});
		world.getEntitiesByClass(IronGolemEntity.class, range, (golem) -> !golem.isPlayerCreated()).forEach((golem) -> {
			RavagerEntity ravager = new RavagerEntity(EntityType.RAVAGER, world);
			setUpEntity(ravager, golem);
			world.spawnEntity(ravager);
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
	
	private MobEntity getPillagerEntity(BlockPos ref, World world) {
		EntityType<? extends LivingEntity> type = CHOICES.get(this.getRNG().nextInt(CHOICES.size()));
		LocalDifficulty local = world.getLocalDifficulty(ref);
		boolean enchant = local.isHarderThan(this.getRNG().nextFloat(4));
		if(type == EntityType.PILLAGER) {
			PillagerEntity entity = new PillagerEntity(EntityType.PILLAGER, world);
			ItemStack stack = new ItemStack(Items.CROSSBOW);
			if(enchant) {
				EnchantmentHelper.enchant(world.getRandom(), stack, this.getRNG().nextInt(10, 40), true);
			}
			entity.equipStack(EquipmentSlot.MAINHAND, stack);
			return entity;
		}
		else if (type == EntityType.VINDICATOR) {
			VindicatorEntity entity = new VindicatorEntity(EntityType.VINDICATOR, world);
			ItemStack stack = new ItemStack(local.isHarderThan(DIAMOND_EQUIPMENT_THRESHOLD) ? Items.DIAMOND_AXE : Items.IRON_AXE);
			if(enchant) {
				EnchantmentHelper.enchant(world.getRandom(), stack, this.getRNG().nextInt(10, 40), true);
				if(EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack) == 0 && this.getRNG().nextFloat(7) < local.getLocalDifficulty()) {
					stack.addEnchantment(Enchantments.SHARPNESS, this.getRNG().nextInt(MathHelper.clamp((int) local.getLocalDifficulty() -3, 0, 5)));
				}
			}
			entity.equipStack(EquipmentSlot.MAINHAND, stack);
			return entity;
		}
		else if (type == EntityType.EVOKER) {
			return new EvokerEntity(EntityType.EVOKER, world);
		}
		else if (type == EntityType.ILLUSIONER) {
			IllusionerEntity entity = new IllusionerEntity(EntityType.ILLUSIONER, world);
			ItemStack stack = new ItemStack(Items.BOW);
			if(enchant) {
				EnchantmentHelper.enchant(world.getRandom(), stack, this.getRNG().nextInt(10, 40), true);
			}
			entity.equipStack(EquipmentSlot.MAINHAND, stack);
			return entity;
		}
		else if (type == EntityType.WITCH) {
			return new WitchEntity(EntityType.WITCH, world);
		}
		return null;
	}
	
	private static void setUpEntity(MobEntity pillagerLike, MobEntity villagerLike) {
		pillagerLike.setPosition(villagerLike.getPos());
		pillagerLike.setVelocity(villagerLike.getVelocity());
		pillagerLike.setPersistent();
		villagerLike.discard();
	}

}
