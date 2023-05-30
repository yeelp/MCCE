package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.MemoryGameStatusPacket;

public final class MemoryGameEffect extends AbstractTimedChaosEffect {

	protected MemoryGameEffect() {
		super(1000, 1500);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			new MemoryGameStatusPacket(true).sendPacket((ServerPlayerEntity) player);
		}
	}

	@Override
	public String getName() {
		return "memorygame";
	}

	@Override
	public void registerCallbacks() {
		return;
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		if(player instanceof ServerPlayerEntity) {
			new MemoryGameStatusPacket(false).sendPacket((ServerPlayerEntity) player);
		}
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, RainbowEffect.class);
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
