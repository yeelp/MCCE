package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.QuiverUpdatePacket;
import yeelp.mcce.util.PlayerUtils;

public final class QuiverEffect extends SimpleTimedChaosEffect {

	private static final float AMOUNT = 1f;
	protected QuiverEffect() {
		super(2000, 3000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getServerPlayer(player).ifPresent(new QuiverUpdatePacket(this.getRNG().nextFloat(-AMOUNT, AMOUNT), this.getRNG().nextFloat(-AMOUNT, AMOUNT))::sendPacket);
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

}
