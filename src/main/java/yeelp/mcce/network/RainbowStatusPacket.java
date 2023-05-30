package yeelp.mcce.network;

import net.minecraft.util.Identifier;

public final class RainbowStatusPacket extends StatusPacket {

	public RainbowStatusPacket(boolean status) {
		super(status);
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.RAINBOW_STATUS_PACKET_ID;
	}

}
