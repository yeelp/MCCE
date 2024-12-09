package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class BouncyEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1500, DURATION_MAX = 2300;
	public BouncyEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setNoDrag(true);
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setNoDrag(false);
	}

	@Override
	public String getName() {
		return "bouncy";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.PRESS_L_TO_LEVITATE, ChaosEffects.TO_THE_MOON, ChaosEffects.CLIPPY);
	}

}
