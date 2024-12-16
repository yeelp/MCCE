package yeelp.mcce.client.event;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import yeelp.mcce.client.screen.GuiWarningScreen;
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
		ScreenEvents.BEFORE_INIT.register(new BeforeInit() {
			private boolean displayed = false;
			@Override
			public void beforeInit(MinecraftClient minecraftClient, Screen screen, int i, int i1) {
				if(!this.displayed && screen instanceof TitleScreen) {
					this.displayed = true;
					minecraftClient.setScreen(new GuiWarningScreen(screen));
				}
			}
		});
	}
}
