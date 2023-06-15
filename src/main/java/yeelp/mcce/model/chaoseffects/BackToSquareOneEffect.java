package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class BackToSquareOneEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		BlockPos pos = player.getWorld().getSpawnPos();
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 3));
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public String getName() {
		return "backtosquareone";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.OVERWORLD && Math.random() < 0.5;
	}

}
