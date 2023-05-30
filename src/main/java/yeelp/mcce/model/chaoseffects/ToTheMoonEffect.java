package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;

public final class ToTheMoonEffect extends SimpleTimedChaosEffect {

	private boolean soundPlayed = false;
	public ToTheMoonEffect() {
		super(20, 150);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.addVelocity(0, Math.E, 0);
		player.velocityModified = true;
		if(!this.soundPlayed && player instanceof ServerPlayerEntity) {
			new SoundPacket(NetworkingConstants.SoundPacketConstants.FIREWORK_LAUNCHES_ID, 1.0f, 1.0f).sendPacket((ServerPlayerEntity) player);
			this.soundPlayed = true;
		}
	}

	@Override
	public String getName() {
		return "tothemoon";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	@Override
	protected boolean canStack() {
		return true;
	}
	
	

}
