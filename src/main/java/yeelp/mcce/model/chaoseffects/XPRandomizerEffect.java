package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.ModConfig;

public class XPRandomizerEffect extends SimpleTimedChaosEffect implements OptionalEffect {

	protected XPRandomizerEffect() {
		super(2000, 5000);
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
