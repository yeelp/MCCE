package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public class RaveEffect extends SimpleTimedChaosEffect {

    private static final int DURATION_MIN = 72*20, DURATION_MAX = 144*20;
    private static final int RADIUS = 15;

    public RaveEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SIMON_SAYS, ChaosEffects.PRESS_L_TO_LEVITATE, ChaosEffects.CLIPPY, ChaosEffects.SMACK_DOWN);
    }


    @Override
    public void applyEffect(PlayerEntity player) {
        player.getEntityWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), LivingEntity::isOnGround).forEach(LivingEntity::jump);
        if(player.isOnGround()) {
            PlayerUtils.addPlayerVelocity(player, new Vec3d(0.0, 0, 0.0));
            player.setJumping(true);
        }
    }

    @Override
    public String getName() {
        return "rave";
    }
}
