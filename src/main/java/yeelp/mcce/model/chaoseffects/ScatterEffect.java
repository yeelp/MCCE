package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.ScatterStatusPayload;

public final class ScatterEffect extends ClientPlayerTrackingChaosEffect<ScatterStatusPayload> {
    private static final int DURATION_MIN = 700, DURATION_MAX = 1100;

    public ScatterEffect() {
        super(DURATION_MIN, DURATION_MAX, ScatterStatusPayload::new);
    }

    ScatterEffect(int duration) {
        super(duration, ScatterStatusPayload::new);
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //nothing
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new ScatterTickHandler();
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
    public String getName() {
        return "scatter";
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.SCATTER, payload);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.SCATTER);
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.SCATTER);
    }

    private static final class ScatterTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.SCATTER;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return ScatterEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new ScatterEffect(1);
        }
    }
}
