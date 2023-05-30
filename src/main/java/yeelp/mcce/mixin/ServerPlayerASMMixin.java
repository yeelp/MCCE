package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerASMMixin {
	@Accessor("syncedHealth")
	public void setSyncedHealth(float health);
	
	@Accessor("syncedFoodLevel")
	public void setSyncedFoodLevel(int foodLevel);
	
	@Accessor("syncedSaturationIsZero")
	public void setSyncedSaturationIsZero(boolean syncedSaturationIsZero);
}
