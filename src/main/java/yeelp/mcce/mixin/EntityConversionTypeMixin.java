package yeelp.mcce.mixin;

import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.conversion.EntityConversionType;
import net.minecraft.entity.mob.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.api.MCCEAPI;

@Mixin(EntityConversionType.class)
public class EntityConversionTypeMixin {

    @Inject(method = "copyData(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/conversion/EntityConversionContext;)V", at = @At("HEAD"))
    private void copyData(MobEntity oldEntity, MobEntity newEntity, EntityConversionContext ctx, CallbackInfo info) {
        boolean shouldCopy = false;
        if(oldEntity instanceof SlimeEntity && newEntity instanceof SlimeEntity) {
            shouldCopy = ctx.type() == EntityConversionType.SPLIT_ON_DEATH;
        }
        else if((oldEntity instanceof AbstractPiglinEntity && newEntity instanceof ZombifiedPiglinEntity) || (oldEntity instanceof HoglinEntity && newEntity instanceof ZoglinEntity)) {
            shouldCopy = ctx.type() == EntityConversionType.SINGLE;
        }
        if(shouldCopy) {
            MCCEAPI.mutator.copyDespawnTimerTo(oldEntity, newEntity);
        }
    }
}
