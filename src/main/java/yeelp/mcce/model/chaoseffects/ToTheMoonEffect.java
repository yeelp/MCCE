package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class ToTheMoonEffect extends SimpleTimedChaosEffect {

	private boolean soundPlayed = false;
	private static final int DURATION_MIN = 20, DURATION_MAX = 150, YCUTOFF = 500;
	private static final Vec3d INPUT = new Vec3d(0, Math.E/2, 0);

	public ToTheMoonEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(player.getY() > YCUTOFF || player.getVelocity().y > 10 * Math.E) {
			return;
		}
		PlayerUtils.addPlayerVelocity(player, INPUT);
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
	public String getDisplayName() {
		return "To the Moon";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SHAKEWEIGHT, ChaosEffects.SMACK_DOWN) && player.getY() < YCUTOFF;
	}

	@Override
	protected boolean canStack() {
		return true;
	}
	
	

}
