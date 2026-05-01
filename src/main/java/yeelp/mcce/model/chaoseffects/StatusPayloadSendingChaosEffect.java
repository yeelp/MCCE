package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.event.ClientSideSyncHandler;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
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

    protected Tracker getTracker() {
        return getTracker(ChaosEffectRegistry.getEntry(this));
    }

    protected static Tracker getTracker(ChaosEffectRegistryEntry entry) {
        return TRACKERS.computeIfAbsent(entry, (e) -> new Tracker());
    }

    private PayloadValidationHandler<? extends StatusPayload> getHandler() {
        return HANDLERS.computeIfAbsent(ChaosEffectRegistry.getEntry(this), (e) -> new PayloadValidationHandler<>(GENERATORS.get(this.getClass()), e));
    }

    private static final class PayloadValidationHandler<P extends StatusPayload> extends ClientSideSyncHandler {

        private final Function<Boolean, P> generator;

        PayloadValidationHandler(Function<Boolean, P> generator, ChaosEffectRegistryEntry entry) {
            super(entry);
            this.generator = generator;
        }

        @Override
        protected Tracker getTracker() {
            return StatusPayloadSendingChaosEffect.getTracker(this.entry);
        }

        @Override
        protected ChaosPayload getPayload(PlayerEntity player, boolean isBeingRemoved) {
            return this.generator.apply(!isBeingRemoved);
        }

        void createAndSendPayload(ServerPlayerEntity player, boolean status) {
            this.getPayload(player, !status).send(player);
        }
    }
}
