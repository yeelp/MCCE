package yeelp.mcce.model.chaoseffects;

import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;

public class GhastEffect extends AbstractInstantChaosEffect {
	
	private static final UUID FOLLOW_RANGE_BOOST = UUID.fromString("664ba2a0-41cb-4090-8916-6873039cb5f8");

	@Override
	public void applyEffect(PlayerEntity player) {
		Box outer = ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 20);
		Box inner = ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 8);
		World world = player.getWorld();
		ChaosLib.getPosWithin(outer, inner, (pos) -> world.isAir(pos) && world.isAir(pos.up()) && world.isAir(pos.up(2)), 50, this.getRNG()).ifPresent((pos) -> {
			GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
			ghast.refreshPositionAndAngles(pos, this.getRNG().nextFloat(180f), this.getRNG().nextFloat(180f));
			ghast.setTarget(player);
			ghast.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier(FOLLOW_RANGE_BOOST, "Ghast Follow Range Boost", 500, Operation.ADDITION));
			if(this.getRNG().nextInt(100) < 30) {
				ghast.setInvulnerable(true);
			}
			ghast.setPersistent();
			MCCEAPI.mutator.setDespawnTimer(ghast, 1200);
			world.spawnEntity(ghast);
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
