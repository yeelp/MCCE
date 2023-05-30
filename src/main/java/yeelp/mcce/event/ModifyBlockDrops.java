package yeelp.mcce.event;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface ModifyBlockDrops {

	Event<ModifyBlockDrops> EVENT = EventFactory.createArrayBacked(ModifyBlockDrops.class, (listeners) -> (world, player, pos, state, blockEntity, tool) -> {
		//stream used to guarantee evaluation on every listener.
		return Arrays.stream(listeners).reduce(false, (b, mbd) -> b || mbd.changeBlockDrops(world, player, pos, state, blockEntity, tool), Boolean::logicalOr);
	});
	
	/**
	 * Modify block drops for a block. Spawn items individually, and return true to effectively replace block drops.
	 * @param world world instance
	 * @param player breaking player
	 * @param pos block pos
	 * @param state block state
	 * @param blockEntity potential block entity
	 * @param tool tool used
	 * @return True if the block should skip normal block drops.
	 */
	boolean changeBlockDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool);
}
