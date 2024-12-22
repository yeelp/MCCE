package yeelp.mcce.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.model.ServerState;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;

import java.util.function.Consumer;

/**
 * A collection of methods for altering player state
 *
 * @author Yeelp
 */
public interface MCCEAPIMutator {

    /**
     * Add a new {@link ChaosEffect} to a player. This overwrites any previous
     * effect active.
     *
     * @param player the player to add the effect to.
     * @param effect the effect to add.
     */
    void addNewChaosEffect(PlayerEntity player, ChaosEffect effect);

    /**
     * Remove a {@link ChaosEffect} from a player.
     *
     * @param player the player to remove the effect from. After this method
     *               returns, the player will not have any instance of {@code clazz}
     *               effect active on them.
     * @param entry  the registry entry of the chaos effect to remove
     * @return true if the effect was removed (i.e. it existed on the target before
     * this method call), false if not (the effect never existed on the
     * target)
     */
    boolean removeChaosEffect(PlayerEntity player, ChaosEffectRegistryEntry entry);

    /**
     * Removes all {@link ChaosEffect}s from a player
     *
     * @param player player to remove effects from.
     */
    void clear(PlayerEntity player);

    /**
     * Modify a player's {@link PlayerChaosEffectState}. This utility method
     * eliminates overhead as it wraps the modification in a "get ServerState" type
     * method and marks the active {@link ServerState} as dirty
     *
     * @param player       player's effect state to modify
     * @param modification the modification to make.
     */
    void modifyEffectState(PlayerEntity player, Consumer<PlayerChaosEffectState> modification);

    /**
     * Modifies a player's instance of a {@link ChaosEffect} active on their {@link PlayerChaosEffectState}.
     *
     * @param player       player's effect state to modify
     * @param clazz        the effect class to alter the instance of
     * @param modification the modification to make
     */
    <E extends ChaosEffect> void modifyEffect(PlayerEntity player, Class<E> clazz, Consumer<E> modification);

    /**
     * Set a despawn timer for an entity
     *
     * @param entity   The entity to set the timer for.
     * @param duration the duration to set.
     */
    void setDespawnTimer(Entity entity, int duration);

    /**
     * Copy a despawn timer from one Entity to another. Only copies the despawn timer if the Entity {@code from} has one set and will set the despawn timer on {@code to} if that's the case.
     *
     * @param from The Entity to copy from.
     * @param to The Entity to copy to.
     */
    void copyDespawnTimerTo(Entity from, Entity to);

}
