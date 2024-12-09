package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public final class OofEffect extends AbstractInstantChaosEffect {

	private static final double Y_DIRECTION_MIN = 0.15, Y_DIRECTION_MAX = 0.95;
	@Override
	public void applyEffect(PlayerEntity player) {
		Vec3d direction = new Vec3d(MathHelper.sin(ChaosLib.convertToRadians(player.getYaw())), 0, -MathHelper.cos(ChaosLib.convertToRadians(player.getYaw())));
		direction = direction.multiply(this.getRNG().nextDouble(4, 9));
		direction = direction.add(0, this.getRNG().nextDouble(Y_DIRECTION_MIN, Y_DIRECTION_MAX), 0);
		player.addVelocityInternal(direction);
		player.velocityModified = true;
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.KNOCKBACK_ID, 1.0f, 1.0f)::send);
	}

	@Override
	public String getName() {
		return "oof";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}
}
