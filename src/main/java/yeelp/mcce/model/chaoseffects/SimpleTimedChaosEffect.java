package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public abstract class SimpleTimedChaosEffect extends AbstractTimedChaosEffect {

	protected SimpleTimedChaosEffect(int durationMin, int durationMax) {
		super(durationMin, durationMax);
	}

	@Override
	public void registerCallbacks() {
		return;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		return;
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		this.applyEffect(player);
	}

}
