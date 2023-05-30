package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;

public abstract class MultiChaosEffect extends AbstractInstantChaosEffect {

	private int times;
	
	protected MultiChaosEffect(int times) {
		this.times = times;
	}
	
	@Override
	public final void applyEffect(PlayerEntity player) {
		boolean preventRecursion = false;
		do {
			ChaosEffect ce = getEffect(player, preventRecursion);
			MCCE.LOGGER.info(ce.getName());
			MCCEAPI.mutator.addNewChaosEffect(player, ce);
			preventRecursion = true;
		}while(--this.times > 0);
	}

	@Override
	protected final boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}
	
	private ChaosEffect getEffect(PlayerEntity player, boolean preventRecursion) {
		ChaosEffect ce = ChaosEffectRegistry.getRandomApplicableEffectForPlayer(player);
		if(this.getClass().isInstance(ce) && (preventRecursion || this.getRNG().nextFloat() < 0.5f)) {
			while(this.getClass().isInstance(ce = ChaosEffectRegistry.getRandomApplicableEffectForPlayer(player)));
		}
		return ce;
	}

}
