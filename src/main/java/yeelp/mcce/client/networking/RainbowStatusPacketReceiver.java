package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.RainbowGuiHandler;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.StatusPacket;

public final class RainbowStatusPacketReceiver implements ClientPacketReceiver {

	@Override
	public void handlePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		boolean status = StatusPacket.StatusPacketDecoder.getInstance().decodePacket(buf).getStatus();
		client.execute(() -> {
			if(status) {
				RainbowGuiHandler.addPlayer(client.player);
			}
			else {
				RainbowGuiHandler.removePlayer(client.player);
			}
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.RAINBOW_STATUS_PACKET_ID;
	}

}
