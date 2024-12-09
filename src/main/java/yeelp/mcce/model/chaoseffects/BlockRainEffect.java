package yeelp.mcce.model.chaoseffects;

import java.util.Optional;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.ModConfig;

public final class BlockRainEffect extends AbstractRainEffect implements OptionalEffect {

	private static final int BLOCK_COUNT = Registries.BLOCK.size();
	private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
	private static final int HORIZONTAL_RADIUS = 15;
	public BlockRainEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public String getName() {
		return "blockrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		int index = this.getRNG().nextInt(BLOCK_COUNT);
		return Registries.BLOCK.stream().skip(index).filter((b) -> b != Blocks.END_PORTAL && b != Blocks.NETHER_PORTAL).findFirst().or(() -> Optional.of(Blocks.BEDROCK)).map((b) -> {
			int x = player.getBlockX() + this.getRNG().nextInt(-HORIZONTAL_RADIUS, HORIZONTAL_RADIUS), z = player.getBlockZ() + this.getRNG().nextInt(-HORIZONTAL_RADIUS, HORIZONTAL_RADIUS);
			BlockPos pos = new BlockPos(x, player.getWorld().getTopYInclusive(), z);
			FallingBlockEntity block = FallingBlockEntity.spawnFromBlock(player.getWorld(), pos, b.getDefaultState());
			player.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			block.dropItem = false;
			return block;
		}).get();
	}

	@Override
	public boolean enabled() {
		return ModConfig.getInstance().game.blockrain;
	}

}
