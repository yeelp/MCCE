package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SilentStatUpdatePacket;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public final class CycleOfLifeEffect extends SimpleTimedChaosEffect {

	public CycleOfLifeEffect() {
		super(500, 1000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setHealth(this.getRNG().nextFloat(1, player.getMaxHealth()));
		PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
			new SilentStatUpdatePacket(p).sendPacket(p);
			new SoundPacket(NetworkingConstants.SoundPacketConstants.UI_BUTTON_CLICK_ID, this.getRNG().nextFloat(0.0f, 2.0f), 0.25f).sendPacket(p);
		});
	}

	@Override
	public String getName() {
		return "cycleoflife";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.areAnyChaosEffectsActive(player, SuddenDeathEffect.class, EquilibriumEffect.class);
	}

	@Override
	protected boolean canStack() {
		return false;
	}
}
