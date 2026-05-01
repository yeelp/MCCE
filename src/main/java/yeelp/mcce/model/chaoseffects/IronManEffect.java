package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.event.PlayerHurtCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class IronManEffect extends AbstractTriggeredChaosEffect {

	private static final int DURATION_MIN = 2000, DURATION_MAX = 4000;
	private static final int INTERVAL_MIN = 1, INTERVAL_MAX = 5;
	private static final float APPLY_VOLUME = 1.0f, BREAK_VOLUME = 0.8f, HIT_VOLUME = 0.8f;
	private static final float APPLY_PITCH_MIN = 0.6f, APPLY_PITCH_MAX = 1.0f;
	private static final float BREAK_PITCH_MIN = 0.6f, BREAK_PITCH_MAX = 1.0f;
	private static final float HIT_PITCH_MIN = 0.6f, HIT_PITCH_MAX = 1.0f;

	public IronManEffect() {
		super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.IRON_EQUIP_ID, this.getRNG().nextFloat(APPLY_PITCH_MIN, APPLY_PITCH_MAX), APPLY_VOLUME)::send);
	}

	@Override
	public String getName() {
		return "ironman";
	}

	@Override
	public String getDisplayName() {
		return "Iron Man";
	}

	@Override
	public void registerCallbacks() {
		PlayerHurtCallback.EVENT.register(new OnHurtCallback());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		if(this.getTriggersRemaining() > 0) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.IRON_BREAK_ID, this.getRNG().nextFloat(BREAK_PITCH_MIN, BREAK_PITCH_MAX), BREAK_VOLUME)::send);
		}
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no additional effect logic
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	private static final class OnHurtCallback implements PlayerHurtCallback {
		@Override
		public CallbackResult onHurt(PlayerEntity player, DamageSource source, float amount) {
			MinecraftServer server = player.getEntityWorld().getServer();
			if(server != null && player.isInvulnerableTo(server.getWorld(player.getEntityWorld().getRegistryKey()), source)) {
				return new CallbackResult();
			}
			CallbackResult result = new CallbackResult();
			if((MCCEAPI.accessor.getChaosEffect(player, IronManEffect.class).map(IronManEffect::getTriggersRemaining)).orElse(0) > 0) {
				result = new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
				MCCEAPI.mutator.modifyEffect(player, IronManEffect.class, (effect) -> PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
						if(effect.trigger() == 0) {
							new SoundPayload(NetworkingConstants.SoundPacketConstants.IRON_BREAK_ID, effect.getRNG().nextFloat(BREAK_PITCH_MIN, BREAK_PITCH_MAX), BREAK_VOLUME).send(p);
						}
						else {
							new SoundPayload(NetworkingConstants.SoundPacketConstants.IRON_HIT_ID, effect.getRNG().nextFloat(HIT_PITCH_MIN, HIT_PITCH_MAX), HIT_VOLUME).send(p);
						}
					}));

			}
			return result;
		}
	}
}
