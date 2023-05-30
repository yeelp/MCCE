package yeelp.mcce.model.chaoseffects;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public abstract class AbstractChaosEffect implements ChaosEffect {
	
	protected Random rand;
	
	protected AbstractChaosEffect() {
		this.rand = new Random(System.currentTimeMillis());
	}
	
	protected Random getRNG() {
		return this.rand;
	}
	
	protected abstract boolean canStack();
	
	protected abstract boolean isApplicableIgnoringStackability(PlayerEntity player);

	@Override
	public final boolean applicable(PlayerEntity player) {
		return (!MCCEAPI.accessor.isChaosEffectActive(player, this.getClass()) || this.canStack()) && this.isApplicableIgnoringStackability(player);
	}
}
