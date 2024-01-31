package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

public class ClippyEffect extends AbstractTimedChaosEffect {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();

	public ClippyEffect() {
		super(20, 160);
	}
	
	protected ClippyEffect(int duration) {
		super(duration, duration);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, this.durationRemaining()));
		player.noClip = true;
	}

	@Override
	public String getName() {
		return "clippy";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getY() > 0 && !MCCEAPI.accessor.isChaosEffectActive(player, BouncyEffect.class);
	}

	@Override
	public void registerCallbacks() {
		PlayerTickCallback.EVENT.register(new ClippyTickHandler());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player);
		player.noClip = false;
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return AFFECTED_PLAYERS.tracked(player);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		if(!AFFECTED_PLAYERS.tracked(player)) {
			AFFECTED_PLAYERS.add(player);
		}
		if(!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, this.durationRemaining()));
		}
	}
	
	private static final class ClippyTickHandler implements PlayerTickCallback {

		@Override
		public void tick(PlayerEntity player) {
			if(ClippyEffect.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, ClippyEffect.class)) {
				MCCEAPI.mutator.addNewChaosEffect(player, new ClippyEffect(1));
			}
		}
		
	}

}
