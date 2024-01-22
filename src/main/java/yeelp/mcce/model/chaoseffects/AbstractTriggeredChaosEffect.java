package yeelp.mcce.model.chaoseffects;

import net.minecraft.nbt.NbtCompound;

public abstract class AbstractTriggeredChaosEffect extends AbstractTimedChaosEffect {

	private int triggers;
	static final String TRIGGERS_KEY = "triggers";
	
	protected AbstractTriggeredChaosEffect(int durationMin, int durationMax, int triggersMin, int triggersMax) {
		super(durationMin, durationMax);
		this.triggers = this.getRNG().nextInt(triggersMin, triggersMax + 1);
	}
	
	protected AbstractTriggeredChaosEffect(int durationMin, int durationMax, int triggers) {
		this(durationMin, durationMax, triggers, triggers);
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putInt(TRIGGERS_KEY, this.getTriggersRemaining());
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.triggers = nbt.getInt(TRIGGERS_KEY);
	}

	protected final int getTriggersRemaining() {
		return this.triggers;
	}
	
	protected final int trigger() {
		return --this.triggers;
	}
}
