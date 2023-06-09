package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.event.PlayerHurtCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public final class IronManEffect extends AbstractTriggeredChaosEffect {

	protected IronManEffect() {
		super(2000, 4000, 1, 5);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.IRON_EQUIP_ID, this.getRNG().nextFloat(0.6f, 1.0f), 1.0f)::sendPacket);
	}

	@Override
	public String getName() {
		return "ironman";
	}

	@Override
	public void registerCallbacks() {
		PlayerHurtCallback.EVENT.register(new OnHurtCallback());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		if(this.getTriggersRemaining() > 0) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.IRON_BREAK_ID, this.getRNG().nextFloat(0.8f, 1.000001f), 0.8f)::sendPacket);
		}
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
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
			if(player.isInvulnerableTo(source)) {
				return new CallbackResult();
			}
			CallbackResult result = new CallbackResult();
			if((MCCEAPI.accessor.getChaosEffect(player, IronManEffect.class).map(IronManEffect::getTriggersRemaining)).orElse(0) > 0) {
				result = new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
				MCCEAPI.mutator.modifyEffect(player, IronManEffect.class, (effect) -> {
					PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
						if(effect.trigger() == 0) {
							new SoundPacket(NetworkingConstants.SoundPacketConstants.IRON_BREAK_ID, effect.getRNG().nextFloat(0.8f, 1.000001f), 0.8f).sendPacket(p);
						}
						else {
							new SoundPacket(NetworkingConstants.SoundPacketConstants.IRON_HIT_ID, effect.getRNG().nextFloat(0.7f, 1.6f), 0.8f).sendPacket(p);
						}
					});
				});

			}
			return result;
		}
	}
}
