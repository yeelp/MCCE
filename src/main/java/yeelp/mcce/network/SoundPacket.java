package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPackets.Packet;
import yeelp.mcce.network.NetworkingPackets.PacketDecoder;

/**
 * Packet for sending sound data, with pitch and volume
 * @author Yeelp
 *
 */
public final class SoundPacket implements Packet {

	private byte id;
	private float pitch, volume;

	public SoundPacket(byte id, float pitch, float volume) {
		this.id = id;
		this.pitch = pitch;
		this.volume = volume;
	}
	
	@Override
	public Identifier getID() {
		return NetworkingConstants.SoundPacketConstants.SOUND_PACKET_ID;
	}

	@Override
	public PacketByteBuf getPacketData() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeByte(this.id).writeFloat(this.pitch).writeFloat(this.volume);
		return buf;
	}

	public byte getSoundId() {
		return this.id;
	}

	public float getPitch() {
		return this.pitch;
	}

	public float getVolume() {
		return this.volume;
	}

	public static final class SoundPacketDecoder implements PacketDecoder<SoundPacket> {

		private static SoundPacket.SoundPacketDecoder instance;
		
		@Override
		public SoundPacket decodePacket(PacketByteBuf buf) {
			return new SoundPacket(buf.readByte(), buf.readFloat(), buf.readFloat());
		}
		
		public static SoundPacket.SoundPacketDecoder getInstance() {
			return instance == null ? instance = new SoundPacket.SoundPacketDecoder() : instance;
		}
	}

}
