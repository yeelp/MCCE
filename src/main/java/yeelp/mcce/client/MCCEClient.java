package yeelp.mcce.client;

import net.fabricmc.api.ClientModInitializer;
import yeelp.mcce.client.event.ClientCallbacks;
import yeelp.mcce.client.event.ScatterSpriteHandler;
import yeelp.mcce.client.networking.*;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.model.chaoseffects.*;
import yeelp.mcce.network.NetworkingConstants;

public final class MCCEClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		new SoundPacketReceiver().register();
		new SilentStatUpdatePacketReceiver().register();
		new MemoryGameStatusPacketReceiver().register();
		new QuiverUpdatePacketReceiver().register();
		new ParticlePacketReceiver().register();
		new StutterSoundStatusPacketReceiver().register();
		new AroundTheWorldStatusPacketReceiver().register();
		new ClippyStatusPacketReceiver().register();
		new ClickyReceiver().register();
		new HotbarRouletteReceiver().register();
		new StatusUpdatePacketReceiver(NetworkingConstants.RAINBOW_STATUS_PACKET_ID, RainbowEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.LOOK_INVERSION_STATUS_PACKET_ID, LookInversionEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.INVERSE_STATUS_PACKET_ID, InverseEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.ROTATE_STATUS_PACKET_ID, RotateEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.SPIN_TO_WIN_STATUS_PACKET_ID, SpinToWinEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.PAINT_STATUS_PACKET_ID, PaintEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.SCATTER_PACKET_ID, (player, payload) -> {
			ScatterEffect.trackClient(player, payload);
			if(payload.status()) {
				ScatterSpriteHandler.addPlayer(player);
			}
			else {
				ScatterSpriteHandler.removePlayer(player);
			}
		}).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.FLIPPING_OUT_STATUS_PACKET_ID, FlippingOutEffect::trackClient).register();
		new StatusUpdatePacketReceiver(NetworkingConstants.GAMMEGA_STATUS_PACKET_ID, GammegaEffect::trackClient).register();
		new ShaderStatusPacketReceiver(NetworkingConstants.GRAYSCALE_STATUS_PACKET_ID, GrayscaleEffect::trackClient).register();
		new ShaderStatusPacketReceiver(NetworkingConstants.MOSAIC_STATUS_PACKET_ID, MosaicEffect::trackClient).register();
		new ShaderStatusPacketReceiver(NetworkingConstants.COLOUR_INVERSION_STATUS_PACKET_ID, ColourInversionEffect::trackClient).register();
		new ShaderStatusPacketReceiver(NetworkingConstants.BAKE_STATUS_PACKET_ID, BakeEffect::trackClient).register();
		new ShaderStatusPacketReceiver(NetworkingConstants.SEPIA_STATUS_PACKET_ID, SepiaEffect::trackClient).register();
		new PushyPacketReceiver().register();
		new IconicPacketReceiver().register();
		new PolitePacketReceiverClient().register();
		EntityInitialUpdatePacketReceiver receiver = new EntityInitialUpdatePacketReceiver();
		receiver.register();
		EntityTickCallback.EVENT.register(receiver);
		ClientCallbacks.registerCallbacks();
	}
}
