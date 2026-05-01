package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.SepiaStatusPayload;

public final class SepiaEffect extends ShaderChaosEffect<SepiaStatusPayload> {

    public static final int DURATION_MIN = 1500, DURATION_MAX = 1700;

    public SepiaEffect() {
        super(DURATION_MIN, DURATION_MAX, SepiaStatusPayload::new);
    }

    SepiaEffect(int duration) {
        super(duration, SepiaStatusPayload::new);
    }

    @Override
    protected ChaosEffectRegistryEntry[] getSpecificExclusions() {
        return new ChaosEffectRegistryEntry[0];
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new SepiaTickHandler();
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    public String getName() {
        return "sepia";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.SEPIA);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.SEPIA);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.SEPIA, payload);
    }

    private static final class SepiaTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.SEPIA;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return SepiaEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new SepiaEffect(1);
        }
    }
}
