package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public abstract class AbstractInstantChaosEffect extends AbstractChaosEffect {

	@Override
	public final boolean isInstant() {
		return true;
	}

	@Override
	public final void registerCallbacks() {
		//no callbacks for instant effects
	}

	@Override
	public final NbtCompound writeToNbt() {
		return null;
	}

	@Override
	public final void readNbt(NbtCompound nbt) {
		return;
	}

	@Override
	public int durationRemaining() {
		return 0;
	}

	@Override
	public int getDurationUntilNextActivation() {
		return 0;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		return;
	}

	@Override
	public void tickEffect(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}
	
}
