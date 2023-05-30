package yeelp.mcce.model.chaoseffects;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.After;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;

public class ChunkyEffect extends AbstractTimedChaosEffect {

	protected ChunkyEffect() {
		super(1600, 2400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		return;
	}

	@Override
	public String getName() {
		return "chunky";
	}

	@Override
	public void registerCallbacks() {
		PlayerBlockBreakEvents.AFTER.register(new BlockBreakHandler());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		return;
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.isChaosEffectActive(player, CrumbleEffect.class);
	}
	
	private static final class BlockBreakHandler implements After {

		@Override
		public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
			if(world.isClient || !MCCEAPI.accessor.isChaosEffectActive(player, ChunkyEffect.class)) {
				return;
			}
			ChunkPos cPos = world.getChunk(pos).getPos();
			for(int x = cPos.getStartX(); x <= cPos.getEndX(); x++) {
				for(int z = cPos.getStartZ(); z <= cPos.getEndZ(); z++) {
					BlockPos bPos = new BlockPos(x, pos.getY(), z);
					if(!world.getBlockState(bPos).isAir()) {
						world.removeBlock(bPos, false);
					}
				}
			}
		}
		
	}

}
