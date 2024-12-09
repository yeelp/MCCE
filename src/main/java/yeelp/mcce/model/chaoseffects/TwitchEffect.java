package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class TwitchEffect extends AbstractInstantChaosEffect {

	private static final double STRENGTH = 2.5;
	private static final double Y_STRENGTH_MIN = 0.33, Y_STRENGTH_MAX = 1.0;
	@Override
	public void applyEffect(PlayerEntity player) {
		Vec3d direction = new Vec3d(1, 0, 0);
		double magnitude = this.getRNG().nextDouble(1, STRENGTH);
		direction = direction.multiply(magnitude);
		direction = direction.rotateY(this.getRNG().nextFloat((float) (2*Math.PI)));
		direction = direction.add(0, this.getRNG().nextDouble(Y_STRENGTH_MIN, Y_STRENGTH_MAX), 0);
		player.addVelocityInternal(direction);
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
