package yeelp.mcce.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.ModConfig;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;
import yeelp.mcce.model.chaoseffects.ChaosEffects;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

public final class ChaosEffectTracker implements PlayerTickCallback {

	private static final Map<UUID, LagTimer> LAST_CALL_TIME = Maps.newHashMap();
	private static class LagTimer {
		private long lastTime;
		private short count;
		private static final int TIME_THRESHOLD_SEC = 5;
		private static final int NANOSEC_PER_SEC = 1_000_000_000;
		private static final int LAG_COUNT_THRESHOLD = 5;

		LagTimer() {
			this.lastTime = System.nanoTime();
		}

		boolean isBehind() {
			this.update();
			return this.count >= LAG_COUNT_THRESHOLD;
		}

		private void update() {
			long diff = Math.abs(this.lastTime - (this.lastTime = System.nanoTime()));
			if(diff / NANOSEC_PER_SEC > TIME_THRESHOLD_SEC) {
				this.count++;
			}
			else {
				this.count = 0;
			}
		}
	}

	@Override
	public void tick(PlayerEntity player) {
		if(player.getWorld().isClient || player.isDead() || player.isSpectator()) {
			return;
		}
		MCCEAPI.mutator.modifyEffectState(player, (pces) -> {
			boolean alreadyCulled = false;
			pces.tickDurationUntilNextEffect();
			if(pces.hasDurationUntilNextEffectExpired()) {
				ChaosEffect ce = ChaosEffectRegistry.getRandomApplicableEffectForPlayer(player);
				alreadyCulled = ChaosEffects.CULL.is(ce);
				pces.addNewEffect(player, ce);
				pces.resetDurationUntilNextEffect();
			}
			Queue<ChaosEffect> effectsToRemove = Lists.newLinkedList();
			Queue<ChaosEffect> processSeparately = Lists.newLinkedList();
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
			if(getLagTimer(player.getUuid()).filter(LagTimer::isBehind).isPresent() && !alreadyCulled) {
				pces.addNewEffect(player, ChaosEffects.CULL.createChaosEffect());
			}
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

	private static Optional<LagTimer> getLagTimer(UUID uuid) {
		if(!ModConfig.getInstance().performance.enableMassEntityDeletionWhenLagDetected) {
			return Optional.empty();
		}
		if(!LAST_CALL_TIME.containsKey(uuid)) {
			LAST_CALL_TIME.put(uuid, new LagTimer());
		}
		return Optional.of(LAST_CALL_TIME.get(uuid));
	}
}
