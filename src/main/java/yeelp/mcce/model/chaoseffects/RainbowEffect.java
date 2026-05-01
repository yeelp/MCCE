package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.network.RainbowStatusPayload;

public final class RainbowEffect extends ClientPlayerTrackingChaosEffect<RainbowStatusPayload> {

	private static final int DURATION_MIN = 1000, DURATION_MAX = 2000;
	public RainbowEffect() {
		super(DURATION_MIN, DURATION_MAX, RainbowStatusPayload::new);
	}

	RainbowEffect(int duration) {
		super(duration, RainbowStatusPayload::new);
	}

	@Override
	public String getName() {
		return "rainbow";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.MEMORY_GAME, ChaosEffects.GRAYSCALE);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	public static boolean isClientTracked(PlayerEntity player) {
		return ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.RAINBOW);
	}

	public static boolean isAffected(PlayerEntity player) {
		return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.RAINBOW);
	}

	public static void trackClient(PlayerEntity player, StatusPayload payload) {
		ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.RAINBOW, payload);
	}

	@Override
	protected ResetEffectHandler getHandler() {
		return new ResetEffectHandler() {
			@Override
			protected ChaosEffectRegistryEntry getRegistryEntry() {
				return ChaosEffects.RAINBOW;
			}

			@Override
			protected boolean isAffected(PlayerEntity player) {
				return RainbowEffect.isAffected(player);
			}

			@Override
			protected ChaosEffect createDummyChaosEffectWithDurationOne() {
				return new RainbowEffect(1);
			}
		};
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
