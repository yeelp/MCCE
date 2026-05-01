package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttributes;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.event.PlayerHurtCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

import java.util.Objects;

public final class LavishLavaEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 3000, DURATION_MAX = 9000;
	private static final int FIRE_TICKS_TO_SET = 50;

	public LavishLavaEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setOnFire(true);
		player.setFireTicks(FIRE_TICKS_TO_SET);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.FIREBALL_ID, 1.0f, 1.0f)::send);
	}

	@Override
	public String getName() {
		return "lavishlava";
	}

	@Override
	public String getDisplayName() {
		return "Lavish Lava";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getEntityWorld().getRegistryKey() == World.NETHER;
	}

	@Override
	public void registerCallbacks() {
		PlayerHurtCallback.EVENT.register(new OnFireDamageCallback());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setOnFire(false);
		player.setFireTicks(0);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.EXTINGUISH_ID, 1.0f, 1.0f)::send);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		if(!((Boolean) Objects.requireNonNull(player.getEntityWorld().getDimension().attributes().getEntry(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS_GAMEPLAY)).argument())) {
			this.setDuration(1);
			return;
		}
		player.setOnFire(true);
		player.setFireTicks(FIRE_TICKS_TO_SET);
	}
	
	private static final class OnFireDamageCallback implements PlayerHurtCallback {

		@Override
		public CallbackResult onHurt(PlayerEntity player, DamageSource source, float amount) {
			if(MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.LAVISH_LAVA) && source.getType().effects() == DamageEffects.BURNING) {
				return new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
			}
			return new CallbackResult();
		}
		
	}

}
