package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.ModConfig;

public class BlockRainEffect extends AbstractRainEffect implements OptionalEffect {

	private static final int BLOCK_COUNT = Registries.BLOCK.size();
	protected BlockRainEffect() {
		super(1200, 2400);
	}

	@Override
	public String getName() {
		return "blockrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		int index = this.getRNG().nextInt(BLOCK_COUNT);
		return Registries.BLOCK.stream().skip(index).findFirst().map((b) -> {
			int x = player.getBlockX() + this.getRNG().nextInt(-15, 15), z = player.getBlockZ() + this.getRNG().nextInt(-15, 15);
			return FallingBlockEntity.spawnFromBlock(player.getWorld(), new BlockPos(x, player.getWorld().getTopY(), z), b.getDefaultState());
		}).get();
	}

	@Override
	public boolean enabled() {
		return ModConfig.getInstance().game.blockRain;
	}

}
