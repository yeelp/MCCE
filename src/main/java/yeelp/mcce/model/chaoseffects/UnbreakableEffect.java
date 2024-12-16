package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.BeforePlayerBreakBlockCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;

public final class UnbreakableEffect extends AbstractTriggeredChaosEffect implements BeforePlayerBreakBlockCallback {

    private static final int DURATION_MIN = 2000, DURATION_MAX = 3000, TRIGGERS_MIN = 3, TRIGGERS_MAX = 5;

    public UnbreakableEffect() {
        super(DURATION_MIN, DURATION_MAX, TRIGGERS_MIN, TRIGGERS_MAX);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.CRUMBLE, ChaosEffects.COLUMN_LIKE_YOU_SEE_EM, ChaosEffects.CHUNKY) && MCCEAPI.accessor.getChaosEffect(player, LotteryEffect.class).filter((e) -> e.getTriggersRemaining() > 0).isEmpty();
    }

    @Override
    public CallbackResult beforeBlockBreak(BlockPos pos, PlayerEntity breakingPlayer) {
        return MCCEAPI.accessor.getChaosEffect(breakingPlayer, this.getClass()).filter((e) -> e.getTriggersRemaining() > 0).map((e) -> {
            e.trigger();
            return new CallbackResult(CancelState.CANCEL);
        }).orElse(new CallbackResult());
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //no additional effect logic
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        //no effect application
    }

    @Override
    public String getName() {
        return "unbreakable";
    }

    @Override
    public void registerCallbacks() {
        BeforePlayerBreakBlockCallback.EVENT.register(this);
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        //no on end effects
    }
}
