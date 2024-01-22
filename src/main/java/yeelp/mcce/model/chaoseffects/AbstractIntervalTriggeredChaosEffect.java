package yeelp.mcce.model.chaoseffects;

import net.minecraft.nbt.NbtCompound;

public abstract class AbstractIntervalTriggeredChaosEffect extends AbstractIntervalChaosEffect {

	private int triggers;

	protected AbstractIntervalTriggeredChaosEffect(int durationMin, int durationMax, int intervalMin, int intervalMax, int triggers) {
		super(durationMin, durationMax, intervalMin, intervalMax);
		this.triggers = triggers;
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putInt(AbstractTriggeredChaosEffect.TRIGGERS_KEY, this.getTriggersRemaining());
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.triggers = nbt.getInt(AbstractTriggeredChaosEffect.TRIGGERS_KEY);
	}

	protected final int getTriggersRemaining() {
		return this.triggers;
	}

	protected final int trigger() {
		return --this.triggers;
	}

}
