package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import yeelp.mcce.api.MCCEAPI;

public final class HelpOrHinderEffect extends AbstractInstantChaosEffect {
    private static final int INSTANT_DAMAGE_MIN = 6;

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SUDDEN_DEATH, ChaosEffects.CYCLE_OF_LIFE);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        RegistryEntry<StatusEffect> choice;
        if((int) player.getHealth() == (int) player.getMaxHealth()) {
            choice = StatusEffects.INSTANT_DAMAGE;
        }
        else if(player.getHealth() <= INSTANT_DAMAGE_MIN) {
            choice = StatusEffects.INSTANT_HEALTH;
        }
        else {
            choice = this.getRNG().nextBoolean() ? StatusEffects.INSTANT_HEALTH : StatusEffects.INSTANT_DAMAGE;
        }
        int amp = Math.min(choice == StatusEffects.INSTANT_DAMAGE ? (int) player.getHealth() / INSTANT_DAMAGE_MIN - 1 : 3, this.getRNG().nextInt(3));
        player.addStatusEffect(new StatusEffectInstance(choice, 1, amp));
    }

    @Override
    public String getName() {
        return "helporhinder";
    }

    @Override
    public String getDisplayName() {
        return "Help or Hinder";
    }
}
