package yeelp.mcce.event;

import com.google.common.collect.Iterators;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.event.CallbackResult.CancelState;

import java.util.Iterator;

@FunctionalInterface
public interface TiltScreenCallback {

    Event<TiltScreenCallback> EVENT = EventFactory.createArrayBacked(TiltScreenCallback.class, (listeners) -> (player, deltaX, deltaY) -> {
        CallbackResult result = new CallbackResult();
        Iterator<TiltScreenCallback> it = Iterators.forArray(listeners);
        for(CancelState state = CancelState.PASS; it.hasNext() && state == CancelState.PASS; state = (result = it.next().shouldAllowTilt(player, deltaX, deltaY)).getCancelState());
        return result;
    });

    CallbackResult shouldAllowTilt(PlayerEntity player, double deltaX, double deltaY);
}
