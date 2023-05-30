package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class DoubleTimeEffect extends SimpleTimedChaosEffect {

	protected DoubleTimeEffect() {
		super(500, 1400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		MCCEAPI.mutator.modifyEffectState(player, (pces) -> pces.tickDurationUntilNextEffect());
	}

	@Override
	public String getName() {
		return "doubletime";
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
