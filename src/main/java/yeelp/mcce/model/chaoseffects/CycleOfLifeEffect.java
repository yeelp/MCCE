package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SilentStatUpdatePacket;
import yeelp.mcce.network.SoundPacket;

public final class CycleOfLifeEffect extends SimpleTimedChaosEffect {

	public CycleOfLifeEffect() {
		super(500, 1000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setHealth(this.getRNG().nextFloat(1, player.getMaxHealth()));
		if(player instanceof ServerPlayerEntity) {
			new SilentStatUpdatePacket(player).sendPacket((ServerPlayerEntity) player);
			new SoundPacket(NetworkingConstants.SoundPacketConstants.UI_BUTTON_CLICK_ID, this.getRNG().nextFloat(0.0f, 2.0f), 0.25f).sendPacket((ServerPlayerEntity) player);
		}
	}

	@Override
	public String getName() {
		return "cycleoflife";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

	@Override
	protected boolean canStack() {
		return false;
	}
}
