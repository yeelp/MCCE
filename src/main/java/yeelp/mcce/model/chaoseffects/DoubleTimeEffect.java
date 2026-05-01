package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.PlayerChaosEffectState;

public final class DoubleTimeEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 500, DURATION_MAX = 1400;
	public DoubleTimeEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		MCCEAPI.mutator.modifyEffectState(player, PlayerChaosEffectState::tickDurationUntilNextEffect);
	}

	@Override
	public String getName() {
		return "doubletime";
	}

	@Override
	public String getDisplayName() {
		return "Double Time";
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
