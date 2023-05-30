package yeelp.mcce.client.event;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.mixin.KeyboardASMMixin;
import yeelp.mcce.util.Tracker;

public final class MemoryGameKeyboardHandler implements KeyPressCallback {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	
	@Override
	public CallbackResult onKeyPressBefore(Keyboard keyboard, long window, int key, int scancode, int action, int modifiers) {
		MinecraftClient client = ((KeyboardASMMixin) keyboard).getClient();
		if(client.player != null && AFFECTED_PLAYERS.tracked(client.player) && action != 0 && client.currentScreen == null && key == GLFW.GLFW_KEY_F1) {
			return new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
		}
		return new CallbackResult();
	}

	public static final void addPlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player.getUuid());
	}
	
	public static final void removePlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player.getUuid());
	}

}
