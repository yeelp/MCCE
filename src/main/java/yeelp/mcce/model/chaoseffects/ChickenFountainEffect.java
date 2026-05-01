package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.EntityInitialUpdatePacket;
import yeelp.mcce.network.EntityInitialUpdatePacket.UpdateAction;
import yeelp.mcce.util.MCCESpawnCap;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.SimpleUtil;

public final class ChickenFountainEffect extends SimpleTimedChaosEffect {

    private static final int DURATION_MIN = 500, DURATION_MAX = 1000;
    private static final double HORIZONTAL_VELOCITY_BOUND = 0.6, VERTICAL_VELOCITY_BOUND = 0.7;

    public ChickenFountainEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        ServerWorld world = SimpleUtil.getServerWorldFromEntity(player);
        ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
        world.getRegistryManager().getOrThrow(RegistryKeys.CHICKEN_VARIANT).getRandom(world.getRandom()).ifPresent(chicken::setVariant);
        chicken.refreshPositionAndAngles(player.getX(), player.getEyeY(), player.getZ(), 0.0f, 0.0f);
        chicken.setVelocity(this.getRNG().nextDouble(-HORIZONTAL_VELOCITY_BOUND, HORIZONTAL_VELOCITY_BOUND), this.getRNG().nextDouble(0, VERTICAL_VELOCITY_BOUND), this.getRNG().nextDouble(-HORIZONTAL_VELOCITY_BOUND, HORIZONTAL_VELOCITY_BOUND));
        chicken.noClip = true;
        MCCEAPI.mutator.setDespawnTimer(chicken, 10);
        MCCESpawnCap.MOB.attemptEntitySpawn(world, chicken);
        PlayerUtils.getServerPlayer(player).ifPresent(new EntityInitialUpdatePacket(chicken.getUuid(), UpdateAction.SET_NO_CLIP)::send);
    }

    @Override
    public String getName() {
        return "chickenfountain";
    }

    @Override
    public String getDisplayName() {
        return "Chicken Fountain";
    }
}
