package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;

public final class SoundPacketReceiver implements ClientPacketReceiver<SoundPayload> {

	@Override
	public void handlePayload(SoundPayload soundPayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> context.player().playSound(NetworkingConstants.SoundPacketConstants.getSound(soundPayload.id()), soundPayload.volume(), soundPayload.pitch()));
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.SoundPacketConstants.SOUND_PACKET_ID;
	}

}
