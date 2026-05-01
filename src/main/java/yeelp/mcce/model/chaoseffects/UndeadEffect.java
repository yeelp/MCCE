package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.SimpleUtil;

public final class UndeadEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1800, DURATION_MAX = 2000;
	public UndeadEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@SuppressWarnings("MagicNumber")
    @Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getEntityWorld();
		if(world.isClient() || !world.isDay() || player.isTouchingWaterOrRain() || player.inPowderSnow || player.wasInPowderSnow) {
			return;
		}
		if(world.isSkyVisible(BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ()))) {
			@SuppressWarnings("deprecation")
			float eyeBrightness = player.getBrightnessAtEyes();
			if(eyeBrightness > 0.5f && this.getRNG().nextFloat() * 30.0f < (eyeBrightness - 0.4f) * 2.0f) {
				ItemStack helm = player.getEquippedStack(EquipmentSlot.HEAD);
				if(!helm.isEmpty()) {
					if(helm.isDamageable()) {
						helm.damage(this.getRNG().nextInt(2), SimpleUtil.getServerWorldFromEntity(player), PlayerUtils.getServerPlayer(player).orElseThrow(), (p) -> player.setOnFireFor(4));
					}
				}
				else {
					player.setOnFireFor(8);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "undead";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getEntityWorld().getRegistryKey() == World.OVERWORLD && player.getY() >= player.getEntityWorld().getSeaLevel() && !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SUDDEN_DEATH);
	}

}
