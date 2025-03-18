package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.mixin.EntityASMMixin;
import yeelp.mcce.network.ClippyStatusPayload;

public final class ClippyEffect extends ClientPlayerTrackingChaosEffect<ClippyStatusPayload> {

	private static final int DURATION_MIN = 20, DURATION_MAX = 160;

	public ClippyEffect() {
		super(DURATION_MIN, DURATION_MAX, ClippyStatusPayload::new);
	}
	
	ClippyEffect(int duration) {
		super(duration, ClippyStatusPayload::new);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, this.durationRemaining()));
		player.noClip = true;
		((EntityASMMixin) player).setOnGround(false);
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
		return player.getY() > 0 && MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.BOUNCY, ChaosEffects.SMACK_DOWN);
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.CLIPPY);
	}

	public static void trackClient(PlayerEntity player, ClippyStatusPayload payload) {
		ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.CLIPPY, payload);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		super.tickAdditionalEffectLogic(player);
		if(!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, this.durationRemaining()));
		}
	}

	@Override
	protected ResetEffectHandler getHandler() {
		return new ClippyTickHandler();
	}

	@Override
	public void registerCallbacks() {
		super.registerCallbacks();
		EntityTickCallback.EVENT.register(new ClippyEntityTickHandler());
	}

	public static void setClippyState(PlayerEntity player) {
		if(ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.CLIPPY)) {
			((EntityASMMixin) player).setOnGround(false);
			player.noClip = true;
		}
	}

	private static final class ClippyTickHandler extends ResetEffectHandler {

		@Override
		protected ChaosEffectRegistryEntry getRegistryEntry() {
			return ChaosEffects.CLIPPY;
		}

		@Override
		protected boolean isAffected(PlayerEntity player) {
			return ClippyEffect.isAffected(player);
		}

		@Override
		protected ChaosEffect createDummyChaosEffectWithDurationOne() {
			return new ClippyEffect(1);
		}

	}

	private static final class ClippyEntityTickHandler implements EntityTickCallback {

		@Override
		public void tick(Entity entity) {
			if(entity instanceof PlayerEntity player && (ClippyEffect.isAffected(player) || ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.CLIPPY))) {
				player.noClip = true;
				((EntityASMMixin) entity).setOnGround(false);
			}
		}
	}

}
