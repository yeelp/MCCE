package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class TwitchEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		Vec3d direction = new Vec3d(1, 0, 0);
		double magnitude = this.getRNG().nextDouble(1, 2.5);
		direction = direction.multiply(magnitude);
		direction = direction.rotateY(this.getRNG().nextFloat((float) (2*Math.PI)));
		direction = direction.add(0, this.getRNG().nextDouble(0.33, 1), 0);
		player.addVelocity(direction);
		player.velocityModified = true;
	}

	@Override
	public String getName() {
		return "twitch";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
