package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

public final class NullEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		//this does nothing
	}

	@Override
	public String getName() {
		return "null";
	}

	@Override
	public boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean canBeFirstEffect() {
		//can't be first since that defeats the whole point of having some effects that can't be applied first
		return false;
	}
}
