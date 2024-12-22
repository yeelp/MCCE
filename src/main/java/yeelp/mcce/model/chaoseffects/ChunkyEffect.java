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
import yeelp.mcce.util.ChaosLib;

public final class ChunkyEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 1600, DURATION_MAX = 2400;
	public ChunkyEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		//No apply effect;
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
		//No on end effect
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
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.CRUMBLE, ChaosEffects.UNBREAKABLE);
	}
	
	private static final class BlockBreakHandler implements After {

		@Override
		public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
			if(world.isClient || !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.CHUNKY)) {
				return;
			}
			ChunkPos cPos = world.getChunk(pos).getPos();
			for(int x = cPos.getStartX(); x <= cPos.getEndX(); x++) {
				for(int z = cPos.getStartZ(); z <= cPos.getEndZ(); z++) {
					ChaosLib.setToAir(world, new BlockPos(x, pos.getY(), z));
				}
			}
		}
		
	}

}
