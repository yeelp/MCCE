package yeelp.mcce.event;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.player.PlayerEntity;
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
				pces.addNewEffect(player, ce);
				pces.resetDurationUntilNextEffect();
			}
			Queue<ChaosEffect> effectsToRemove = new LinkedList<ChaosEffect>();
			Queue<ChaosEffect> processSeparately = new LinkedList<ChaosEffect>();
			for(ChaosEffect ce : pces) {
				if(ce.canModifyEffectState()) {
					processSeparately.add(ce);
				}
				else if (tickChaosEffect(player, ce)) {
					effectsToRemove.add(ce);
				}
			}
			for(ChaosEffect ce : processSeparately) {
				if(tickChaosEffect(player, ce)) {
					effectsToRemove.add(ce);
				}
			}
			effectsToRemove.forEach(pces::removeEffect);
		});
	}
	
	/**
	 * Tick a {@link ChaosEffect} on a {@code player} then check if the duration is zero
	 * @param player Player to affect.
	 * @param effect ChaosEffect to tick.
	 * @return true if the ChaosEffect's duration via {@link ChaosEffect#durationRemaining()} is now zero.
	 */
	private static boolean tickChaosEffect(PlayerEntity player, ChaosEffect effect) {
		effect.tickEffect(player);
		return effect.durationRemaining() <= 0;
	}

	@Override
	public int priority() {
		return 10;
	}
}
