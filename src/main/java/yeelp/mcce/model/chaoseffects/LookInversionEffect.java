package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.LookInversionStatusPayload;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

public final class LookInversionEffect extends StatusPayloadSendingChaosEffect<LookInversionStatusPayload> {
    private static final int DURATION_MIN = 1200, DURATION_MAX = 1800;
    private static final Tracker TRACKED_CLIENT = new Tracker();

    private boolean silent = false;

    public LookInversionEffect() {
        super(DURATION_MIN, DURATION_MAX, LookInversionStatusPayload::new);
    }

    LookInversionEffect(int duration) {
        super(duration, duration, LookInversionStatusPayload::new);
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
    public void registerCallbacks() {
        super.registerCallbacks();
        PlayerTickCallback.EVENT.register(new LookInversionTickHandler());
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        super.onEffectEnd(player);
        if(!this.silent) {
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.INVERSE_END, 1.0f, 1.0f)::send);
        }
    }

    public static double changeInput(Entity entity, double input) {
        return entity instanceof PlayerEntity player && TRACKED_CLIENT.tracked(player) ? input * -1 : input;
    }

    public static boolean isAffected(PlayerEntity player) {
        return StatusPayloadSendingChaosEffect.getTracker(ChaosEffects.LOOK_INVERSION).tracked(player);
    }

    public static void trackClient(PlayerEntity player, LookInversionStatusPayload payload) {
        if(payload.status()) {
            TRACKED_CLIENT.add(player);
        }
        else {
            TRACKED_CLIENT.remove(player);
        }
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        Tracker tracker = this.getTracker();
        if(tracker.tracked(player)) {
            return;
        }
        tracker.add(player);
    }

    private static final class LookInversionTickHandler implements PlayerTickCallback {

        @Override
        public void tick(PlayerEntity player) {
            if(LookInversionEffect.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.LOOK_INVERSION)) {
                MCCEAPI.mutator.addNewChaosEffect(player, new LookInversionEffect(1));
            }
        }
    }
}
