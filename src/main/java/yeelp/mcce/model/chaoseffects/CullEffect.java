package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.Set;

public final class CullEffect extends AbstractInstantChaosEffect {

    private static final int RADIUS = 64;
    private static final float PITCH_MIN = 0.5f, PITCH_MAX = 1.5f;
    private static final Set<Byte> SOUNDS = Sets.newHashSet(SoundPacketConstants.WITHER_SPAWN, SoundPacketConstants.WITHER_DEATH);
    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        player.getWorld().getEntitiesByClass(Entity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> !(entity instanceof PlayerEntity) && !(entity instanceof EnderDragonEntity)).forEach(Entity::discard);
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(ChaosLib.getRandomElementFrom(SOUNDS, this.getRNG()), this.getRNG().nextFloat(PITCH_MIN, PITCH_MAX), 1.0f)::send);
    }

    @Override
    public String getName() {
        return "cull";
    }
}
