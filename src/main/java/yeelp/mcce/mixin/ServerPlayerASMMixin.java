package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerASMMixin {
	@Accessor("syncedHealth")
    void setSyncedHealth(float health);
	
	@Accessor("syncedFoodLevel")
    void setSyncedFoodLevel(int foodLevel);
	
	@Accessor("syncedSaturationIsZero")
    void setSyncedSaturationIsZero(boolean syncedSaturationIsZero);
}
