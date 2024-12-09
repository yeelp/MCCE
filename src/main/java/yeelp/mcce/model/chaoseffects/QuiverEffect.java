package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.ModConfig;
import yeelp.mcce.network.QuiverPayload;
import yeelp.mcce.util.PlayerUtils;

public final class QuiverEffect extends SimpleTimedChaosEffect implements OptionalEffect {

	private static final float AMOUNT = 1f;
	private static final int DURATION_MIN = 1000, DURATION_MAX = 1500;
	public QuiverEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getServerPlayer(player).ifPresent(new QuiverPayload(this.getRNG().nextFloat(-AMOUNT, AMOUNT), this.getRNG().nextFloat(-AMOUNT, AMOUNT))::send);
	}

	@Override
	public String getName() {
		return "quiver";
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
	public boolean enabled() {
		return ModConfig.getInstance().game.quiver;
	}

}
