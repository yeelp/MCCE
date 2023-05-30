package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public class PressLToLevitateEffect extends AbstractTimedChaosEffect {

	public PressLToLevitateEffect() {
		super(800, 1800);
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
		return;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setNoGravity(false);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
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
