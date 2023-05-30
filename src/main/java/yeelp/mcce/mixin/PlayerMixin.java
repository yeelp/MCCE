package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.PlayerHurtCallback;
import yeelp.mcce.event.PlayerTickCallback;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void tick(@SuppressWarnings("unused") CallbackInfo info) {
		PlayerTickCallback.EVENT.invoker().tick((PlayerEntity) (Object) this);
	}
	
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	private void applyDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(PlayerHurtCallback.EVENT.invoker().onHurt((PlayerEntity) (Object) this, source, amount).getCancelState() == CancelState.CANCEL) {
			info.setReturnValue(false);
		}
	}
}
