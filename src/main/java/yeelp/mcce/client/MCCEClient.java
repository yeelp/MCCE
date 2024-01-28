package yeelp.mcce.client;

import net.fabricmc.api.ClientModInitializer;
import yeelp.mcce.client.event.ClientCallbacks;
import yeelp.mcce.client.networking.MemoryGameStatusPacketReceiver;
import yeelp.mcce.client.networking.ParticlePacketReceiver;
import yeelp.mcce.client.networking.QuiverUpdatePacketReceiver;
import yeelp.mcce.client.networking.RainbowStatusPacketReceiver;
import yeelp.mcce.client.networking.SilentStatUpdatePacketReceiver;
import yeelp.mcce.client.networking.SoundPacketReceiver;
import yeelp.mcce.client.networking.StutterSoundStatusPacketReceiver;

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
		ClientCallbacks.registerCallbacks();
	}
}
