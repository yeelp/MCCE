package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public final class SoundEffect extends AbstractInstantChaosEffect {

    private static final byte[] VALID_SOUNDS = {
            SoundPacketConstants.BASALT_DELTAS_ADDITIONS,
            SoundPacketConstants.ARROW_HIT_PLAYER,
            SoundPacketConstants.BELL_RESONATE,
            SoundPacketConstants.BLAZE_AMBIENT,
            SoundPacketConstants.CHEST_LOCKED,
            SoundPacketConstants.CREEPER_PRIMED,
            SoundPacketConstants.DRINK_HONEY,
            SoundPacketConstants.EVOKER_WOLOLO,
            SoundPacketConstants.FIREWORK_TWINKLE,
            SoundPacketConstants.FOX_AMBIENT,
            SoundPacketConstants.GHAST_AMBIENT,
            SoundPacketConstants.GOAT_HORN,
            SoundPacketConstants.ITEM_BREAK,
            SoundPacketConstants.PHANTOM_AMBIENT,
            SoundPacketConstants.PHANTOM_SWOOP,
            SoundPacketConstants.SCULK_SENSOR,
            SoundPacketConstants.SILVERFISH_AMBIENT,
            SoundPacketConstants.STAL,
            SoundPacketConstants.STRAD,
            SoundPacketConstants.WARD,
            SoundPacketConstants.INVERSE_START,
            SoundPacketConstants.VAULT_ACTIVATE,
            SoundPacketConstants.OMINOUS_SPAWNER,
            SoundPacketConstants.OMINOUS_PREPARE,
            SoundPacketConstants.AMBIENT_CAVE,
            SoundPacketConstants.PLING,
            SoundPacketConstants.COPPER_SPIN,
            SoundPacketConstants.CREAKING_ACTIVATE,
            SoundPacketConstants.ANGRY_PIGLIN,
            SoundPacketConstants.WAX_ON};

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(VALID_SOUNDS[this.getRNG().nextInt(VALID_SOUNDS.length)], 1.0f, 1.0f)::send);
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
