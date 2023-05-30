package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.QuiverUpdatePacket;

public class QuiverUpdatePacketReceiver implements ClientPacketReceiver {

	@Override
	public void handlePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		QuiverUpdatePacket packet = QuiverUpdatePacket.QuiverStatusUpdatePacketDecoder.getInstance().decodePacket(buf);
		client.execute(() -> {
			client.player.setPitch(client.player.getPitch() + packet.getPitch());
			client.player.setYaw(client.player.getYaw() + packet.getYaw());
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.QUIVER_UPDATE_PACKET_ID;
	}

}
