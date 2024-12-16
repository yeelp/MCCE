package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.world.World;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.SimpleUtil;

import java.util.EnumSet;

public final class WrapAroundEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 4000, DURATION_MAX = 5000;
	public WrapAroundEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		if(player.getY() <= world.getDimension().minY()) {
			player.teleport(SimpleUtil.getServerWorldFromEntity(player), player.getX(), world.getTopYInclusive(), player.getZ(), EnumSet.noneOf(PositionFlag.class), player.getBodyYaw(), player.getPitch(), false);
		}
	}

	@Override
	public String getName() {
		return "wraparound";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.END && PlayerUtils.doesPlayerHaveValidPosition(player);
	}

}
