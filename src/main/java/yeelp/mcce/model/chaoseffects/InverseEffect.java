package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.network.InverseStatusPayload;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class InverseEffect extends ClientPlayerTrackingChaosEffect<InverseStatusPayload> {

	private boolean silent = false;
	private static final int DURATION_MIN = 1200, DURATION_MAX = 1800;
	
	public InverseEffect() {
		super(DURATION_MIN, DURATION_MAX, InverseStatusPayload::new);
	}
	
    InverseEffect(int duration) {
		super(duration, InverseStatusPayload::new);
		this.silent = true;
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		if(!this.silent) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.INVERSE_START, 1.0f, 1.0f)::send);
		}
	}

	@Override
	public String getName() {
		return "inverse";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		if(!this.silent && player instanceof ServerPlayerEntity) {
			new SoundPayload(NetworkingConstants.SoundPacketConstants.INVERSE_END, 1.0f, 1.0f).send((ServerPlayerEntity) player);
		}
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return ClientPlayerTrackingChaosEffect.isAffected(player, ChaosEffects.INVERSE);
	}

	public static void trackClient(PlayerEntity player, InverseStatusPayload payload) {
		ClientPlayerTrackingChaosEffect.trackClient(player, ChaosEffects.INVERSE, payload);
	}

	@Override
	protected ResetEffectHandler getHandler() {
		return new InverseTickHandler();
	}

	public static Vec3d invertMovement(PlayerEntity player, Vec3d input) {
		if(ClientPlayerTrackingChaosEffect.isClientTracked(player, ChaosEffects.INVERSE)) {
			return input.multiply(-1, 1, -1);
		}
		return input;
	}

	private static final class InverseTickHandler extends ResetEffectHandler {

		@Override
		protected ChaosEffectRegistryEntry getRegistryEntry() {
			return ChaosEffects.INVERSE;
		}

		@Override
		protected boolean isAffected(PlayerEntity player) {
			return InverseEffect.isAffected(player);
		}

		@Override
		protected ChaosEffect createDummyChaosEffectWithDurationOne() {
			return new InverseEffect(1);
		}

	}

}
