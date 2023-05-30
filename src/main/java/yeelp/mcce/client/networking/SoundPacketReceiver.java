package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;

public final class SoundPacketReceiver implements ClientPacketReceiver {


	@Override
	public Identifier getID() {
		return NetworkingConstants.SoundPacketConstants.SOUND_PACKET_ID;
	}

	@Override
	public void handlePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		SoundPacket packet = SoundPacket.SoundPacketDecoder.getInstance().decodePacket(buf);
		client.execute(() -> client.player.playSound(NetworkingConstants.SoundPacketConstants.getSound(packet.getSoundId()), packet.getVolume(), packet.getPitch()));
	}

}
