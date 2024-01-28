package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.StutterSoundStatusPacket;

public class StutterSoundEffect extends StatusPacketSendingChaosEffect<StutterSoundStatusPacket> {

	protected StutterSoundEffect() {
		super(1200, 2400, StutterSoundStatusPacket::new);
	}

	@Override
	public String getName() {
		return "stuttersound";
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
