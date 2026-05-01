package yeelp.mcce.client.event;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BeforeInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.client.event.ClientRenderCallbacks.*;
import yeelp.mcce.client.screen.GuiWarningScreen;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.event.TiltScreenCallback;
import yeelp.mcce.model.chaoseffects.*;
import yeelp.mcce.network.NetworkingConstants;

import java.util.function.Predicate;

public final class ClientCallbacks {

	public static void registerCallbacks() {
		KeyPressCallback.EVENT.register(new MemoryGameKeyboardHandler());
		PlayerTickCallback.EVENT.register(new StutterSoundSoundHandler());
		TiltScreenCallback.EVENT.register(new CycleOfLifeTiltHandler());
		RainbowHandler rainbow = new RainbowHandler();
		ChangeSpriteColourCallback.EVENT.register(rainbow);
		PlayerTickCallback.EVENT.register(rainbow);
		BeforeClientRenderCallback.EVENT.register(new RotateScreenHandler());
		registerShaderCallback(NetworkingConstants.GRAYSCALE_STATUS_PACKET_ID, GrayscaleEffect::isClientTracked);
		registerShaderCallback(NetworkingConstants.MOSAIC_STATUS_PACKET_ID, MosaicEffect::isClientTracked);
		registerShaderCallback(NetworkingConstants.COLOUR_INVERSION_STATUS_PACKET_ID, ColourInversionEffect::isClientTracked);
		registerShaderCallback(NetworkingConstants.BAKE_STATUS_PACKET_ID, BakeEffect::isClientTracked);
		registerShaderCallback(NetworkingConstants.SEPIA_STATUS_PACKET_ID, SepiaEffect::isClientTracked);
		OnCameraUpdate.EVENT.register(new AroundTheWorldScreenHandler());
		OnSpriteDrawCallback.EVENT.register(new PaintSpriteHandler());
		OnSpriteDrawCallback.EVENT.register(new SpinToWinSpriteHandler());
		OnSpriteDrawCallback.EVENT.register(new ScatterSpriteHandler());
		ChangeSpriteCallback.EVENT.register(new IconicSpriteSwapper());
		FlippingOutModelHandler handler = new FlippingOutModelHandler();
		PlayerTickCallback.EVENT.register(handler);
		BeforeModelRenderCallback.EVENT.register(handler);
		ScreenEvents.BEFORE_INIT.register(new BeforeInit() {
			private boolean displayed = false;
			@Override
			public void beforeInit(@NotNull MinecraftClient minecraftClient, @NotNull Screen screen, int i, int i1) {
				if(!this.displayed && screen instanceof TitleScreen) {
					this.displayed = true;
					minecraftClient.setScreen(new GuiWarningScreen(screen));
				}
			}
		});
	}

	private static void registerShaderCallback(Identifier id, Predicate<PlayerEntity> check) {
		SetShaderCallback.EVENT.register(new ChaosShaderCallback(id, check));
	}

}
