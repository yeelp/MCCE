package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class GaspEffect extends AbstractStatCycleEffect {

    @Override
    protected void alterStat(PlayerEntity player, float value) {
        player.setAir((int) value);
    }

    @Override
    protected float getBound(PlayerEntity player) {
        return player.getMaxAir();
    }

    @Override
    protected ChaosEffectRegistryEntry[] getMutualExclusiveEffects() {
        return new ChaosEffectRegistryEntry[0];
    }

    @Override
    public String getName() {
        return "gasp";
    }
}
