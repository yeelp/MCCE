package yeelp.mcce.client.event;

import com.google.common.collect.Iterators;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.ProcessState;

import java.util.Iterator;

@FunctionalInterface
public interface KeyPressCallback {

	Event<KeyPressCallback> EVENT = EventFactory.createArrayBacked(KeyPressCallback.class, (listeners) -> (keyboard, window, keyaction, input) -> {
		Iterator<KeyPressCallback> it = Iterators.forArray(listeners);
		CallbackResult result = new CallbackResult();
		for(ProcessState process = ProcessState.PASS; it.hasNext() && process == ProcessState.PASS; process = (result = result.mergeResults(it.next().onKeyPressBefore(keyboard, window, keyaction, input))).getProcessState());
		return result;
	});
	
	CallbackResult onKeyPressBefore(Keyboard keyboard, long window, int keyaction, KeyInput input);
}
