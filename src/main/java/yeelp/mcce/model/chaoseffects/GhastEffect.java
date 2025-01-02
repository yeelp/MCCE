package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.AttributeUtils;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.MCCESpawnCap;

public final class GhastEffect extends AbstractInstantChaosEffect {
	
	private static final Identifier FOLLOW_RANGE_BOOST = MCCE.createIdentifier("ghastfollowrange");
	private static final int FOLLOW_RANGE_BOOST_AMOUNT = 500;
	private static final int DESPAWN_TIMER_AMOUNT = 1200;
	private static final int INVULNERABLE_CHANCE = 30;
	private static final int OUTER_SPAWN_RADIUS = 20, INNER_SPAWN_RADIUS = 8;
	private static final int SPAWN_TRIES = 50;
	private static final float ROTATION_ANGLE_BOUND = 180.0f;

	@Override
	public void applyEffect(PlayerEntity player) {
		Box outer = ChaosLib.getBoxCenteredOnPlayerWithRadius(player, OUTER_SPAWN_RADIUS);
		Box inner = ChaosLib.getBoxCenteredOnPlayerWithRadius(player, INNER_SPAWN_RADIUS);
		World world = player.getWorld();
		ChaosLib.getPosWithin(outer, inner, (pos) -> world.isAir(pos) && world.isAir(pos.up()) && world.isAir(pos.up(2)), SPAWN_TRIES, this.getRNG()).ifPresent((pos) -> {
			GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
			ghast.refreshPositionAndAngles(pos, this.getRNG().nextFloat(ROTATION_ANGLE_BOUND), this.getRNG().nextFloat(ROTATION_ANGLE_BOUND));
			ghast.setTarget(player);
			AttributeUtils.addAttributeModifier(ghast, EntityAttributes.FOLLOW_RANGE, new EntityAttributeModifier(FOLLOW_RANGE_BOOST, FOLLOW_RANGE_BOOST_AMOUNT, Operation.ADD_VALUE));
			if(this.getRNG().nextInt(100) < INVULNERABLE_CHANCE) {
				ghast.setInvulnerable(true);
			}
			ghast.setPersistent();
			MCCEAPI.mutator.setDespawnTimer(ghast, DESPAWN_TIMER_AMOUNT);
			MCCESpawnCap.MOB.attemptEntitySpawn(world, ghast);
		});
	}

	@Override
	public String getName() {
		return "ghast";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
