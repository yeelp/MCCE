package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public class SoundEffect extends AbstractInstantChaosEffect {

	private static final byte[] VALID_SOUNDS = {
			NetworkingConstants.SoundPacketConstants.BASALT_DELTAS_ADDITIONS,
			NetworkingConstants.SoundPacketConstants.ARROW_HIT_PLAYER,
			NetworkingConstants.SoundPacketConstants.BELL_RESONATE,
			NetworkingConstants.SoundPacketConstants.BLAZE_AMBIENT,
			NetworkingConstants.SoundPacketConstants.CHEST_LOCKED,
			NetworkingConstants.SoundPacketConstants.CREEPER_PRIMED,
			NetworkingConstants.SoundPacketConstants.DRINK_HONEY,
			NetworkingConstants.SoundPacketConstants.EVOKER_WOLOLO,
			NetworkingConstants.SoundPacketConstants.FIREWORK_TWINKLE,
			NetworkingConstants.SoundPacketConstants.FOX_AMBIENT,
			NetworkingConstants.SoundPacketConstants.GHAST_AMBIENT,
			NetworkingConstants.SoundPacketConstants.GOAT_HORN,
			NetworkingConstants.SoundPacketConstants.ITEM_BREAK,
			NetworkingConstants.SoundPacketConstants.PHANTOM_AMBIENT,
			NetworkingConstants.SoundPacketConstants.PHANTOM_SWOOP,
			NetworkingConstants.SoundPacketConstants.SCULK_SENSOR,
			NetworkingConstants.SoundPacketConstants.SILVERFISH_AMBIENT,
			NetworkingConstants.SoundPacketConstants.STAL,
			NetworkingConstants.SoundPacketConstants.STRAD,
			NetworkingConstants.SoundPacketConstants.WARD,
			NetworkingConstants.SoundPacketConstants.INVERSE_START};

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(VALID_SOUNDS[this.getRNG().nextInt(VALID_SOUNDS.length)], 1.0f, 1.0f)::sendPacket);
	}

	@Override
	public String getName() {
		return "sound";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
