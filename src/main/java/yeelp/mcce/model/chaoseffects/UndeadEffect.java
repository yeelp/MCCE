package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;

public class UndeadEffect extends SimpleTimedChaosEffect {

	public UndeadEffect() {
		super(1800, 2000);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		if(world.isClient || !world.isDay() || player.isWet() || player.inPowderSnow || player.wasInPowderSnow) {
			return;
		}
		if(world.isSkyVisible(BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ()))) {
			@SuppressWarnings("deprecation")
			float eyeBrightness = player.getBrightnessAtEyes();
			if(eyeBrightness > 0.5f && this.getRNG().nextFloat() *30.0f < (eyeBrightness - 0.4f) * 2.0f) {
				ItemStack helm = player.getEquippedStack(EquipmentSlot.HEAD);
				if(!helm.isEmpty()) {
					if(helm.isDamageable()) {
						helm.damage(this.getRNG().nextInt(2), player, (p) -> p.setOnFireFor(4));
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
		return player.getWorld().getRegistryKey() == World.OVERWORLD && player.getY() >= player.getWorld().getSeaLevel() && !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

}
