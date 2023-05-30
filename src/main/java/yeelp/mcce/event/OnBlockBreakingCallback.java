package yeelp.mcce.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface OnBlockBreakingCallback {

	Event<OnBlockBreakingCallback> EVENT = EventFactory.createArrayBacked(OnBlockBreakingCallback.class, (listeners) -> (world, id, pos, progress) -> {
		for(OnBlockBreakingCallback obbc : listeners) {
			obbc.onBlockBreaking(world, id, pos, progress);
		}
	});

	void onBlockBreaking(ServerWorld world, int entityId, BlockPos pos, int progress);

}
