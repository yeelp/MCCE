package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class FoodChainEffect extends AbstractStatCycleEffect {

    private static final float HUNGER_MAX = 20.0f;
    private static final ChaosEffectRegistryEntry[] MUTUALLY_EXCLUSIVE_EFFECTS = new ChaosEffectRegistryEntry[] {ChaosEffects.EQUILIBRIUM};

    @Override
    protected float getBound(PlayerEntity player) {
        return HUNGER_MAX;
    }

    @Override
    protected void alterStat(PlayerEntity player, float value) {
        player.getHungerManager().setFoodLevel((int) value);
        player.getHungerManager().setSaturationLevel(value);
    }

    @Override
    public String getName() {
        return "foodchain";
    }

    @Override
    protected ChaosEffectRegistryEntry[] getMutualExclusiveEffects() {
        return MUTUALLY_EXCLUSIVE_EFFECTS;
    }
}
