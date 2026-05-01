package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.ColourInversionStatusPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public final class ColourInversionEffect extends ShaderChaosEffect<ColourInversionStatusPayload> {

    public static final int DURATION_MIN = 1600, DURATION_MAX = 2000;

    public ColourInversionEffect() {
        super(DURATION_MIN, DURATION_MAX, ColourInversionStatusPayload::new);
    }

    ColourInversionEffect(int duration) {
        super(duration, ColourInversionStatusPayload::new);
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
        return new ColourInversionTickHandler();
    }

    @Override
    public String getName() {
        return "colourinversion";
    }

    @Override
    public String getDisplayName() {
        return "Colour Inversion";
    }

    public static boolean isAffected(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.COLOUR_INVERSION);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.COLOUR_INVERSION, payload);
    }

    public static boolean isClientTracked(PlayerEntity player) {
        return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.COLOUR_INVERSION);
    }

    private static final class ColourInversionTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.COLOUR_INVERSION;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return ColourInversionEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new ColourInversionEffect(1);
        }
    }
}
