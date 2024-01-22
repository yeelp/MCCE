package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public abstract class AbstractLastingChaosEffect extends AbstractChaosEffect {

	protected static final String DURATION_KEY = "duration";
	private int duration;
	
	protected AbstractLastingChaosEffect(int duration) {
		this.duration = duration;
	}
	
	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public int durationRemaining() {
		return this.duration;
	}

	@Override
	public void tickEffect(PlayerEntity player) {
		if(--this.duration <= 0) {
			this.onEffectEnd(player);
		}
		else {
			this.tickAdditionalEffectLogic(player);
		}
	}
	
	protected void setDuration(int duration) {
		this.duration = duration;
	}
	
	protected abstract void tickAdditionalEffectLogic(PlayerEntity player);
	
	protected static int getIntInRange(int min, int max) {
		return (int) ((max - min + 1) * Math.random()) + min;
	}

}
