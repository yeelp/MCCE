package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.api.MCCEAPI;

public class RepeatingEffect extends AbstractIntervalTriggeredChaosEffect {

	private String effect;
	private static final String EFFECT_KEY = "effect";

	public RepeatingEffect() {
		super(1000, 1000, 10, 60, AbstractLastingChaosEffect.getIntInRange(3, 5));
		this.effect = ChaosEffectRegistry.getEffectValidForRepeating();
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
		return;
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
