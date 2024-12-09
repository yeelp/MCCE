package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

import java.util.Map;
import java.util.function.Function;

public abstract class StatusPayloadSendingChaosEffect<P extends StatusPayload> extends AbstractTimedChaosEffect {

    private static final Map<ChaosEffectRegistryEntry, PayloadValidationHandler<? extends StatusPayload>> HANDLERS = Maps.newHashMap();
    private static final Map<Class<? extends ChaosEffect>, Function<Boolean, ? extends StatusPayload>> GENERATORS = Maps.newHashMap();
    private static final Map<ChaosEffectRegistryEntry, Tracker> TRACKERS = Maps.newHashMap();

    protected StatusPayloadSendingChaosEffect(int durationMin, int durationMax, Function<Boolean, P> payloadGenerator) {
        super(durationMin, durationMax);
        GENERATORS.put(this.getClass(), payloadGenerator);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        this.checkAndSendPacket(player, true);
        this.getTracker().add(player);
    }

    @Override
    public void registerCallbacks() {
        PlayerTickCallback.EVENT.register(this.getHandler());
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        this.checkAndSendPacket(player, false);
        this.getTracker().remove(player);
    }

    private void checkAndSendPacket(PlayerEntity player, boolean status) {
        if (player instanceof ServerPlayerEntity) {
            this.getHandler().createAndSendPayload((ServerPlayerEntity) player, status);
        }
    }

    private Tracker getTracker() {
        return getTracker(ChaosEffectRegistry.getEntry(this));
    }

    protected static Tracker getTracker(ChaosEffectRegistryEntry entry) {
        return TRACKERS.computeIfAbsent(entry, (e) -> new Tracker());
    }

    private PayloadValidationHandler<? extends StatusPayload> getHandler() {
        return HANDLERS.computeIfAbsent(ChaosEffectRegistry.getEntry(this), (e) -> new PayloadValidationHandler<>(GENERATORS.get(this.getClass()), e));
    }

    private record PayloadValidationHandler<P extends StatusPayload>(Function<Boolean, P> generator,
                                                                     ChaosEffectRegistryEntry entry) implements PlayerTickCallback {

        @Override
        public void tick(PlayerEntity player) {
            //If the player saves and quits while the ChaosEffect is active and then joins a different world where the ChaosEffect is inactive
            //They will be "tracked" but will not have the effect active, so send a packet to disable the client side effects and stop tracking them.
            if (PlayerUtils.isPlayerWorldClient(player)) {
                return;
            }
            Tracker tracker;
            boolean tracked = (tracker = this.getTracker()).tracked(player);
            boolean active = MCCEAPI.accessor.isChaosEffectActive(player, this.entry());
            if (tracked && !active) {
                this.createAndSendPayload((ServerPlayerEntity) player, false);
                tracker.remove(player);
            }
            if (!tracked && active) {
                this.createAndSendPayload((ServerPlayerEntity) player, true);
                tracker.add(player);
            }
        }

        @Override
        public int priority() {
            return -1;
        }

        private Tracker getTracker() {
            return StatusPayloadSendingChaosEffect.getTracker(this.entry());
        }

        void createAndSendPayload(ServerPlayerEntity player, boolean status) {
            this.generator.apply(status).send(player);
        }
    }
}
