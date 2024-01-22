package yeelp.mcce.model.chaoseffects;

import org.joml.Vector3d;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.util.ChaosLib;

public class MagnetEffect extends SimpleTimedChaosEffect {

	public MagnetEffect() {
		super(1200, 1500);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Vector3d playerPos = new Vector3d(new double[] {player.getX(), player.getY(), player.getZ()});
		player.getWorld().getEntitiesByClass(Entity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 40), (entity) -> !(entity instanceof PlayerEntity) && entity.getPassengerList().isEmpty()).forEach((entity) -> {
			Vector3d entityPos = new Vector3d(new double[] {entity.getX(), entity.getY(), entity.getZ()});
			Vector3d heading = new Vector3d();
			playerPos.sub(entityPos, heading).normalize(0.3);
			entityPos.add(heading);
			entity.teleport(entityPos.x, entityPos.y, entityPos.z);
		});
	}

	@Override
	public String getName() {
		return "magnet";
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
