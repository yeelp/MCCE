package yeelp.mcce.model.chaoseffects;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.StatusPacket;
import yeelp.mcce.util.Tracker;

public abstract class StatusPacketSendingChaosEffect<P extends StatusPacket> extends AbstractTimedChaosEffect {
	
	private static final Map<Class<? extends ChaosEffect>, PacketValidationHandler<? extends StatusPacket>> HANDLERS = Maps.newHashMap();
	private static final Map<Class<? extends ChaosEffect>, Tracker> TRACKERS = Maps.newHashMap();
	
	protected StatusPacketSendingChaosEffect(int durationMin, int durationMax, Function<Boolean, P> packetGenerator) {
		super(durationMin, durationMax);
		HANDLERS.computeIfAbsent(this.getClass(), (clazz) -> new PacketValidationHandler<P>(packetGenerator, clazz));
		TRACKERS.computeIfAbsent(this.getClass(), (clazz) -> new Tracker());
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		this.checkAndSendPacket(player, true);
		TRACKERS.get(this.getClass()).add(player);
	}

	@Override
	public void registerCallbacks() {
		PlayerTickCallback.EVENT.register(HANDLERS.get(this.getClass()));
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		this.checkAndSendPacket(player, false);
		TRACKERS.get(this.getClass()).remove(player);
	}
	
	private final void checkAndSendPacket(PlayerEntity player, boolean status) {
		if(player instanceof ServerPlayerEntity) {
			HANDLERS.get(this.getClass()).createAndSendPacket((ServerPlayerEntity) player, status);
		}
	}

	private static final class PacketValidationHandler<P extends StatusPacket> implements PlayerTickCallback {
		private final Function<Boolean, P> generator;
		private final Class<? extends ChaosEffect> clazz;
		
		PacketValidationHandler(Function<Boolean, P> generator, Class<? extends ChaosEffect> clazz) {
			this.generator = generator;
			this.clazz = clazz;
		}
		
		@Override
		public void tick(PlayerEntity player) {
			//If the player saves and quits while the ChaosEffect is active and then joins a different world where the ChaosEffect is inactive
			//They will be "tracked" but will not have the effect active, so send a packet to disable the client side effects and stop tracking them.
			if(player.world.isClient) {
				return;
			}
			boolean tracked = TRACKERS.get(this.clazz).tracked(player);
			boolean active = MCCEAPI.accessor.isChaosEffectActive(player, this.clazz);
			if(tracked && !active) {
				this.createAndSendPacket((ServerPlayerEntity) player, false);
				TRACKERS.get(this.clazz).remove(player);
			}
			if(!tracked && active) {
				this.createAndSendPacket((ServerPlayerEntity) player, true);
				TRACKERS.get(this.clazz).add(player);
			}
		}

		@Override
		public int priority() {
			return -1;
		}
		
		void createAndSendPacket(ServerPlayerEntity player, boolean status) {
			this.generator.apply(status).sendPacket(player);
		}
	}
}
