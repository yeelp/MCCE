package yeelp.mcce.api;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.model.chaoseffects.ChaosEffect;

/**
 * A collection of methods for reading player state
 * @author Yeelp
 *
 */
public interface MCCEAPIAccessor {
	
	/**
	 * Get a player's effect state. Modifications made here are NOT synced to the server directly.
	 * @param player
	 * @return This player's {@link PlayerChaosEffectState}.
	 */
	PlayerChaosEffectState getPlayerChaosEffectState(PlayerEntity player);
	
	/**
	 * Get the active {@link ChaosEffect} instance on this player
	 * @param player
	 * @param clazz the class of the ChaosEffect to get
	 * @return An {@link Optional} wrapping the result if it exists, otherwise and empty Optional.
	 */
	<E extends ChaosEffect> Optional<E> getChaosEffect(PlayerEntity player, Class<E> clazz);
	
	/**
	 * Check if a player has a certain chaos effect active.
	 * @param clazz The class of the Chaos effect to check
	 * @param player The player to check
	 * @return true if the chaos effect is active, false if not
	 */
	boolean isChaosEffectActive(PlayerEntity player, Class<? extends ChaosEffect> clazz);
	
	/**
	 * Check if any of the passed Chaos effect classes are active on a player.
	 * @param player Player to check
	 * @param clazzes The classes to check
	 * @return true if at least one of the passed Chaos Effect classes is active.
	 */
	boolean areAnyChaosEffectsActive(PlayerEntity player, @SuppressWarnings("unchecked") Class<? extends ChaosEffect>... clazzes);
}
