package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.util.ChaosLib;

public final class EnderAngerEffect extends SimpleTimedChaosEffect {

    private static final int DURATION_MIN = 800, DURATION_MAX = 1200, RADIUS = 64;

    public EnderAngerEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return player.getEntityWorld().getRegistryKey() == World.END;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        ChaosLib.forEachPos(player.getBlockPos().up(2).north().west(), player.getBlockPos().up(5).south().east(), (pos) -> ChaosLib.setToAir(player.getEntityWorld(), pos));
        player.getEntityWorld().getEntitiesByClass(EndermanEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), Predicates.alwaysTrue()).forEach((enderman) -> {
            enderman.setAngryAt(LazyEntityReference.ofUUID(player.getUuid()));
            enderman.setTarget(player);
        });
    }

    @Override
    public String getName() {
        return "enderanger";
    }

    @Override
    public String getDisplayName() {
        return "Ender Anger";
    }
}
