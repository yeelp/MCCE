package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;

public class BatBombEffect extends SimpleTimedChaosEffect {

	public BatBombEffect() {
		super(1200, 2400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		world.getEntitiesByClass(BatEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 5), (bat) -> true).forEach((bat) -> {
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
		return player.getWorld().getRegistryKey() == World.OVERWORLD && player.getY() < player.getWorld().getSeaLevel() && !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

}
