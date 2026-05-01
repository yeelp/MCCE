package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.SpinToWinStatusPayload;

public final class SpinToWinEffect extends ClientPlayerTrackingChaosEffect<SpinToWinStatusPayload> {

    private static final int DURATION_MIN = 1200, DURATION_MAX = 1600;

    public SpinToWinEffect() {
        super(DURATION_MIN, DURATION_MAX, SpinToWinStatusPayload::new);
    }

    SpinToWinEffect(int duration) {
        super(duration, duration, SpinToWinStatusPayload::new);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MEMORY_GAME);
    }


    @Override
    protected ResetEffectHandler getHandler() {
        return new SpinToWinTickHandler();
    }

    @Override
    public String getName() {
        return "spintowin";
    }

    @Override
    public String getDisplayName() {
        return "Spin to Win";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.SPIN_TO_WIN);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.SPIN_TO_WIN, payload);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.SPIN_TO_WIN);
    }

    private static final class SpinToWinTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.SPIN_TO_WIN;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return SpinToWinEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new SpinToWinEffect(1);
        }
    }
}
