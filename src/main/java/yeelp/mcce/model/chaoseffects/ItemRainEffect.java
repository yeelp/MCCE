package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.MCCESpawnCap;

public final class ItemRainEffect extends AbstractRainEffect {

	private static final int ITEM_COUNT = Registries.ITEM.size();
	private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
	private static final int HORIZONTAL_RADIUS = 15;
	public ItemRainEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public String getName() {
		return "itemrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		int index = this.getRNG().nextInt(ITEM_COUNT);
		return Registries.ITEM.stream().skip(index).findFirst().map((item) -> {
			final double x = this.getRNG().nextDouble(-HORIZONTAL_RADIUS, HORIZONTAL_RADIUS), z = this.getRNG().nextDouble(-HORIZONTAL_RADIUS,  HORIZONTAL_RADIUS);
			ItemStack stack = new ItemStack(item);
            Entity itemEntity = new ItemEntity(player.getWorld(), player.getX() + x, player.getWorld().getTopYInclusive(), player.getZ() + z, stack);
			MCCEAPI.mutator.setDespawnTimer(itemEntity, this.durationRemaining() + DURATION_MAX/2);
			return itemEntity;
		}).orElseThrow();
	}

	@Override
	protected MCCESpawnCap getSpawnCap() {
		return MCCESpawnCap.ITEM;
	}
}
