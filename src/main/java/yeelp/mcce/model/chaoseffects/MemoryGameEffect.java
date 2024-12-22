package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.MemoryGamePayload;

public final class MemoryGameEffect extends StatusPayloadSendingChaosEffect<MemoryGamePayload> {

	private static final int DURATION_MIN = 1000, DURATION_MAX  = 1500;
	public MemoryGameEffect() {
		super(DURATION_MIN, DURATION_MAX, MemoryGamePayload::new);
	}

	@Override
	public String getName() {
		return "memorygame";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.RAINBOW, ChaosEffects.SIMON_SAYS);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no additional effect logic
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
