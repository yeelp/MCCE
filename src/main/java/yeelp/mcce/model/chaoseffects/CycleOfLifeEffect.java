package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class CycleOfLifeEffect extends AbstractStatCycleEffect {

    private static final ChaosEffectRegistryEntry[] MUTUALLY_EXCLUSIVE_EFFECTS = new ChaosEffectRegistryEntry[] {ChaosEffects.SUDDEN_DEATH, ChaosEffects.EQUILIBRIUM};

    public CycleOfLifeEffect() {
        super();
    }

    @Override
    protected float getBound(PlayerEntity player) {
        return player.getMaxHealth();
    }

    @Override
    protected void alterStat(PlayerEntity player, float value) {
        player.setHealth(value);
    }

    @Override
    public String getName() {
        return "cycleoflife";
    }

    @Override
    protected ChaosEffectRegistryEntry[] getMutualExclusiveEffects() {
        return MUTUALLY_EXCLUSIVE_EFFECTS;
    }
}
