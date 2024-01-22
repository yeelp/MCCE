package yeelp.mcce.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityLike;
import yeelp.mcce.model.chaoseffects.ClippyEffect;
import yeelp.mcce.model.chaoseffects.InverseEffect;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
	
	private static final Map<UUID, Vec3d> MOVEMENT_TRACKER = Maps.newHashMap();
	
	@SuppressWarnings("resource")
	@ModifyVariable(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), ordinal = 0)
	private Vec3d alterMovement(Vec3d input, MovementType type, @SuppressWarnings("unused") Vec3d moveInput) {
		Entity entity = (Entity) (Object) this;
		if(!(entity instanceof PlayerEntity)) {
			return input;
		}
		PlayerEntity player = (PlayerEntity) entity;
		if(ClippyEffect.isAffected(player)) {
			player.noClip = true;
			((EntityASMMixin) entity).setOnGround(false);
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
