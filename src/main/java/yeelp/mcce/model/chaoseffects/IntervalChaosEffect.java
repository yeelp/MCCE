package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public abstract class IntervalChaosEffect extends AbstractLastingChaosEffect {

	private int interval, intervalMin, intervalMax;
	private static final String INTERVAL_KEY = "interval", INTERVAL_MIN_KEY = "intervalMin", INTERVAL_MAX_KEY = "intervalMax";
	protected IntervalChaosEffect(int durationMin, int durationMax, int intervalMin, int intervalMax) {
		super(AbstractLastingChaosEffect.getIntInRange(durationMin, durationMax));
		this.intervalMin = intervalMin;
		this.intervalMax = intervalMax;
		this.interval = AbstractLastingChaosEffect.getIntInRange(intervalMin, intervalMax);
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt(DURATION_KEY, this.durationRemaining());
		nbt.putInt(INTERVAL_KEY, this.getDurationUntilNextActivation());
		nbt.putInt(INTERVAL_MIN_KEY, this.intervalMin);
		nbt.putInt(INTERVAL_MAX_KEY, this.intervalMax);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.setDuration(nbt.getInt(DURATION_KEY));
		this.interval = nbt.getInt(INTERVAL_KEY);
		this.intervalMin = nbt.getInt(INTERVAL_MIN_KEY);
		this.intervalMax = nbt.getInt(INTERVAL_MAX_KEY);
	}

	@Override
	public int getDurationUntilNextActivation() {
		return this.interval;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		return;
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		if(--this.interval <= 0) {
			this.applyEffect(player);
			this.interval = AbstractLastingChaosEffect.getIntInRange(this.intervalMin, this.intervalMax);
		}
	}

}
