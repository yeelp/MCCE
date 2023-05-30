package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class BouncyEffect extends SimpleTimedChaosEffect {

	protected BouncyEffect() {
		super(1500, 2300);
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

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.areAnyChaosEffectsActive(player, PressLToLevitateEffect.class, ToTheMoonEffect.class);
	}

}
