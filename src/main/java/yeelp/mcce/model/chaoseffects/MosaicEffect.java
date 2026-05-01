package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.MosaicStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class MosaicEffect extends ShaderChaosEffect<MosaicStatusPayload> {

    private static final int DURATION_MIN = 1400, DURATION_MAX = 1700;

    public MosaicEffect() {
        super(DURATION_MIN, DURATION_MAX, MosaicStatusPayload::new);
    }

    MosaicEffect(int duration) {
        super(duration, MosaicStatusPayload::new);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected ChaosEffectRegistryEntry[] getSpecificExclusions() {
        return new ChaosEffectRegistryEntry[0];
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new MosaicTickHandler();
    }

    @Override
    public String getName() {
        return "mosaic";
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.MOSAIC);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.MOSAIC, payload);
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.MOSAIC);
    }

    private static final class MosaicTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.MOSAIC;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return MosaicEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new MosaicEffect(1);
        }
    }
}
