package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
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
        return player.getWorld().getRegistryKey() == World.END;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        ChaosLib.forEachPos(player.getBlockPos().up(3).north().west(), player.getBlockPos().up(5).south().east(), (pos) -> ChaosLib.setToAir(player.getWorld(), pos));
        player.getWorld().getEntitiesByClass(EndermanEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), Predicates.alwaysTrue()).forEach((enderman) -> enderman.setAngryAt(player.getUuid()));
    }

    @Override
    public String getName() {
        return "enderanger";
    }
}
