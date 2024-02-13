package yeelp.mcce.model.chaoseffects;

import java.util.Optional;

import net.minecraft.block.Blocks;
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
		return Registries.BLOCK.stream().skip(index).filter((b) -> b != Blocks.END_PORTAL && b != Blocks.NETHER_PORTAL).findFirst().or(() -> Optional.of(Blocks.BEDROCK)).map((b) -> {
			int x = player.getBlockX() + this.getRNG().nextInt(-15, 15), z = player.getBlockZ() + this.getRNG().nextInt(-15, 15);
			FallingBlockEntity block = FallingBlockEntity.spawnFromBlock(player.getWorld(), new BlockPos(x, player.getWorld().getTopY(), z), b.getDefaultState());
			block.dropItem = false;
			return block;
		}).get();
	}

	@Override
	public boolean enabled() {
		return ModConfig.getInstance().game.blockRain;
	}

}
