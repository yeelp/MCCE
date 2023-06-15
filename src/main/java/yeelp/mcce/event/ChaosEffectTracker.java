package yeelp.mcce.event;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;

public final class ChaosEffectTracker implements PlayerTickCallback {

	@SuppressWarnings("resource")
	@Override
	public void tick(PlayerEntity player) {
		if(player.getWorld().isClient || player.isDead()) {
			return;
		}
		MCCEAPI.mutator.modifyEffectState(player, (pces) -> {
			pces.tickDurationUntilNextEffect();
			if(pces.hasDurationUntilNextEffectExpired()) {
				ChaosEffect ce = ChaosEffectRegistry.getRandomApplicableEffectForPlayer(player);
				MCCE.LOGGER.info(ce.getName());
				pces.addNewEffect(player, ce);
				pces.resetDurationUntilNextEffect();
			}
			Queue<ChaosEffect> effectsToRemove = new LinkedList<ChaosEffect>();
			for(ChaosEffect ce : pces) {
				ce.tickEffect(player);
				if(ce.durationRemaining() <= 0) {
					effectsToRemove.add(ce);
				}
			}
			effectsToRemove.forEach(pces::removeEffect);
		});
	}

	@Override
	public int priority() {
		return 10;
	}
}
