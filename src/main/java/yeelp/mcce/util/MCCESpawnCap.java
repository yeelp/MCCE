package yeelp.mcce.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.ModConfig;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public enum MCCESpawnCap {
    MOB(LivingEntity.class, ModConfig.getInstance().performance.spawnCaps::getLocalMobCap),
    ITEM(ItemEntity.class, ModConfig.getInstance().performance.spawnCaps::getLocalItemCap),
    FALLING_BLOCK(FallingBlockEntity.class, ModConfig.getInstance().performance.spawnCaps::getLocalFallingBlackCap);

    private final IntSupplier cap;
    private final Class<? extends Entity> clazz;
    MCCESpawnCap(Class<? extends Entity> clazz, IntSupplier cap) {
        this.clazz = clazz;
        this.cap = cap;
    }

    public <E extends Entity> void attemptEntitySpawn(World world, E entity) {
        if(this.clazz.isInstance(entity) && ModConfig.getInstance().performance.enableEntityCaps && world.getEntitiesByClass(this.clazz, ChaosLib.getBoxCenteredOnPosWithRadius(entity.getBlockPos(), ModConfig.getInstance().performance.spawnCaps.getRadius), Predicate.not(PlayerEntity.class::isInstance)).size() > this.cap.getAsInt()) {
            return;
        }
        world.spawnEntity(entity);
    }
}
