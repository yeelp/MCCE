package yeelp.mcce.mixin;

import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.conversion.EntityConversionType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.model.DespawnTimer;
import yeelp.mcce.model.ServerState;

import java.util.UUID;

@Mixin(EntityConversionType.class)
public class EntityConversionTypeMixin {

    @Inject(method = "copyData(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/conversion/EntityConversionContext;)V", at = @At("HEAD"))
    private void copyData(MobEntity oldEntity, MobEntity newEntity, EntityConversionContext ctx, CallbackInfo info) {
        if(oldEntity instanceof SlimeEntity && newEntity instanceof SlimeEntity && ctx.type() == EntityConversionType.SPLIT_ON_DEATH) {
            MinecraftServer server = oldEntity.getServer();
            ServerState state = server != null ? ServerState.getServerState(server) : null;
            UUID uuid = oldEntity.getUuid();
            DespawnTimer timer;
            if(state != null && state.hasDespawnTimer(uuid) && (timer = state.getDespawnTimer(uuid)).isSet()) {
                state.getDespawnTimer(newEntity.getUuid()).setTimer(timer.getTimeRemaining());
                state.markDirty();
            }
        }
    }
}
