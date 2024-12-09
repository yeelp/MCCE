package yeelp.mcce.api.impl;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.api.MCCEAPIAccessor;
import yeelp.mcce.api.MCCEAPIMutator;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.model.ServerState;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("UseOfConcreteClass")
public enum MCCEAPIImpl implements MCCEAPIAccessor, MCCEAPIMutator {
    INSTANCE;

    MCCEAPIImpl() {
        MCCEAPI.accessor = this;
        MCCEAPI.mutator = this;
    }

    @SuppressWarnings("UseOfConcreteClass")
    @Override
    public PlayerChaosEffectState getPlayerChaosEffectState(PlayerEntity player) {
        return getEffectStateFromServerState(getServerState(player), player);
    }

    @Override
    public <E extends ChaosEffect> Optional<E> getChaosEffect(PlayerEntity player, Class<E> clazz) {
        Iterator<ChaosEffect> it = this.getPlayerChaosEffectState(player).iterator();
        ChaosEffect ce;
        while (it.hasNext()) {
            if (clazz.isInstance((ce = it.next()))) {
                return Optional.of(clazz.cast(ce));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isChaosEffectActive(PlayerEntity player, ChaosEffectRegistryEntry entry) {
        return this.getPlayerChaosEffectState(player).isEffectActive(entry);
    }

    @Override
    public boolean areChaosEffectsNotActive(PlayerEntity player, ChaosEffectRegistryEntry... entries) {
        PlayerChaosEffectState state = this.getPlayerChaosEffectState(player);
        return Arrays.stream(entries).noneMatch(state::isEffectActive);
    }

    @Override
    public void addNewChaosEffect(PlayerEntity player, ChaosEffect effect) {
        alterServerState(player, (state) -> getEffectStateFromServerState(state, player).addNewEffect(player, effect));
    }

    @Override
    public boolean removeChaosEffect(PlayerEntity player, ChaosEffectRegistryEntry entry) {
        if (!this.isChaosEffectActive(player, entry)) {
            return false;
        }
        alterServerState(player, (state) -> {
            PlayerChaosEffectState pces = getEffectStateFromServerState(state, player);
            Iterator<ChaosEffect> effects = pces.iterator();
            ChaosEffect ce;
            while (!entry.is(ce = effects.next())) ;
            ce.onEffectEnd(player);
            pces.removeEffect(ce);
        });
        return true;
    }

    @Override
    public void clear(PlayerEntity player) {
        Queue<ChaosEffect> effects = Lists.newLinkedList();
        this.getPlayerChaosEffectState(player).forEach(effects::add);
        alterServerState(player, (state) -> {
            PlayerChaosEffectState pces = getEffectStateFromServerState(state, player);
            effects.forEach((ce) -> ce.onEffectEnd(player));
            effects.forEach(pces::removeEffect);
        });
    }

    @Override
    public void modifyEffectState(PlayerEntity player, Consumer<PlayerChaosEffectState> modification) {
        alterServerState(player, (state) -> modification.accept(getEffectStateFromServerState(state, player)));
    }

    @Override
    public <E extends ChaosEffect> void modifyEffect(PlayerEntity player, Class<E> clazz, Consumer<E> modification) {
        alterServerState(player, (state) -> {
            for (ChaosEffect chaosEffect : getEffectStateFromServerState(state, player)) {
                ChaosEffect ce;
                if (clazz.isInstance(ce = chaosEffect)) {
                    modification.accept(clazz.cast(ce));
                    return;
                }
            }
        });
    }

    @Override
    public void setDespawnTimer(Entity entity, int duration) {
        //Players can't despawn.
        if (entity instanceof PlayerEntity) {
            return;
        }
        ServerState state = ServerState.getServerState(Objects.requireNonNull(entity.getServer()));
        state.getDespawnTimer(entity.getUuid()).setTimer(duration);
        state.markDirty();
    }

    private static PlayerChaosEffectState getEffectStateFromServerState(ServerState state, PlayerEntity player) {
        return state.getEffectState(player.getUuid());
    }

    private static ServerState getServerState(PlayerEntity player) {
        return ServerState.getServerState(Objects.requireNonNull(player.getServer()));
    }

    private static void alterServerState(PlayerEntity player, Consumer<ServerState> alteration) {
        ServerState state = getServerState(player);
        alteration.accept(state);
        state.markDirty();
    }
}
