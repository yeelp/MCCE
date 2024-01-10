package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPackets.Packet;
import yeelp.mcce.network.NetworkingPackets.PacketDecoder;

public final class ParticlePacket implements Packet {

	private final byte id;
	private final float x, y, z, dx, dy, dz;
	
	public ParticlePacket(byte id, float x, float y, float z, float dx, float dy, float dz) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	@Override
	public PacketByteBuf getPacketData() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeByte(this.id).writeFloat(this.x).writeFloat(this.y).writeFloat(this.z).writeFloat(this.dx).writeFloat(this.dy).writeFloat(this.dz);
		return buf;
	}

	public byte getId() {
		return this.id;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public float getDx() {
		return this.dx;
	}

	public float getDy() {
		return this.dy;
	}

	public float getDz() {
		return this.dz;
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.ParticlePacketConstants.PARTICLE_PACKET_ID;
	}
	
	public static final class ParticlePacketDecoder implements PacketDecoder<ParticlePacket> {

		private static ParticlePacket.ParticlePacketDecoder instance;
		
		@Override
		public ParticlePacket decodePacket(PacketByteBuf buf) {
			return new ParticlePacket(buf.readByte(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
		}
		
		public static ParticlePacket.ParticlePacketDecoder getInstance() {
			return instance == null ? instance = new ParticlePacket.ParticlePacketDecoder() : instance;
		}
	}

}
