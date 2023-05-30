package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class WitherEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 900));
	}

	@Override
	public String getName() {
		return "wither";
	}

	@Override
	public boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !player.getActiveStatusEffects().containsKey(StatusEffects.WITHER) && !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

}
