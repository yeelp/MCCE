package yeelp.mcce.event;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.event.CallbackResult.ProcessState;

@FunctionalInterface
public interface PlayerHurtCallback {
	
	Event<PlayerHurtCallback> EVENT = EventFactory.createArrayBacked(PlayerHurtCallback.class, (listeners) -> (player, source, amount) -> {
		Iterator<PlayerHurtCallback> it = Iterators.forArray(listeners);
		CallbackResult result = new CallbackResult();
		for(ProcessState process = ProcessState.PASS; it.hasNext() && process == ProcessState.PASS; process = (result = it.next().onHurt(player, source, amount)).getProcessState()); 
		return result;
	});
	
	CallbackResult onHurt(PlayerEntity player, DamageSource source, float amount);
}
