package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public final class MobVisionEffect extends SimpleTimedChaosEffect {

	public MobVisionEffect() {
		super(1200, 1800);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getWorld().getEntitiesByClass(LivingEntity.class, new Box(player.getBlockPos().east(16).north(16).up(16), player.getBlockPos().west(16).south(16).down(16)), (entity) -> entity != player).forEach((entity) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20)));
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
