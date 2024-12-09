package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.util.ChaosLib;

public final class MobVisionEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1200, DURATION_MAX = 1800;
	private static final int RADIUS = 16;
	private static final int EFFECT_DURATION = 20;
	public MobVisionEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> entity != player).forEach((entity) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, EFFECT_DURATION)));
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
