package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.MemoryGameStatusPacket;

public final class MemoryGameEffect extends StatusPacketSendingChaosEffect<MemoryGameStatusPacket> {

	protected MemoryGameEffect() {
		super(1000, 1500, MemoryGameStatusPacket::new);
	}

	@Override
	public String getName() {
		return "memorygame";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, RainbowEffect.class);
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
