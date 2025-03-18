package yeelp.mcce.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.model.ServerState;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {

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
}
