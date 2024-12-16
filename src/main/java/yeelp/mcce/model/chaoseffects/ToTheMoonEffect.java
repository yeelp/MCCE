package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class ToTheMoonEffect extends SimpleTimedChaosEffect {

	private boolean soundPlayed = false;
	private static final int DURATION_MIN = 20, DURATION_MAX = 150;

	public ToTheMoonEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.addVelocity(0, Math.E, 0);
		player.velocityModified = true;
		if(!this.soundPlayed) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.FIREWORK_LAUNCHES_ID, 1.0f, 1.0f)::send);
			this.soundPlayed = true;
		}
	}

	@Override
	public String getName() {
		return "tothemoon";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SHAKEWEIGHT);
	}

	@Override
	protected boolean canStack() {
		return true;
	}
	
	

}
