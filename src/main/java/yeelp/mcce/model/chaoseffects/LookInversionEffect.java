package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.LookInversionStatusPayload;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class LookInversionEffect extends ClientPlayerTrackingChaosEffect<LookInversionStatusPayload> {
    private static final int DURATION_MIN = 1200, DURATION_MAX = 1800;

    private boolean silent = false;

    public LookInversionEffect() {
        super(DURATION_MIN, DURATION_MAX, LookInversionStatusPayload::new);
    }

    LookInversionEffect(int duration) {
        super(duration, LookInversionStatusPayload::new);
        this.silent = true;
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
    public void applyEffect(PlayerEntity player) {
        super.applyEffect(player);
        if(!this.silent) {
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.INVERSE_START, 1.0f, 1.0f)::send);
        }
    }

    @Override
    public String getName() {
        return "lookinversion";
    }

    @Override
    public String getDisplayName() {
        return "Look Inversion";
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        super.onEffectEnd(player);
        if(!this.silent) {
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.INVERSE_END, 1.0f, 1.0f)::send);
        }
    }

    public static double changeInput(Entity entity, double input) {
        return entity instanceof PlayerEntity player && ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.LOOK_INVERSION) ? input * -1 : input;
    }

    public static boolean isAffected(PlayerEntity player) {
        return StatusPayloadSendingChaosEffect.getTracker(ChaosEffects.LOOK_INVERSION).tracked(player);
    }

    public static void trackClient(PlayerEntity player, StatusPayload payload) {
        ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.LOOK_INVERSION, payload);
    }

    @Override
    protected ResetEffectHandler getHandler() {
        return new LookInversionTickHandler();
    }

    private static final class LookInversionTickHandler extends ResetEffectHandler {

        @Override
        protected ChaosEffectRegistryEntry getRegistryEntry() {
            return ChaosEffects.LOOK_INVERSION;
        }

        @Override
        protected boolean isAffected(PlayerEntity player) {
            return LookInversionEffect.isAffected(player);
        }

        @Override
        protected ChaosEffect createDummyChaosEffectWithDurationOne() {
            return new LookInversionEffect(1);
        }
    }
}
