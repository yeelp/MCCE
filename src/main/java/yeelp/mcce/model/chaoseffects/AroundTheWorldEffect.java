package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.AroundTheWorldStatusPayload;

public final class AroundTheWorldEffect extends StatusPayloadSendingChaosEffect<AroundTheWorldStatusPayload> {

    private static final int DURATION_MIN = 800, DURATION_MAX = 1600;
    public AroundTheWorldEffect() {
        super(DURATION_MIN, DURATION_MAX, AroundTheWorldStatusPayload::new);
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //nothing
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
        return "aroundtheworld";
    }

    @Override
    public String getDisplayName() {
        return "Around the World";
    }
}
