package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.FlippingOutStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class FlippingOutEffect extends ClientPlayerTrackingChaosEffect<FlippingOutStatusPayload> {

    private static final int DURATION_MIN = 1200, DURATION_MAX = 1800;

    public FlippingOutEffect() {
        super(DURATION_MIN, DURATION_MAX, FlippingOutStatusPayload::new);
    }

    FlippingOutEffect(int duration) {
        super(duration, FlippingOutStatusPayload::new);
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new FlippingOutTickHandler();
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public String getName() {
        return "flippingout";
    }

    @Override
    public String getDisplayName() {
        return "Flipping Out";
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.FLIPPING_OUT);
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.FLIPPING_OUT);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.FLIPPING_OUT, payload);
    }

    private static final class FlippingOutTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.FLIPPING_OUT;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return FlippingOutEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new FlippingOutEffect(1);
        }
    }
}
