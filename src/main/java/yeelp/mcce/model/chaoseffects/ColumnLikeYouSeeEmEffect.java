package yeelp.mcce.model.chaoseffects;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.After;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;

public final class ColumnLikeYouSeeEmEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 1600, DURATION_MAX = 2400;
	public ColumnLikeYouSeeEmEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		//no apply effect
	}

	@Override
	public String getName() {
		return "columnlikeyouseeem";
	}

	@Override
	public void registerCallbacks() {
		PlayerBlockBreakEvents.AFTER.register(new BlockBreakHandler());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		//no on end effect
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.CRUMBLE);
	}
	
	private static final class BlockBreakHandler implements After {

		@Override
		public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
			if(world.isClient || !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.COLUMN_LIKE_YOU_SEE_EM)) {
				return;
			}
			BlockPos colPos = new BlockPos(pos.getX(), world.getDimension().minY(), pos.getZ());
			for(int y = world.getDimension().minY(); y < world.getTopYInclusive(); y++) {
				if(!world.getBlockState(colPos).isAir() && world.isInBuildLimit(colPos)) {
					world.removeBlock(colPos, false);
				}
				colPos = colPos.up();
			}
		}
		
	}

}
