package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.AttributeUtils;
import yeelp.mcce.util.MCCESpawnCap;
import yeelp.mcce.util.PlayerUtils;

public final class InfestationEffect extends AbstractInstantChaosEffect {

	private static final Identifier GEORGE_HEALTH_BOOST_NAME = MCCE.createIdentifier("george_boss_boost");
	private static final float GEORGE_HEALTH_TOTAL = 200.0f;
	private static final int GEORGE_HEALTH_BOOST_AMOUNT = (int) (GEORGE_HEALTH_TOTAL - new SilverfishEntity(EntityType.SILVERFISH, null).getMaxHealth());
	private static final double ENDERMITE_CHANCE = 0.5, GEORGE_CHANCE = 0.33;
	private static final double HORIZONTAL_SPAWN_BOUND_MAX = 1.3;
	private static final double VERTICAL_SPAWN_BOUND_MAX = 0.6;
	private static final double HORIZONTAL_VELOCITY_MAX = 1;

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		for(int amount = this.getRNG().nextInt(5) + 3; amount > 0; amount--) {
			LivingEntity entity;
			if(Math.random() < ENDERMITE_CHANCE) {
				entity = new EndermiteEntity(EntityType.ENDERMITE, world);
			}
			else {
				entity = new SilverfishEntity(EntityType.SILVERFISH, world);
				if(Math.random() < GEORGE_CHANCE) {
					entity.setCustomName(Text.empty().append("George").formatted(Formatting.RED));
					AttributeUtils.addAttributeModifier(entity, EntityAttributes.MAX_HEALTH, new EntityAttributeModifier(GEORGE_HEALTH_BOOST_NAME, GEORGE_HEALTH_BOOST_AMOUNT, Operation.ADD_VALUE));
					entity.heal(GEORGE_HEALTH_TOTAL);
					entity.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.MUSIC_DISC_BLOCKS));
					((SilverfishEntity) entity).updateDropChances(EquipmentSlot.OFFHAND);
				}
			}
			entity.refreshPositionAndAngles(player.getX() + this.getRNG().nextDouble(-HORIZONTAL_SPAWN_BOUND_MAX, HORIZONTAL_SPAWN_BOUND_MAX), player.getY() + this.getRNG().nextDouble(VERTICAL_SPAWN_BOUND_MAX) + 2, player.getZ() + this.getRNG().nextDouble(-HORIZONTAL_SPAWN_BOUND_MAX, HORIZONTAL_SPAWN_BOUND_MAX), 0.0f, 0.0f);
			entity.setVelocity(this.getRNG().nextDouble(-HORIZONTAL_VELOCITY_MAX, HORIZONTAL_VELOCITY_MAX), this.getRNG().nextDouble(), this.getRNG().nextDouble(-HORIZONTAL_VELOCITY_MAX, HORIZONTAL_VELOCITY_MAX));
			MCCESpawnCap.MOB.attemptEntitySpawn(world, entity);
		}
	}

	@Override
	public String getName() {
		return "infestation";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SUDDEN_DEATH) && PlayerUtils.doesPlayerHaveValidPosition(player);
	}

}
