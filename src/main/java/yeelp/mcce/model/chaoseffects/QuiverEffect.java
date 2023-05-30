package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.QuiverUpdatePacket;

public final class QuiverEffect extends SimpleTimedChaosEffect {

	private static final float AMOUNT = 1f;
	protected QuiverEffect() {
		super(2000, 3000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			new QuiverUpdatePacket(this.getRNG().nextFloat(-AMOUNT, AMOUNT), this.getRNG().nextFloat(-AMOUNT, AMOUNT)).sendPacket((ServerPlayerEntity) player);			
		}
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
