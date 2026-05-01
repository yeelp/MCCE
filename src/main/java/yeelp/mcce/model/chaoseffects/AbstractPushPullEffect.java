package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import org.joml.Vector3d;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.SimpleUtil;

import java.util.EnumSet;

public abstract class AbstractPushPullEffect extends SimpleTimedChaosEffect {
    private static final int DURATION_MIN = 1200, DURATION_MAX = 1500;
    private static final double SPEED = 0.3;
    private static final int RADIUS = 40;
    private static final float APPLY_CHANCE = 0.75f;

    protected AbstractPushPullEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    protected abstract float positionMultiplier();

    @Override
    public final void applyEffect(PlayerEntity player) {
        Vector3d playerPos = new Vector3d(new double[] {player.getX(), player.getY(), player.getZ()});
        player.getEntityWorld().getEntitiesByClass(Entity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> !(entity instanceof PlayerEntity) && entity.getPassengerList().isEmpty()).forEach((entity) -> {
            Vector3d entityPos = new Vector3d(new double[] {entity.getX(), entity.getY(), entity.getZ()});
            Vector3d heading = new Vector3d();
            playerPos.sub(entityPos, heading).normalize(SPEED);
            entityPos.add(heading.mul(this.positionMultiplier()));
            entity.teleport(SimpleUtil.getServerWorldFromEntity(entity), entityPos.x, entityPos.y, entityPos.z, EnumSet.noneOf(PositionFlag.class), 0.0f, 0.0f, false);
        });
    }

    @Override
    protected final boolean canStack() {
        return false;
    }

    @Override
    protected final boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return Math.random() < APPLY_CHANCE && MCCEAPI.accessor.getChaosEffect(player, AbstractPushPullEffect.class).isEmpty();
    }
}
