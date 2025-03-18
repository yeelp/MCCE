package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

import java.util.Map;
import java.util.function.Function;

public abstract class ClientPlayerTrackingChaosEffect<P extends StatusPayload> extends StatusPayloadSendingChaosEffect<P> {

    private static final Map<ChaosEffectRegistryEntry, Tracker> TRACKED_CLIENT = Maps.newHashMap();

    protected ClientPlayerTrackingChaosEffect(int durationMin, int durationMax, Function<Boolean, P> payloadGenerator) {
        super(durationMin, durationMax, payloadGenerator);

    }

    protected ClientPlayerTrackingChaosEffect(int duration, Function<Boolean, P> payloadGenerator) {
        super(duration, duration, payloadGenerator);
    }

    @Override
    public void registerCallbacks() {
        super.registerCallbacks();
        PlayerTickCallback.EVENT.register(this.getHandler());
    }

    public static Tracker getClientTracker(ChaosEffectRegistryEntry entry) {
        return TRACKED_CLIENT.computeIfAbsent(entry, (effect) -> new Tracker());
    }

    protected static void trackClient(PlayerEntity player, ChaosEffectRegistryEntry entry, StatusPayload payload) {
        Tracker clientTracker = getClientTracker(entry);
        if(payload.status()) {
            clientTracker.add(player);
        }
        else {
            clientTracker.remove(player);
        }
    }

    protected static boolean isClientTracked(PlayerEntity player, ChaosEffectRegistryEntry entry) {
        return getClientTracker(entry).tracked(player);
    }

    protected static boolean isAffected(PlayerEntity player, ChaosEffectRegistryEntry entry) {
        return StatusPayloadSendingChaosEffect.getTracker(entry).tracked(player);
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        Tracker tracker = this.getTracker();
        if(!tracker.tracked(player)) {
            tracker.add(player);
        }
    }

    protected abstract ResetEffectHandler getHandler();

    protected static abstract class ResetEffectHandler implements PlayerTickCallback {

        @Override
        public void tick(PlayerEntity player) {
            if(this.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, this.getRegistryEntry())) {
                MCCEAPI.mutator.addNewChaosEffect(player, this.createDummyChaosEffectWithDurationOne());
            }
        }

        protected abstract ChaosEffectRegistryEntry getRegistryEntry();

        protected abstract boolean isAffected(PlayerEntity player);

        protected abstract ChaosEffect createDummyChaosEffectWithDurationOne();
    }
}
