package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public final class PingPongEffect extends AbstractIntervalTriggeredChaosEffect {

    private static final int DURATION_MIN = 800, DURATION_MAX = 1600, INTERVAL_MIN = 2, INTERVAL_MAX = 6, TRIGGERS_MIN = 6, TRIGGERS_MAX = 10;
    private static final double STRENGTH_MIN = 7.5, STRENGTH_MAX = 12, Y_DIRECTION_MIN = 0.15, Y_DIRECTION_MAX = 1;
    private static final float PITCH_PING = 1.4f, PITCH_PONG = 0.6f, END_CHANCE = 0.33f;
    private static final String BACKWARDS_KEY = "shouldKnockBackwards";
    private boolean shouldKnockBackwards;

    public PingPongEffect() {
        super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX, AbstractLastingChaosEffect.getIntInRange(TRIGGERS_MIN, TRIGGERS_MAX));
        this.shouldKnockBackwards = this.getRNG().nextBoolean();
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        this.trigger();
        double dy = player.getVelocity().y;
        Vec3d direction = new Vec3d(-MathHelper.sin(ChaosLib.convertToRadians(player.getYaw())), 0, MathHelper.cos(ChaosLib.convertToRadians(player.getYaw())));
        if(this.shouldKnockBackwards) {
            direction = direction.multiply(-1);
        }
        direction = direction.multiply(this.getRNG().nextDouble(STRENGTH_MIN, STRENGTH_MAX)).add(0, dy <= Y_DIRECTION_MIN ? this.getRNG().nextDouble(Y_DIRECTION_MIN, Y_DIRECTION_MAX) : 0, 0);
        player.addVelocityInternal(direction);
        player.velocityModified = true;
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.BOOP, this.shouldKnockBackwards ? PITCH_PONG : PITCH_PING, 1.0f)::send);
        this.shouldKnockBackwards = !this.shouldKnockBackwards;
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        super.tickAdditionalEffectLogic(player);
        if(this.getTriggersRemaining() <= 0 && this.getRNG().nextFloat() < END_CHANCE) {
            this.setDuration(1);
        }
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.putBoolean(BACKWARDS_KEY, this.shouldKnockBackwards);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.shouldKnockBackwards = nbt.getBoolean(BACKWARDS_KEY);
    }

    @Override
    public String getName() {
        return "pingpong";
    }

    @Override
    public void registerCallbacks() {
        //nothing
    }
}
