package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.PlayerUtils;

public final class PressLToLevitateEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 800, DURATION_MAX = 1800;
	public PressLToLevitateEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setNoGravity(true);
	}

	@Override
	public String getName() {
		return "pressltolevitate";
	}

	@Override
	public String getDisplayName() {
		return "Press L to Levitate";
	}

	@Override
	public void registerCallbacks() {
		//no callbacks
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setNoGravity(false);
		//reset floating timing so player doesn't get kicked for flying too long on non flying servers.
		PlayerUtils.getServerPlayer(player).ifPresent((p) -> p.networkHandler.resetFloatingTicks());
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SMACK_DOWN, ChaosEffects.RAVE);
	}

	@Override
	protected boolean canStack() {
		return true;
	}

}
