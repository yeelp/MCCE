package yeelp.mcce.client;

import net.fabricmc.api.ClientModInitializer;
import yeelp.mcce.client.event.ClientCallbacks;
import yeelp.mcce.client.networking.*;

public final class MCCEClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		new SoundPacketReceiver().register();
		new SilentStatUpdatePacketReceiver().register();
		new MemoryGameStatusPacketReceiver().register();
		new RainbowStatusPacketReceiver().register();
		new QuiverUpdatePacketReceiver().register();
		new ParticlePacketReceiver().register();
		new StutterSoundStatusPacketReceiver().register();
		new LookInversionStatusPacketReceiver().register();
		ClientCallbacks.registerCallbacks();
	}
}
