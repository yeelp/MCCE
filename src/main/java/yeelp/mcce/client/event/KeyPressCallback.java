package yeelp.mcce.client.event;

import java.util.Iterator;

import org.spongepowered.include.com.google.common.collect.Iterators;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Keyboard;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.ProcessState;

@FunctionalInterface
public interface KeyPressCallback {

	Event<KeyPressCallback> EVENT = EventFactory.createArrayBacked(KeyPressCallback.class, (listeners) -> (keyboard, window, key, scancode, action, modifiers) -> {
		Iterator<KeyPressCallback> it = Iterators.forArray(listeners);
		CallbackResult result = new CallbackResult();
		for(ProcessState process = ProcessState.PASS; it.hasNext() && process == ProcessState.PASS; process = (result = it.next().onKeyPressBefore(keyboard, window, key, scancode, action, modifiers)).getProcessState());
		return result;
	});
	
	public CallbackResult onKeyPressBefore(Keyboard keyboard, long window, int key, int scancode, int action, int modifiers);
}
