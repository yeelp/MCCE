package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.ParticlePacket;

public class ParticlePacketReceiver implements ClientPacketReceiver {

	@Override
	public void handlePacket(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		ParticlePacket packet = ParticlePacket.ParticlePacketDecoder.getInstance().decodePacket(buf);
		client.execute(() -> client.world.addParticle(NetworkingConstants.ParticlePacketConstants.getParticle(packet.getId()), packet.getX(), packet.getY(), packet.getZ(), packet.getDx(), packet.getDy(), packet.getDz()));
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.ParticlePacketConstants.PARTICLE_PACKET_ID;
	}

}
