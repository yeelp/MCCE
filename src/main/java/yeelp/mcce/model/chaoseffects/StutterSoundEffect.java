package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.StutterSoundStatusPayload;

public final class StutterSoundEffect extends StatusPayloadSendingChaosEffect<StutterSoundStatusPayload> {

	private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
	public StutterSoundEffect() {
		super(DURATION_MIN, DURATION_MAX, StutterSoundStatusPayload::new);
	}

	@Override
	public String getName() {
		return "stuttersound";
	}

	@Override
	public String getDisplayName() {
		return "Stutter Sound";
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
