package yeelp.mcce.client.event;

import yeelp.mcce.event.PlayerTickCallback;

public final class ClientCallbacks {

	public static final void registerCallbacks() {
		KeyPressCallback.EVENT.register(new MemoryGameKeyboardHandler());
		ClientRenderCallbacks.RenderHealthCallback.Phase.BEFORE.register(new RainbowGuiHandler(false));
		ClientRenderCallbacks.RenderHealthCallback.Phase.AFTER.register(new RainbowGuiHandler(true));
		ClientRenderCallbacks.AfterShaderSetCallback.EVENT.register(new RainbowGuiHandler.RainbowShaderHandler());
		PlayerTickCallback.EVENT.register(new StutterSoundSoundHandler());
	}
}
