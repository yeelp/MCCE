package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.api.MCCEAPI;

public final class RepeatingEffect extends AbstractIntervalTriggeredChaosEffect {

	private String effect;
	private static final String EFFECT_KEY = "effect";

	private static final int DURATION = 1000, INTERVAL_MIN = 10, INTERVAL_MAX = 60;

	private static final ChaosEffect DUMMY = new RepeatingEffect(true);

	public static ChaosEffect getDummyInstance() {
		return DUMMY;
	}

	public RepeatingEffect() {
		super(DURATION, DURATION, INTERVAL_MIN, INTERVAL_MAX, AbstractLastingChaosEffect.getIntInRange(3, 5));
		this.effect = ChaosEffectRegistry.getEffectValidForRepeating();
	}

	private RepeatingEffect(boolean ignored) {
		super(DURATION, DURATION, INTERVAL_MIN, INTERVAL_MAX, 2);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(this.getTriggersRemaining() > 0) {
			this.trigger();
			MCCEAPI.mutator.addNewChaosEffect(player, ChaosEffectRegistry.getEffect(this.effect));
		}
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putString(EFFECT_KEY, this.effect);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.effect = nbt.getString(EFFECT_KEY);
	}

	@Override
	public String getName() {
		return "repeating";
	}

	@Override
	public void registerCallbacks() {
		//no callbacks
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean canModifyEffectState() {
		return true;
	}
}
