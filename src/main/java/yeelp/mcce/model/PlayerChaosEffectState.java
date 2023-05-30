package yeelp.mcce.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.model.chaoseffects.AbstractLastingChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;

/**
 * Our player state for tracking which effects they have active.
 * 
 * @author Yeelp
 *
 */
public final class PlayerChaosEffectState implements Iterable<ChaosEffect> {

	private final Map<String, ChaosEffect> activeEffects = Maps.newHashMap();
	private int durationUntilNextEffect;
	private final Random rand = new Random(System.currentTimeMillis());
	private static final String EFFECT_KEY = "effects";
	private static final String TIME_KEY = "durationUntilNextEffect";

	/**
	 * Create an empty PlayerChaosEffectState.
	 */
	public PlayerChaosEffectState() {
		this.durationUntilNextEffect = 500;
	}

	/**
	 * Create a PlayerChaosEffectState by loading from the passed NbtCompound. This
	 * constructor assumes the passed NbtCompound was one created by
	 * {@link PlayerChaosEffectState#writeToNbt()}
	 * 
	 * @param nbt NBT to load data from.
	 */
	public PlayerChaosEffectState(NbtCompound nbt) {
		super();
		NbtCompound effects = nbt.getCompound(EFFECT_KEY);
		effects.getKeys().forEach((s) -> {
			this.activeEffects.put(s, ChaosEffectRegistry.createEffectFromNbt(s, effects.getCompound(s)));
		});
		this.durationUntilNextEffect = nbt.getInt(TIME_KEY);
	}

	/**
	 * Add a new effect to this player state. Instant effects instead resolve
	 * immediately in this method and are not added.
	 * 
	 * @param player player that gets affected by this effect.
	 * @param effect The effect to add.
	 */
	public void addNewEffect(PlayerEntity player, ChaosEffect effect) {
		effect.applyEffect(player);
		if(!effect.isInstant()) {
			this.activeEffects.put(effect.getName(), effect);
		}
	}
	
	/**
	 * Remove an effect on this player state.
	 * @param effect The effect to remove.
	 */
	public void removeEffect(ChaosEffect effect) {
		this.activeEffects.remove(effect.getName());
	}

	/**
	 * Write the player state to NBT
	 * @return an NbtCompound that reflects this player state.
	 */
	public NbtCompound writeToNbt() {
		NbtCompound tag = new NbtCompound(), root = new NbtCompound();
		this.activeEffects.forEach((s, e) -> {
			tag.put(s, e.writeToNbt());
		});
		root.put(EFFECT_KEY, tag);
		root.putInt(TIME_KEY, this.durationUntilNextEffect);
		return root;
	}
	
	public boolean hasDurationUntilNextEffectExpired() {
		return this.durationUntilNextEffect <= 0;
	}
	
	public void tickDurationUntilNextEffect() {
		this.durationUntilNextEffect--;
	}
	
	public int getDurationUntilNextEffect() {
		return this.durationUntilNextEffect;
	}
	
	public void resetDurationUntilNextEffect() {
		this.durationUntilNextEffect = this.rand.nextInt(200, 1000);
	}

	@Override
	public Iterator<ChaosEffect> iterator() {
		return this.activeEffects.values().iterator();
	}

	@Override
	public String toString() {
		StringBuilder effects = new StringBuilder();
		Iterator<ChaosEffect> it = this.iterator();
		while(it.hasNext()) {
			ChaosEffect ce = it.next();
			if(ce instanceof AbstractLastingChaosEffect) {
				AbstractLastingChaosEffect alce = (AbstractLastingChaosEffect) ce;
				effects.append(String.format("(%s, %d)", ce.getName(), alce.durationRemaining()));				
			}
		}
		return String.format("Time: %d, [%s]", this.durationUntilNextEffect, effects.toString());
	}

}
