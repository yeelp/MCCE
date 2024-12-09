package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.RainbowGuiHandler;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.RainbowStatusPayload;

public final class RainbowStatusPacketReceiver implements ClientPacketReceiver<RainbowStatusPayload> {

	@Override
	public void handlePayload(RainbowStatusPayload rainbowStatusPayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (rainbowStatusPayload.status()) {
				RainbowGuiHandler.addPlayer(context.player());
			}
			else {
				RainbowGuiHandler.removePlayer(context.player());
			}
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.RAINBOW_STATUS_PACKET_ID;
	}

}
