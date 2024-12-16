package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public final class ShakeweightEffect extends AbstractIntervalTriggeredChaosEffect {

    private static final int DURATION = 200, INTERVAL_MIN = 3, INTERVAL_MAX = 6, TRIGGERS_MIN = 6, TRIGGERS_MAX = 10;
    private static final double SHAKE_STRENGTH = 6.0;
    private static final int THROW_UP_WHEN_ON_GROUND_GRACE_PERIOD = 20;
    private static final float THROW_PITCH = 0.5f, SHAKE_PITCH = 0.8f;
    private static final double THROW_STRENGTH_MIN = 6, THROW_STRENGTH_MAX = 10;

    private static final Vec3d UP = new Vec3d(0, SHAKE_STRENGTH, 0), DOWN = UP.negate();

    public ShakeweightEffect() {
        super(DURATION, DURATION, INTERVAL_MIN, INTERVAL_MAX, AbstractLastingChaosEffect.getIntInRange(TRIGGERS_MIN, TRIGGERS_MAX));
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        if(MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.TO_THE_MOON)) {
            return false;
        }
        if(!player.isOnGround()) {
            return true;
        }
        BlockPos pos = player.getBlockPos();
        World world = player.getWorld();
        int i;
        for(i = 0; i++ < 3 && world.isAir(pos); pos = pos.up());
        return i >= 3;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        this.trigger();
        Vec3d velocity = player.getVelocity();
        if(velocity.y > 0) {
            player.addVelocityInternal(DOWN);
        }
        else {
            player.addVelocityInternal(UP);
        }
        if(this.getTriggersRemaining() == 0) {
            player.addVelocityInternal(UP);
        }
        player.velocityModified = true;
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.SHAKE, SHAKE_PITCH, 1.0f)::send);
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        super.tickAdditionalEffectLogic(player);
        if(player.isOnGround() && this.getDurationUntilNextActivation() > THROW_UP_WHEN_ON_GROUND_GRACE_PERIOD) {
            player.addVelocityInternal(UP);
            player.velocityModified = true;
        }
        if(this.getTriggersRemaining() == 0) {
            this.setDuration(1);
        }
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        super.onEffectEnd(player);
        Vec3d direction = new Vec3d(MathHelper.sin(ChaosLib.convertToRadians(player.getYaw())), 0, -MathHelper.cos(ChaosLib.convertToRadians(player.getYaw()))).multiply(this.getRNG().nextDouble(THROW_STRENGTH_MIN, THROW_STRENGTH_MAX)).multiply(this.getRNG().nextBoolean() ? 1 : -1).add(0, Math.PI/2, 0);
        player.addVelocityInternal(direction);
        player.velocityModified = true;
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.KNOCKBACK_ID, THROW_PITCH,1.0f)::send);
    }

    @Override
    public String getName() {
        return "shakeweight";
    }

    @Override
    public void registerCallbacks() {
        //no callbacks
    }
}
