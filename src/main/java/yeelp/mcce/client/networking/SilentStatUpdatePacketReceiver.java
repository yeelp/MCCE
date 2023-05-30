package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SilentStatUpdatePacket;

public class SilentStatUpdatePacketReceiver implements ClientPacketReceiver {

	@Override
	public void handlePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		SilentStatUpdatePacket packet = SilentStatUpdatePacket.SilentStatUpdatePacketDecoder.getInstance().decodePacket(buf);
		client.execute(() -> {
			client.player.setHealth(packet.getHealth());
			client.player.getHungerManager().setFoodLevel(packet.getFood());
			client.player.getHungerManager().setSaturationLevel(packet.getSat());
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.SILENT_UPDATE_PACKET_ID;
	}

}
