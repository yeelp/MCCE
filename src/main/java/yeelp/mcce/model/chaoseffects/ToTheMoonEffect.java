package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public final class ToTheMoonEffect extends SimpleTimedChaosEffect {

	private boolean soundPlayed = false;
	public ToTheMoonEffect() {
		super(20, 150);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.addVelocity(0, Math.E, 0);
		player.velocityModified = true;
		if(!this.soundPlayed) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.FIREWORK_LAUNCHES_ID, 1.0f, 1.0f)::sendPacket);
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
