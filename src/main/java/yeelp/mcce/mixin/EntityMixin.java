package yeelp.mcce.mixin;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.model.ServerState;
import yeelp.mcce.model.chaoseffects.ClippyEffect;
import yeelp.mcce.model.chaoseffects.InverseEffect;

import java.util.Map;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
	
	@Unique
	private static final Map<UUID, Vec3d> MOVEMENT_TRACKER = Maps.newHashMap();
	
	@Inject(at = @At("HEAD"), method = "tick()V")
	private void tick(@SuppressWarnings("unused") CallbackInfo info) {
		EntityTickCallback.EVENT.invoker().tick((Entity) (Object) this);
	}
	
	@Inject(at = @At("HEAD"), method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V")
	private void remove(@SuppressWarnings("unused") RemovalReason reason, @SuppressWarnings("unused") CallbackInfo info) {
		Entity entity = (Entity) (Object) this;
		if(entity instanceof PlayerEntity) {
			return;
		}
		MinecraftServer server = entity.getServer();
		if(server != null) {
			ServerState state = ServerState.getServerState(server);
			state.removeTimer(entity.getUuid());
			state.markDirty();
		}
	}
	
	@ModifyVariable(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private Vec3d alterMovement(Vec3d input, MovementType type, @SuppressWarnings("unused") Vec3d moveInput) {
		Entity entity = (Entity) (Object) this;
		if(!(entity instanceof PlayerEntity player)) {
			return input;
		}
        if(ClippyEffect.isAffected(player)) {
			((EntityASMMixin) entity).setOnGround(false);				
			player.noClip = true;
		}
		if(type == MovementType.PISTON || type == MovementType.SELF) {
			if(InverseEffect.isAffected(player)) {
				Vec3d newInput = input.multiply(-1, 1, -1);
				if(entity.getWorld().isClient) {
					MOVEMENT_TRACKER.put(entity.getUuid(), newInput);
					return newInput;
				}
				if((newInput = MOVEMENT_TRACKER.getOrDefault(entity.getUuid(), input)).equals(input)) {
					return newInput;
				}		
			}			
		}
		return input;
	}
}
