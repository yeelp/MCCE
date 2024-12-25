package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public final class CullEffect extends AbstractInstantChaosEffect {

    private static final int RADIUS = 64;
    private static final float PITCH_MIN = 0.5f, PITCH_MAX = 1.5f;
    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        player.getWorld().getEntitiesByClass(Entity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> !(entity instanceof PlayerEntity) && !(entity instanceof EnderDragonEntity)).forEach(Entity::discard);
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.WITHER_SPAWN, this.getRNG().nextFloat(PITCH_MIN, PITCH_MAX), 1.0f)::send);
    }

    @Override
    public String getName() {
        return "cull";
    }
}
