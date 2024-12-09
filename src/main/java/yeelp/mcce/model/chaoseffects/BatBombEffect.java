package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;

public final class BatBombEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
	private static final float EXPLOSION_POWER_MIN = 2.0f, EXPLOSION_POWER_MAX = 6.0f;

	public BatBombEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		world.getEntitiesByClass(BatEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 5), (bat) -> true).forEach((bat) -> {
			world.createExplosion(bat, bat.getX(), bat.getY(), bat.getZ(), this.getRNG().nextFloat(EXPLOSION_POWER_MIN, EXPLOSION_POWER_MAX), World.ExplosionSourceType.MOB);
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
		return player.getWorld().getRegistryKey() == World.OVERWORLD && player.getY() < player.getWorld().getSeaLevel() && !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SUDDEN_DEATH);
	}

}
