package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class BatBombEffect extends SimpleTimedChaosEffect {

	protected BatBombEffect() {
		super(1200, 2400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Box range = new Box(player.getBlockPos().down(5).east(5).south(5), player.getBlockPos().up(5).west(5).north(5));
		World world = player.getWorld();
		world.getEntitiesByClass(BatEntity.class, range, (bat) -> true).forEach((bat) -> {
			world.createExplosion(bat, bat.getX(), bat.getY(), bat.getZ(), this.getRNG().nextFloat(2.0f, 6.0f), World.ExplosionSourceType.MOB);
			bat.discard();
		});
	}

	@Override
	public String getName() {
		return "batbomb";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getWorld().getRegistryKey() == World.OVERWORLD && player.getY() < player.getWorld().getSeaLevel();
	}

}
