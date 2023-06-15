package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.event.PlayerHurtCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public final class LavishLavaEffect extends AbstractTimedChaosEffect {

	protected LavishLavaEffect() {
		super(3000, 9000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.setOnFire(true);
		player.setFireTicks(50);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.FIREBALL_ID, 1.0f, 1.0f)::sendPacket);
	}

	@Override
	public String getName() {
		return "lavishlava";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.NETHER;
	}

	@Override
	public void registerCallbacks() {
		PlayerHurtCallback.EVENT.register(new OnFireDamageCallback());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		player.setOnFire(false);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.EXTINGUISH_ID, 1.0f, 1.0f)::sendPacket);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		if(!player.getWorld().getDimension().respawnAnchorWorks()) {
			this.setDuration(1);
			return;
		}
		player.setOnFire(true);
		player.setFireTicks(50);
	}
	
	private static final class OnFireDamageCallback implements PlayerHurtCallback {

		@Override
		public CallbackResult onHurt(PlayerEntity player, DamageSource source, float amount) {
			if(MCCEAPI.accessor.isChaosEffectActive(player, LavishLavaEffect.class) && source.getType().effects() == DamageEffects.BURNING) {
				return new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
			}
			return new CallbackResult();
		}
		
	}

}
