package yeelp.mcce.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.api.MCCEAPIAccessor;
import yeelp.mcce.api.MCCEAPIMutator;
import yeelp.mcce.model.PlayerChaosEffectState;
import yeelp.mcce.model.ServerState;
import yeelp.mcce.model.chaoseffects.ChaosEffect;

public enum MCCEAPIImpl implements MCCEAPIAccessor, MCCEAPIMutator {
	INSTANCE;

	private MCCEAPIImpl() {
		MCCEAPI.accessor = this;
		MCCEAPI.mutator = this;
	}

	@Override
	public PlayerChaosEffectState getPlayerChaosEffectState(PlayerEntity player) {
		return getEffectStateFromServerState(getServerState(player), player);
	}

	@Override
	public <E extends ChaosEffect> Optional<E> getChaosEffect(PlayerEntity player, Class<E> clazz) {
		Iterator<ChaosEffect> it = this.getPlayerChaosEffectState(player).iterator();
		ChaosEffect ce = null;
		while(it.hasNext()) {
			if(clazz.isInstance((ce = it.next()))) {
				return Optional.of(clazz.cast(ce));
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean isChaosEffectActive(PlayerEntity player, Class<? extends ChaosEffect> clazz) {
		Iterator<ChaosEffect> it = this.getPlayerChaosEffectState(player).iterator();
		boolean b;
		for(b = false; !b && it.hasNext(); b = clazz.isInstance(it.next()));
		return b;
	}

	@Override
	public boolean areAnyChaosEffectsActive(PlayerEntity player, @SuppressWarnings("unchecked") Class<? extends ChaosEffect>... clazzes) {
		List<Class<? extends ChaosEffect>> effects = new ArrayList<Class<? extends ChaosEffect>>();
		for(Class<? extends ChaosEffect> class1 : clazzes) {
			effects.add(class1);
		}
		for(ChaosEffect ce : this.getPlayerChaosEffectState(player)) {
			for(int i = 0; i < effects.size(); i++) {
				if(effects.get(i).isInstance(ce)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addNewChaosEffect(PlayerEntity player, ChaosEffect effect) {
		alterServerState(player, (state) -> getEffectStateFromServerState(state, player).addNewEffect(player, effect));
	}

	@Override
	public boolean removeChaosEffect(PlayerEntity player, Class<? extends ChaosEffect> clazz) {
		if(!this.isChaosEffectActive(player, clazz)) {
			return false;
		}
		alterServerState(player, (state) -> {
			PlayerChaosEffectState pces = getEffectStateFromServerState(state, player);
			Iterator<ChaosEffect> effects = pces.iterator();
			ChaosEffect ce = null;
			while(!clazz.isInstance(ce = effects.next()));
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
			Iterator<ChaosEffect> it = getEffectStateFromServerState(state, player).iterator();
			while(it.hasNext()) {
				ChaosEffect ce = null;
				if(clazz.isInstance(ce = it.next())) {
					modification.accept(clazz.cast(ce));
					return;
				}
			}
		});
	}

	@Override
	public void setDespawnTimer(Entity entity, int duration) {
		//Players can't despawn.
		if(entity instanceof PlayerEntity) {
			return;
		}
		ServerState state = ServerState.getServerState(entity.getServer());
		state.getDespawnTimer(entity.getUuid()).setTimer(duration);
		state.markDirty();
	}

	private static PlayerChaosEffectState getEffectStateFromServerState(ServerState state, PlayerEntity player) {
		return state.getEffectState(player.getUuid());
	}

	private static ServerState getServerState(PlayerEntity player) {
		return ServerState.getServerState(player.getServer());
	}

	private static void alterServerState(PlayerEntity player, Consumer<ServerState> alteration) {
		ServerState state = getServerState(player);
		alteration.accept(state);
		state.markDirty();
	}
}
