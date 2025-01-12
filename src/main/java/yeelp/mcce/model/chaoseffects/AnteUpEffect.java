package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class AnteUpEffect extends SimpleTimedChaosEffect {

    private static final int DURATION_MIN = 3600, DURATION_MAX = 5600;
    private static final float TRIPLE_CHANCE = 0.4f;
    private static final float APPLY_CHANCE = 0.8f;

    public AnteUpEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }
    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return Math.random() < APPLY_CHANCE;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        MCCEAPI.mutator.modifyEffectState(player, (pces) -> {
            if(pces.getDurationUntilNextEffect() <= 5) {
                pces.resetDurationUntilNextEffect();
                pces.setDurationUntilNextEffect(pces.getDurationUntilNextEffect()/2);
                pces.addNewEffect(player, (this.getRNG().nextFloat() < TRIPLE_CHANCE ? ChaosEffects.TRIPLE_THREAT : ChaosEffects.DOUBLE_TROUBLE).createChaosEffect());
            }
        });
    }

    @Override
    public String getName() {
        return "anteup";
    }

    @Override
    public boolean canBeFirstEffect() {
        return false;
    }

    @Override
    public boolean canModifyEffectState() {
        return true;
    }
}
