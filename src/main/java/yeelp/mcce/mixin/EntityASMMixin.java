package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityASMMixin {
	
	@Accessor("onGround")
	public void setOnGround(boolean onGround);
}
