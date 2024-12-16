package yeelp.mcce.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yeelp.mcce.model.chaoseffects.LookInversionEffect;

@Mixin(Entity.class)
public class ClientEntityMixin {

    @ModifyVariable(method = "changeLookDirection(DD)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double alterLookDeltaX(double input, double deltaX, double deltaY) {
        return LookInversionEffect.changeInput((Entity) (Object) this, input);
    }

    @ModifyVariable(method = "changeLookDirection(DD)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double alterLookDeltaY(double input, double deltaX, double deltaY) {
        return LookInversionEffect.changeInput((Entity) (Object) this, input);
    }
}
