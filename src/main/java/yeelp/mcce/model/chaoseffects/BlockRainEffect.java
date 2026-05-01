package yeelp.mcce.model.chaoseffects;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.ModConfig;
import yeelp.mcce.util.MCCESpawnCap;

import java.util.Optional;

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
	public String getDisplayName() {
		return "Block Rain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		int index = this.getRNG().nextInt(BLOCK_COUNT);
		return Registries.BLOCK.stream().skip(index).filter((b) -> b != Blocks.END_PORTAL && b != Blocks.NETHER_PORTAL).findFirst().or(() -> Optional.of(Blocks.BEDROCK)).map((b) -> {
			int x = player.getBlockX() + this.getRNG().nextInt(-HORIZONTAL_RADIUS, HORIZONTAL_RADIUS), z = player.getBlockZ() + this.getRNG().nextInt(-HORIZONTAL_RADIUS, HORIZONTAL_RADIUS);
			BlockPos pos = new BlockPos(x, player.getEntityWorld().getTopYInclusive(), z);
			FallingBlockEntity block = FallingBlockEntity.spawnFromBlock(player.getEntityWorld(), pos, b.getDefaultState());
			player.getEntityWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			block.dropItem = false;
			return block;
		}).get();
	}

	@Override
	protected MCCESpawnCap getSpawnCap() {
		return MCCESpawnCap.FALLING_BLOCK;
	}

	@Override
	public boolean enabled() {
		return ModConfig.getInstance().game.blockrain;
	}

}
