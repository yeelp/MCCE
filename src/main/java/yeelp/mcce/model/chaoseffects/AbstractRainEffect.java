package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public abstract class AbstractRainEffect extends SimpleTimedChaosEffect {

	protected AbstractRainEffect(int durationMin, int durationMax) {
		super(durationMin, durationMax);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getWorld().spawnEntity(this.getEntityToSpawn(player));
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().isSkyVisible(player.getBlockPos()) && Math.random() < 0.75;
	}
	
	protected abstract Entity getEntityToSpawn(PlayerEntity player);

}
