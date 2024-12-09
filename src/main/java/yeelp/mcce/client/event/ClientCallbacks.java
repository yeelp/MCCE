package yeelp.mcce.client.event;

import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.event.TiltScreenCallback;

public final class ClientCallbacks {

	public static void registerCallbacks() {
		KeyPressCallback.EVENT.register(new MemoryGameKeyboardHandler());
		ClientRenderCallbacks.RenderHealthCallback.Phase.BEFORE.register(new RainbowGuiHandler(false));
		ClientRenderCallbacks.RenderHealthCallback.Phase.AFTER.register(new RainbowGuiHandler(true));
		ClientRenderCallbacks.ChangeTextureColour.EVENT.register(new RainbowGuiHandler.RainbowShaderHandler());
		PlayerTickCallback.EVENT.register(new StutterSoundSoundHandler());
		TiltScreenCallback.EVENT.register(new CycleOfLifeTiltHandler());
	}
}
