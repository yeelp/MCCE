package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public abstract class AbstractRainEffect extends SimpleTimedChaosEffect {

	private static final double APPLY_CHANCE = 0.75;
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
		return player.getWorld().isSkyVisible(player.getBlockPos()) && Math.random() < APPLY_CHANCE;
	}
	
	protected abstract Entity getEntityToSpawn(PlayerEntity player);

}
