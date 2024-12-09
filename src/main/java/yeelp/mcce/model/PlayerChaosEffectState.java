package yeelp.mcce.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.model.chaoseffects.AbstractLastingChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;

/**
 * Our player state for tracking which effects they have active.
 * 
 * @author Yeelp
 *
 */
@SuppressWarnings("UseOfConcreteClass")
public final class PlayerChaosEffectState implements Iterable<ChaosEffect> {

	private final Map<String, ChaosEffect> activeEffects = Maps.newHashMap();
	private int durationUntilNextEffect;
	private final Random rand = new Random(System.currentTimeMillis());
	private static final String EFFECT_KEY = "effects";
	private static final String TIME_KEY = "durationUntilNextEffect";
	private static final int INITIAL_DURATION = 500;
	private static final int DURATION_MIN = 200;
	private static final int DURATION_MAX = 1000;

	/**
	 * Create an empty PlayerChaosEffectState.
	 */
	public PlayerChaosEffectState() {
		this.durationUntilNextEffect = INITIAL_DURATION;
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
		effects.getKeys().forEach((s) -> this.activeEffects.put(s, ChaosEffectRegistry.createEffectFromNbt(s, effects.getCompound(s))));
		this.durationUntilNextEffect = nbt.getInt(TIME_KEY);
	}

	/**
	 * Add a new effect to this player state. Instant effects instead resolve
	 * immediately in this method and are not added.
	 * 
	 * @param player player that gets affected by this effect.
	 * @param effect The effect to add.
	 */
	@SuppressWarnings("FeatureEnvy")
    public void addNewEffect(PlayerEntity player, ChaosEffect effect) {
		effect.applyEffect(player);
		if(!effect.isInstant()) {
			this.activeEffects.put(effect.getName(), effect);
		}
	}

	public boolean isEffectActive(ChaosEffectRegistryEntry entry) {
		return this.activeEffects.containsKey(entry.getName());
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
		this.activeEffects.forEach((s, e) -> tag.put(s, e.writeToNbt()));
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

	@SuppressWarnings("unused")
    public int getDurationUntilNextEffect() {
		return this.durationUntilNextEffect;
	}
	
	public void resetDurationUntilNextEffect() {
		this.durationUntilNextEffect = this.rand.nextInt(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public @NotNull Iterator<ChaosEffect> iterator() {
		return this.activeEffects.values().iterator();
	}

	@Override
	public String toString() {
		StringBuilder effects = new StringBuilder();
        for (ChaosEffect ce : this) {
            if (ce instanceof AbstractLastingChaosEffect alce) {
                effects.append(String.format("(%s, %d)", ce.getName(), alce.durationRemaining()));
            }
        }
		return String.format("Time: %d, [%s]", this.durationUntilNextEffect, effects);
	}

}
