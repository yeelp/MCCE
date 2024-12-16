package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.MemoryGameKeyboardHandler;
import yeelp.mcce.network.MemoryGamePayload;
import yeelp.mcce.network.NetworkingConstants;

public final class MemoryGameStatusPacketReceiver implements ClientPacketReceiver<MemoryGamePayload> {

	@Override
	public Identifier getID() {
		return NetworkingConstants.MEMORY_GAME_STATUS_PACKET_ID;
	}

	@Override
	public void handlePayload(MemoryGamePayload memoryGamePayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			context.client().options.hudHidden = memoryGamePayload.status();
			if (memoryGamePayload.status()) {
				MemoryGameKeyboardHandler.addPlayer(context.player());
			}
			else {
				MemoryGameKeyboardHandler.removePlayer(context.player());
			}
		});
	}
}
