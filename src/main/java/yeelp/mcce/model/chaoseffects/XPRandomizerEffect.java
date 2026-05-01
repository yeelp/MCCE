package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.ModConfig;

public final class XPRandomizerEffect extends SimpleTimedChaosEffect implements OptionalEffect {

	private static final int DURATION_MIN = 2000, DURATION_MAX = 5000;
	public XPRandomizerEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		int score = player.getScore();
		player.addExperience(Integer.MIN_VALUE);
		player.addExperience(Math.abs(this.getRNG().nextInt()));
		player.setScore(score);
	}

	@Override
	public String getName() {
		return "xprandomizer";
	}

	@Override
	public String getDisplayName() {
		return "XP Randomizer";
	}

	@Override
	public boolean enabled() {
		return ModConfig.getInstance().game.xprandomizer;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
