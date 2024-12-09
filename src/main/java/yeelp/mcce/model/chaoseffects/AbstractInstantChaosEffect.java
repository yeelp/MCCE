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
		//no NBT to read
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
		//No on end effects
	}

	@Override
	public void tickEffect(PlayerEntity player) {
		//Effect can't be ticked
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	public final boolean canModifyEffectState() {
		return false;
	}

}
