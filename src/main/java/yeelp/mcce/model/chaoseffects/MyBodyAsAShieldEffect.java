package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class MyBodyAsAShieldEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setAbsorptionAmount(this.getRNG().nextFloat(1.0f, 20.0f));
	}

	@Override
	public String getName() {
		return "mybodyasashield";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getAbsorptionAmount() == 0;
	}

}
