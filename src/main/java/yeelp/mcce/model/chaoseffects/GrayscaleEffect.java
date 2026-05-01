package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.GrayscaleStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class GrayscaleEffect extends ShaderChaosEffect<GrayscaleStatusPayload> {

    private static final int DURATION_MIN = 1000, DURATION_MAX = 2000;

    public GrayscaleEffect() {
        super(DURATION_MIN, DURATION_MAX, GrayscaleStatusPayload::new);
    }

    GrayscaleEffect(int duration) {
        super(duration, GrayscaleStatusPayload::new);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected ChaosEffectRegistryEntry[] getSpecificExclusions() {
        return new ChaosEffectRegistryEntry[] {ChaosEffects.MEMORY_GAME, ChaosEffects.RAINBOW};
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new GrayscaleTickHandler();
    }

    @Override
    public String getName() {
        return "grayscale";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.GRAYSCALE);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.GRAYSCALE);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.GRAYSCALE, payload);
    }

    private static final class GrayscaleTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.GRAYSCALE;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return GrayscaleEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new GrayscaleEffect(1);
        }
    }
}
