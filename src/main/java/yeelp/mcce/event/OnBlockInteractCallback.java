package yeelp.mcce.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

@FunctionalInterface
public interface OnBlockInteractCallback {

	Event<OnBlockInteractCallback> EVENT = EventFactory.createArrayBacked(OnBlockInteractCallback.class, (listeners) -> (player, world, stack, hand, hitResult) -> {
		for(OnBlockInteractCallback obic : listeners) {
			obic.onBlockInteract(player, world, stack, hand, hitResult);
		}
	});
	
	void onBlockInteract(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult);
}
