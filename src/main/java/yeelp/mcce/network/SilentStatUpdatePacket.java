package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.mixin.ServerPlayerASMMixin;
import yeelp.mcce.network.NetworkingPackets.Packet;
import yeelp.mcce.network.NetworkingPackets.PacketDecoder;

/**
 * A silent update packet for stats. Updates the client's stats without doing
 * any sort of screen tilt for health or updating last damage received. Sending
 * this packet before {@link ServerPlayerEntity} checks if a
 * {@link HealthUpdateS2CPacket} needs to be sent prevents that packet from
 * being sent as this packet does all the updates needed for that check to fail.
 * 
 * @author Yeelp
 *
 */
public class SilentStatUpdatePacket implements Packet {

	private float health, sat;
	private int food;

	public SilentStatUpdatePacket(float health, int food, float sat) {
		this.health = health;
		this.sat = sat;
		this.food = food;
	}
	
	public SilentStatUpdatePacket(PlayerEntity player) {
		this(player.getHealth(), player.getHungerManager().getFoodLevel(), player.getHungerManager().getSaturationLevel());
	}

	public float getHealth() {
		return this.health;
	}

	public float getSat() {
		return this.sat;
	}

	public int getFood() {
		return this.food;
	}

	@Override
	public PacketByteBuf getPacketData() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeFloat(this.health);
		buf.writeVarInt(this.food).writeFloat(this.sat);
		return buf;
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.SILENT_UPDATE_PACKET_ID;
	}

	@Override
	public void sendPacket(ServerPlayerEntity player) {
		Packet.super.sendPacket(player);
		((ServerPlayerASMMixin) player).setSyncedHealth(player.getHealth());
		((ServerPlayerASMMixin) player).setSyncedFoodLevel(player.getHungerManager().getFoodLevel());
		((ServerPlayerASMMixin) player).setSyncedSaturationIsZero(player.getHungerManager().getSaturationLevel() == 0.0f);
	}

	public static final class SilentStatUpdatePacketDecoder implements PacketDecoder<SilentStatUpdatePacket> {

		private static SilentStatUpdatePacket.SilentStatUpdatePacketDecoder instance;

		@Override
		public SilentStatUpdatePacket decodePacket(PacketByteBuf buf) {
			return new SilentStatUpdatePacket(buf.readFloat(), buf.readVarInt(), buf.readFloat());
		}

		public static SilentStatUpdatePacket.SilentStatUpdatePacketDecoder getInstance() {
			return instance == null ? instance = new SilentStatUpdatePacket.SilentStatUpdatePacketDecoder() : instance;
		}

	}

}
