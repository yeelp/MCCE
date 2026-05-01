package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.RotateStatusPayload;

public final class RotateEffect extends ClientPlayerTrackingChaosEffect<RotateStatusPayload> {

    private static final int DURATION_MIN = 800, DURATION_MAX = 1200;

    public RotateEffect() {
        super(DURATION_MIN, DURATION_MAX, RotateStatusPayload::new);
    }

    RotateEffect(int duration) {
        super(duration, duration, RotateStatusPayload::new);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public String getName() {
        return "rotate";
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new RotateTickHandler();
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.ROTATE);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.ROTATE, payload);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.ROTATE);
    }

    private static final class RotateTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.ROTATE;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return RotateEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new RotateEffect(1);
        }
    }
}
