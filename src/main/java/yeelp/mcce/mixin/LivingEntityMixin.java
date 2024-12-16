package yeelp.mcce.mixin;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.TiltScreenCallback;
import yeelp.mcce.model.chaoseffects.ChaosEffects;
import yeelp.mcce.model.chaoseffects.SimonSaysEffect;
import yeelp.mcce.model.chaoseffects.SluggishEffect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@SuppressWarnings("MagicNumber")
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

	@Inject(method = "jump()V", at = @At("HEAD"))
	private void onJump(CallbackInfo info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if(entity instanceof ServerPlayerEntity player && MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SIMON_SAYS)) {
			SimonSaysEffect.trackJump(player);
		}
	}

	@Inject(method = "tiltScreen(DD)V", at = @At("HEAD"), cancellable = true)
	private void onScreenTilt(double deltaX, double deltaY, CallbackInfo info) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity instanceof PlayerEntity player && TiltScreenCallback.EVENT.invoker().shouldAllowTilt(player, deltaX, deltaY).getCancelState() == CancelState.CANCEL) {
			info.cancel();
		}
	}
}
