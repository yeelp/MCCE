package yeelp.mcce.model.chaoseffects;

import net.minecraft.nbt.NbtCompound;

public abstract class AbstractTimedChaosEffect extends AbstractLastingChaosEffect {

	protected AbstractTimedChaosEffect(int durationMin, int durationMax) {
		super(AbstractLastingChaosEffect.getIntInRange(durationMin, durationMax));
	}

	@Override
	public int getDurationUntilNextActivation() {
		return 0;
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt(DURATION_KEY, this.durationRemaining());
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.setDuration(nbt.getInt(DURATION_KEY));
	}

}
