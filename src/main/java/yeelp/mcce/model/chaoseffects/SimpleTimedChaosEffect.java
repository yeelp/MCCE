package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public abstract class SimpleTimedChaosEffect extends AbstractTimedChaosEffect {

	protected SimpleTimedChaosEffect(int durationMin, int durationMax) {
		super(durationMin, durationMax);
	}

	@Override
	public void registerCallbacks() {
		//no callbacks
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		//no on effect end
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		this.applyEffect(player);
	}

}
