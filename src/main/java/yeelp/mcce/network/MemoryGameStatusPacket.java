package yeelp.mcce.network;

import net.minecraft.util.Identifier;

public final class MemoryGameStatusPacket extends StatusPacket {

	public MemoryGameStatusPacket(boolean status) {
		super(status);
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.MEMORY_GAME_STATUS_PACKET_ID;
	}
}
