package yeelp.mcce.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.event.OnPlayerDeathCallback;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo ci) {
        OnPlayerDeathCallback.EVENT.invoker().onDeath((ServerPlayerEntity) (Object) this, source);
    }
}
