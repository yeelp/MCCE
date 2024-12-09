package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class PressLToLevitateEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 800, DURATION_MAX = 1800;
	public PressLToLevitateEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setNoGravity(true);
	}

	@Override
	public String getName() {
		return "pressltolevitate";
	}

	@Override
	public void registerCallbacks() {
		//no callbacks
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setNoGravity(false);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
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
