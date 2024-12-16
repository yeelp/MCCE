package yeelp.mcce.api;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;

import java.util.Optional;

/**
 * A collection of methods for reading player state
 * @author Yeelp
 *
 */
@SuppressWarnings("UseOfConcreteClass")
public interface MCCEAPIAccessor {
	
	/**
	 * Get a player's effect state. Modifications made here are NOT synced to the server directly.
	 * @param player player to target
	 * @return This player's {@link PlayerChaosEffectState}.
	 */
	PlayerChaosEffectState getPlayerChaosEffectState(PlayerEntity player);
	
	/**
	 * Get the active {@link ChaosEffect} instance on this player
	 * @param player player to target
	 * @param clazz the class of the ChaosEffect to get
	 * @return An {@link Optional} wrapping the result if it exists, otherwise and empty Optional.
	 */
	<E extends ChaosEffect> Optional<E> getChaosEffect(PlayerEntity player, Class<E> clazz);

	/**
	 * Check if a player has a certain chaos effect active.
	 * @param entry The registry entry of the chaos effect to check
	 * @param player The player to check
	 * @return true if the chaos effect is active, false if not
	 */
	boolean isChaosEffectActive(PlayerEntity player, ChaosEffectRegistryEntry entry);

	/**
	 * Check if any of the passed chaos effects are active on a player
	 * @param player the Player to check
	 * @param entries an array of ChaosEffectRegistryEntry to check
	 * @return false if at least one of the entries are active on the player
	 */
	boolean areChaosEffectsNotActive(PlayerEntity player, ChaosEffectRegistryEntry... entries);

	/**
	 * Check if at least one of the passed chaos effects are active on a player
	 * @param player the Player to check
	 * @param entries an array of ChaosEffectRegistryEntry to check
	 * @return true if at least one of the effects is active
	 */
	default boolean areAnyChaosEffectsActive(PlayerEntity player, ChaosEffectRegistryEntry... entries) {
		for(ChaosEffect effect : this.getPlayerChaosEffectState(player)) {
			for(ChaosEffectRegistryEntry entry : entries) {
				if(entry.is(effect)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Has this player had chaos effects applied before. Some effects can't be the very first effect applied.
	 * @param player player to check
	 * @return True if this player has had chaos effect applied before.
	 */
	default boolean hasHadEffectsBefore(PlayerEntity player) {
		return this.getPlayerChaosEffectState(player).hasHadEffectsBefore();
	}
}
