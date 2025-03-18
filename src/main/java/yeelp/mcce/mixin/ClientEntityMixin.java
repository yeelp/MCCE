package yeelp.mcce.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yeelp.mcce.model.chaoseffects.ClippyEffect;
import yeelp.mcce.model.chaoseffects.InverseEffect;
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

    @ModifyVariable(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Vec3d alterMovement(Vec3d input, MovementType type, @SuppressWarnings("unused") Vec3d moveInput) {
        Entity entity = (Entity) (Object) this;
        Vec3d vec = input;
        if(!(entity instanceof PlayerEntity player)) {
            return input;
        }
        ClippyEffect.setClippyState(player);
        if(type == MovementType.PISTON || type == MovementType.SELF) {
            vec = InverseEffect.invertMovement(player, input);
        }
        return vec;
    }
}
