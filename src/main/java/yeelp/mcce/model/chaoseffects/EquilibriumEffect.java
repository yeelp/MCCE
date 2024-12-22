package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class EquilibriumEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1500, DURATION_MAX = 2000;
	public EquilibriumEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		float health = player.getHealth() + player.getAbsorptionAmount();
		float threshold = player.getMaxHealth()/2;
		int hunger = player.getHungerManager().getFoodLevel();
		boolean hasSat = player.getHungerManager().getSaturationLevel() > 0;
		if(!player.hasStatusEffect(StatusEffects.POISON) && health > threshold) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, this.durationRemaining(), 1));
			player.removeStatusEffect(StatusEffects.REGENERATION);
		}
		if(!player.hasStatusEffect(StatusEffects.REGENERATION) && health < threshold) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, this.durationRemaining(), 1));
			player.removeStatusEffect(StatusEffects.POISON);
		}
		if(!player.hasStatusEffect(StatusEffects.HUNGER) && (hasSat || hunger > 10)) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, this.durationRemaining(), 7));
			player.removeStatusEffect(StatusEffects.SATURATION);
		}
		if(!player.hasStatusEffect(StatusEffects.SATURATION) && (!hasSat && hunger < 10)) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, this.durationRemaining()));
			player.removeStatusEffect(StatusEffects.HUNGER);
		}
	}

	@Override
	public String getName() {
		return "equilibrium";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SUDDEN_DEATH, ChaosEffects.CYCLE_OF_LIFE, ChaosEffects.FOOD_CHAIN);
	}

}
