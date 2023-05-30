package yeelp.mcce.model.chaoseffects;

import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public final class PillagerDisguisesEffect extends SimpleTimedChaosEffect {

	private static final List<EntityType<? extends LivingEntity>> CHOICES = ImmutableList.of(EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.WITCH);

	protected PillagerDisguisesEffect() {
		super(4000, 8000);
	}


	@Override
	public void applyEffect(PlayerEntity player) {
		Box range = new Box(player.getBlockPos().north(10).east(10).down(10), player.getBlockPos().south(10).west(10).up(10));
		player.world.getEntitiesByClass(VillagerEntity.class, range, (villager) -> !villager.isBaby()).forEach((villager) -> {
			LivingEntity entity = this.getPillagerEntity(player.world);
			entity.setPosition(villager.getPos());
			entity.setVelocity(villager.getVelocity());
			villager.refreshPositionAfterTeleport(villager.getPos().x, player.world.getDimension().minY() - 100, villager.getPos().z);
			player.world.spawnEntity(entity);
		});
		player.world.getEntitiesByClass(AllayEntity.class, range, Predicates.alwaysTrue()).forEach((allay) -> {
			VexEntity vex = new VexEntity(EntityType.VEX, player.world);
			vex.setPosition(allay.getPos());
			vex.setVelocity(allay.getVelocity());
			vex.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			allay.refreshPositionAfterTeleport(allay.getPos().x, player.world.getDimension().minY() - 100, allay.getPos().z);
			player.world.spawnEntity(vex);
		});
		player.world.getEntitiesByClass(IronGolemEntity.class, range, (golem) -> !golem.isPlayerCreated()).forEach((golem) -> {
			RavagerEntity ravager = new RavagerEntity(EntityType.RAVAGER, player.world);
			ravager.setPosition(golem.getPos());
			ravager.setVelocity(golem.getVelocity());
			golem.refreshPositionAfterTeleport(golem.getPos().x, player.world.getDimension().minY() - 100, golem.getPos().z);
			player.world.spawnEntity(ravager);
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
		return player.world.getRegistryKey() == World.OVERWORLD;
	}
	
	private LivingEntity getPillagerEntity(World world) {
		EntityType<? extends LivingEntity> type = CHOICES.get(this.getRNG().nextInt(CHOICES.size()));
		if(type == EntityType.PILLAGER) {
			PillagerEntity entity = new PillagerEntity(EntityType.PILLAGER, world);
			entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
			return entity;
		}
		else if (type == EntityType.VINDICATOR) {
			VindicatorEntity entity = new VindicatorEntity(EntityType.VINDICATOR, world);
			entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
			return entity;
		}
		else if (type == EntityType.EVOKER) {
			return new EvokerEntity(EntityType.EVOKER, world);
		}
		else if (type == EntityType.ILLUSIONER) {
			IllusionerEntity entity = new IllusionerEntity(EntityType.ILLUSIONER, world);
			entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
			return entity;
		}
		else if (type == EntityType.WITCH) {
			return new WitchEntity(EntityType.WITCH, world);
		}
		return null;
	}

}
