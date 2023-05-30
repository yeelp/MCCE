package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class NullEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		return;
	}

	@Override
	public String getName() {
		return "null";
	}

	@Override
	public boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
