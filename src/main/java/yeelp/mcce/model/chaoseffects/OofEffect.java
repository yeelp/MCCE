package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;

public class OofEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		Vec3d direction = new Vec3d(MathHelper.sin(player.getYaw() * ((float) Math.PI/180)), 0, -MathHelper.cos(player.getYaw() * ((float) Math.PI/180)));
		direction = direction.multiply(this.getRNG().nextDouble(4, 9));
		direction = direction.add(0, this.getRNG().nextDouble(0.15, 0.95), 0);
		player.addVelocity(direction);
		player.velocityModified = true;
		if(player instanceof ServerPlayerEntity) {
			new SoundPacket(NetworkingConstants.SoundPacketConstants.KNOCKBACK_ID, 1.0f, 1.0f).sendPacket((ServerPlayerEntity) player);
		}
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
