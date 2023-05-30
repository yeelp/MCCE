package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class NetworkingPackets {
	
	private NetworkingPackets() {
		//not to be initialized
	}
	
	public interface Packet {
		PacketByteBuf getPacketData();
		
		Identifier getID();
		
		default void sendPacket(ServerPlayerEntity player) {
			ServerPlayNetworking.send(player, this.getID(), this.getPacketData());
		}
	}

	public interface PacketDecoder<V extends Packet> {
		V decodePacket(PacketByteBuf buf);
	}
}
