package yeelp.mcce.model.chaoseffects;

import net.minecraft.nbt.NbtCompound;

public abstract class AbstractTimedChaosEffect extends AbstractLastingChaosEffect {

	private static final String DURATION_KEY = "duration";
	protected AbstractTimedChaosEffect(int durationMin, int durationMax) {
		super((int) ((durationMax - durationMin) * Math.random()) + durationMin + 1);
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
