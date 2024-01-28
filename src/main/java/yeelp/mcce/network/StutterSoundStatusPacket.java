package yeelp.mcce.network;

import net.minecraft.util.Identifier;

public class StutterSoundStatusPacket extends StatusPacket {

	public StutterSoundStatusPacket(boolean status) {
		super(status);
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.STUTTER_SOUND_STATUS_PACKET_ID;
	}

}
