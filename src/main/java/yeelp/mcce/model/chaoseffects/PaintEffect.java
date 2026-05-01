package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.ModConfig;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.PaintStatusPayload;

public final class PaintEffect extends ClientPlayerTrackingChaosEffect<PaintStatusPayload> implements OptionalEffect {

    private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
    public PaintEffect() {
        super(DURATION_MIN, DURATION_MAX, PaintStatusPayload::new);
    }

    PaintEffect(int duration) {
        super(duration, PaintStatusPayload::new);
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new PaintTickHandler();
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.MOB_VISION, ChaosEffects.COLOUR_INVERSION, ChaosEffects.BAKE, ChaosEffects.RAINBOW);
    }

    @Override
    public String getName() {
        return "paint";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.PAINT);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.PAINT);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.PAINT, payload);
    }

    @Override
    public boolean enabled() {
        return ModConfig.getInstance().game.paint;
    }

    private static final class PaintTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.PAINT;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return PaintEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new PaintEffect(1);
        }
    }
}
