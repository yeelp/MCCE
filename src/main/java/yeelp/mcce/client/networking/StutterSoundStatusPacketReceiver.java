package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.StutterSoundSoundHandler;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.StutterSoundStatusPayload;

public final class StutterSoundStatusPacketReceiver implements ClientPacketReceiver<StutterSoundStatusPayload> {

	@Override
	public void handlePayload(StutterSoundStatusPayload stutterSoundStatusPayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			if (stutterSoundStatusPayload.status()) {
				StutterSoundSoundHandler.addPlayer(context.player());
			}
			else {
				StutterSoundSoundHandler.removePlayer(context.player());
			}
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.STUTTER_SOUND_STATUS_PACKET_ID;
	}

}
