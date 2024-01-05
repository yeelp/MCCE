package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class ItemRainEffect extends AbstractRainEffect {

	private static final int ITEM_COUNT = Registries.ITEM.size();
	public ItemRainEffect() {
		super(1200, 2400);
	}

	@Override
	public String getName() {
		return "itemrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		int index = this.getRNG().nextInt(ITEM_COUNT);
		return Registries.ITEM.stream().skip(index).findFirst().map((item) -> {
			final double x = this.getRNG().nextDouble(-15, 15), z = this.getRNG().nextDouble(-15,  15);
			ItemStack stack = new ItemStack(item);
			ItemEntity entity = new ItemEntity(player.getWorld(), player.getX() + x, player.getWorld().getTopY(), player.getZ() + z, stack);
			return entity;
		}).get();
	}

}
