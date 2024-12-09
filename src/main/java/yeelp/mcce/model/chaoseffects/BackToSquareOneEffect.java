package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.util.SimpleUtil;

import java.util.EnumSet;

public final class BackToSquareOneEffect extends AbstractInstantChaosEffect {

	private static final double APPLY_CHANCE = 0.5;
	@Override
	public void applyEffect(PlayerEntity player) {
		BlockPos pos = player.getWorld().getSpawnPos();
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 3));
		player.teleport(SimpleUtil.getServerWorldFromEntity(player), pos.getX(), pos.getY(), pos.getZ(), EnumSet.noneOf(PositionFlag.class), player.getBodyYaw(), player.getPitch(), false);
	}

	@Override
	public String getName() {
		return "backtosquareone";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.OVERWORLD && Math.random() < APPLY_CHANCE;
	}

}
