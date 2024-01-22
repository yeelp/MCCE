package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.util.ChaosLib;

public final class MobVisionEffect extends SimpleTimedChaosEffect {

	public MobVisionEffect() {
		super(1200, 1800);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 16), (entity) -> entity != player).forEach((entity) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20)));
	}

	@Override
	public String getName() {
		return "mobvision";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
