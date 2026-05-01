package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.GammegaStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class GammegaEffect extends ClientPlayerTrackingChaosEffect<GammegaStatusPayload> {

    private static final int DURATION_MIN = 1300, DURATION_MAX = 1800;

    public GammegaEffect() {
        super(DURATION_MIN, DURATION_MAX, GammegaStatusPayload::new);
    }

    GammegaEffect(int duration) {
        super(duration, GammegaStatusPayload::new);
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new ResetEffectHandler() {
            @Override
            protected ChaosEffectRegistryEntry getRegistryEntry() {
                return ChaosEffects.GAMMEGA;
            }

            @Override
            protected boolean isAffected(PlayerEntity player) {
                return GammegaEffect.isAffected(player);
            }

            @Override
            protected ChaosEffect createDummyChaosEffectWithDurationOne() {
                return new GammegaEffect(1);
            }
        };
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
        return "gammega";
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.GAMMEGA, payload);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.GAMMEGA);
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.GAMMEGA);
    }
}
