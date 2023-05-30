package yeelp.mcce.model.chaoseffects;

import org.joml.Math;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public final class SwitcherooEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		float health = player.getHealth();
		float hunger = player.getHungerManager().getFoodLevel();
		player.setHealth(hunger);
		player.getHungerManager().setSaturationLevel(Math.min(player.getHungerManager().getSaturationLevel(), Math.min(health, hunger)));
		player.getHungerManager().setFoodLevel((int) health);
	}

	@Override
	public String getName() {
		return "switcheroo";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return (int) player.getHealth() != player.getHungerManager().getFoodLevel() && player.getHungerManager().getFoodLevel() > 0 && MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

}
