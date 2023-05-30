package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.RainbowStatusPacket;

public final class RainbowEffect extends AbstractTimedChaosEffect {

	public RainbowEffect() {
		super(1000, 2000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			new RainbowStatusPacket(true).sendPacket((ServerPlayerEntity) player);			
		}
	}

	@Override
	public String getName() {
		return "rainbow";
	}

	@Override
	public void registerCallbacks() {
		return;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			new RainbowStatusPacket(false).sendPacket((ServerPlayerEntity) player);			
		}
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, MemoryGameEffect.class);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
