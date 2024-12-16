package yeelp.mcce.event;

import com.google.common.collect.Iterators;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.event.CallbackResult.CancelState;

import java.util.Iterator;

@FunctionalInterface
public interface OnBlockPlaceCallback {
    Event<OnBlockPlaceCallback> EVENT = EventFactory.createArrayBacked(OnBlockPlaceCallback.class, (listeners) -> ((world, pos, state, placer, stack) -> {
        CallbackResult result = new CallbackResult();
        Iterator<OnBlockPlaceCallback> it = Iterators.forArray(listeners);
        for(CancelState cancel = CancelState.PASS; it.hasNext() && cancel == CancelState.PASS; cancel = (result = it.next().onBlockPlace(world, pos, state, placer, stack)).getCancelState());
        return result;
    }));

    CallbackResult onBlockPlace(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack);
}
