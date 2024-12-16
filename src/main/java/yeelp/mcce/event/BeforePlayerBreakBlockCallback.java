package yeelp.mcce.event;

import com.google.common.collect.Iterators;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.event.CallbackResult.CancelState;

import java.util.Iterator;

@FunctionalInterface
public interface BeforePlayerBreakBlockCallback {

    Event<BeforePlayerBreakBlockCallback> EVENT = EventFactory.createArrayBacked(BeforePlayerBreakBlockCallback.class, (listeners) -> (pos, player) -> {
        CallbackResult result = new CallbackResult();
        Iterator<BeforePlayerBreakBlockCallback> it = Iterators.forArray(listeners);
        for(CancelState s = CancelState.PASS; it.hasNext() && s == CancelState.PASS; s = (result = it.next().beforeBlockBreak(pos, player)).getCancelState());
        return result;
    });

    CallbackResult beforeBlockBreak(BlockPos pos, PlayerEntity player);
}
