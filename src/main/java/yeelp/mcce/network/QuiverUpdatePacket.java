package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPackets.Packet;
import yeelp.mcce.network.NetworkingPackets.PacketDecoder;

public class QuiverUpdatePacket implements Packet {

	private final float pitch, yaw;
	
	public QuiverUpdatePacket(float pitch, float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	@Override
	public PacketByteBuf getPacketData() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeFloat(this.pitch);
		buf.writeFloat(this.yaw);
		return buf;
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.QUIVER_UPDATE_PACKET_ID;
	}
	
	
	public float getPitch() {
		return this.pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public static final class QuiverStatusUpdatePacketDecoder implements PacketDecoder<QuiverUpdatePacket> {

		private static QuiverUpdatePacket.QuiverStatusUpdatePacketDecoder instance;
		
		@Override
		public QuiverUpdatePacket decodePacket(PacketByteBuf buf) {
			return new QuiverUpdatePacket(buf.readFloat(), buf.readFloat());
		}
		
		public static QuiverUpdatePacket.QuiverStatusUpdatePacketDecoder getInstance() {
			return instance == null ? instance = new QuiverUpdatePacket.QuiverStatusUpdatePacketDecoder() : instance;
		}
	}

}
