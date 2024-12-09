package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.RainbowStatusPayload;

public final class RainbowEffect extends StatusPayloadSendingChaosEffect<RainbowStatusPayload> {

	private static final int DURATION_MIN = 1000, DURATION_MAX = 2000;
	public RainbowEffect() {
		super(DURATION_MIN, DURATION_MAX, RainbowStatusPayload::new);
	}

	@Override
	public String getName() {
		return "rainbow";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MEMORY_GAME);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
