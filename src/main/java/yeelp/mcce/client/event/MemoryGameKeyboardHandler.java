package yeelp.mcce.client.event;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.mixin.KeyboardASMMixin;
import yeelp.mcce.util.Tracker;

public final class MemoryGameKeyboardHandler implements KeyPressCallback {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	
	@Override
	public CallbackResult onKeyPressBefore(Keyboard keyboard, long window, int keyaction, KeyInput input) {
		MinecraftClient client = ((KeyboardASMMixin) keyboard).getClient();
		if(client.player != null && AFFECTED_PLAYERS.tracked(client.player) && keyaction != 0 && client.currentScreen == null && input.getKeycode() == GLFW.GLFW_KEY_F3) {
			return new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
		}
		return new CallbackResult();
	}

	public static void addPlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player.getUuid());
	}
	
	public static void removePlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player.getUuid());
	}

}
