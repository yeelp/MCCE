package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public final class WrapAroundEffect extends SimpleTimedChaosEffect {

	protected WrapAroundEffect() {
		super(4000, 5000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		if(player.getY() <= world.getDimension().minY()) {
			player.refreshPositionAfterTeleport(player.getX(), world.getTopY(), player.getZ());
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
		return player.getWorld().getRegistryKey() == World.END;
	}

}
