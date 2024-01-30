package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import yeelp.mcce.model.chaoseffects.SluggishEffect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getJumpBoostVelocityModifier()F", at = @At("RETURN"), cancellable = true)
	private void getJumpBoostVelocityModifier(CallbackInfoReturnable<Float> info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if(!(entity instanceof PlayerEntity)) {
			return;
		}
		if(SluggishEffect.isAffected((PlayerEntity) entity)) {
			info.setReturnValue(-0.3f + 0.2f * info.getReturnValueF());
		}
	}
}
