package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPackets.Packet;
import yeelp.mcce.network.NetworkingPackets.PacketDecoder;

public abstract class StatusPacket implements Packet {

	private final boolean status;
	
	protected StatusPacket(boolean status) {
		this.status = status;
	}
	
	@Override
	public PacketByteBuf getPacketData() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(this.status);
		return buf;
	}
	
	public boolean getStatus() {
		return this.status;
	}

	public static final class StatusPacketDecoder implements PacketDecoder<StatusPacket> {

		private static StatusPacket.StatusPacketDecoder instance;
		
		@Override
		public StatusPacket decodePacket(PacketByteBuf buf) {
			return new StatusPacket(buf.readBoolean()) {
				@Override
				public Identifier getID() {
					return null;
				}
			};
		}
		
		public static StatusPacket.StatusPacketDecoder getInstance() {
			return instance == null ? instance = new StatusPacket.StatusPacketDecoder() : instance;
		}
	}
}
