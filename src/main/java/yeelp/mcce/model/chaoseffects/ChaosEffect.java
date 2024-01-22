package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.util.Tracker;

/**
 * The root interface of Chaos Effects. All Chaos Effects must implement this
 * interface.
 * 
 * @author Yeelp
 */
public interface ChaosEffect {

	/**
	 * Is this an instant Chaos Effect? Instant effects are applied immediately and
	 * not saved to a player's {@link PlayerChaosEffectState}.
	 * 
	 * @return {@code true} if this Chaos Effect is an instant effect, and
	 *         {@code false} if not.
	 */
	boolean isInstant();

	/**
	 * Apply this Chaos Effect to {@code player}. Implementors should have the Chaos
	 * Effect perform its desired effect here. If the Chaos Effect relies on
	 * callbacks (like those injected via mixins), then this method should be used
	 * to keep track of which players are under the effect of instances of this
	 * Chaos Effect.
	 * 
	 * @param player Player to affect
	 * @see ChaosEffect#registerCallbacks()
	 * @see Tracker
	 */
	void applyEffect(PlayerEntity player);

	/**
	 * Get the name of this Chaos Effect.
	 * 
	 * @return The name of this Chaos Effect.
	 */
	String getName();

	/**
	 * If this Chaos Effect relies on callbacks (like those injected via mixins),
	 * then those callbacks will be registered in this method.
	 */
	void registerCallbacks();

	/**
	 * Write this ChaosEffect to NBT.
	 * 
	 * @return an {@link NbtCompound} containing the serialized data of this
	 *         ChaosEffect.
	 */
	NbtCompound writeToNbt();

	/**
	 * Read data from NBT and update the state of this ChaosEffect.
	 * 
	 * @param nbt The {@link NbtCompound} to read from.
	 */
	void readNbt(NbtCompound nbt);

	/**
	 * How much longer this ChaosEffect will last, in ticks.
	 * 
	 * @return The remaining duration in ticks.
	 */
	int durationRemaining();

	/**
	 * Get the remaining duration until this ChaosEffect should reapply its effects.
	 * 
	 * @return The remaining duration until the next activation.
	 */
	int getDurationUntilNextActivation();

	/**
	 * A cleanup method that gets called when this ChaosEffect expires. This is to
	 * end the effect on the player, if any such operations are required.
	 * 
	 * @param player Player that this effect should end on.
	 */
	void onEffectEnd(PlayerEntity player);

	/**
	 * Tick this effect.
	 * 
	 * @param player The player this ChaosEffect is currently affecting.
	 */
	void tickEffect(PlayerEntity player);

	/**
	 * Can this ChaosEffect be applied to this player?
	 * 
	 * @param player Player to check against.
	 * @return {@code true} if this ChaosEffect can be applied to this player, and
	 *         {@code false} if not
	 */
	boolean applicable(PlayerEntity player);

	/**
	 * Can this ChaosEffect modify a player's {@link PlayerChaosEffectState}? If
	 * {@link ChaosEffect#isInstant()} returns {@code true}, then the value returned
	 * from this method does not matter, as it will never be called for instant
	 * ChaosEffects. This is called on ChaosEffects which are not instant which may
	 * alter a player's list of active ChaosEffects. These ChaosEffects are ticked
	 * separately to avoid concurrent modification.
	 * 
	 * @return {@code true} if this ChaosEffect can modify player's effect state,
	 *         and {@code false} if not.
	 */
	default boolean canModifyEffectState() {
		return false;
	}
}
