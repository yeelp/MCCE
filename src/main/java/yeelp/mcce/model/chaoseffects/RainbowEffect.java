package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.RainbowStatusPacket;

public final class RainbowEffect extends StatusPacketSendingChaosEffect<RainbowStatusPacket> {

	public RainbowEffect() {
		super(1000, 2000, RainbowStatusPacket::new);
	}

	@Override
	public String getName() {
		return "rainbow";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, MemoryGameEffect.class);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
