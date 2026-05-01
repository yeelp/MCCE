package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.BakeStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class BakeEffect extends ShaderChaosEffect<BakeStatusPayload> {

    private static final int DURATION_MIN = 1500, DURATION_MAX = 1800;
    public BakeEffect() {
        super(DURATION_MIN, DURATION_MAX, BakeStatusPayload::new);
    }

    BakeEffect(int duration) {
        super(duration, BakeStatusPayload::new);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected ChaosEffectRegistryEntry[] getSpecificExclusions() {
        return new ChaosEffectRegistryEntry[0];
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new GlintedTickHandler();
    }

    @Override
    public String getName() {
        return "bake";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.BAKE);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.BAKE);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.BAKE, payload);
    }

    private static final class GlintedTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.BAKE;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return BakeEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new BakeEffect(1);
        }
    }
}
