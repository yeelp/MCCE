package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.event.OnBlockBreakingCallback;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Inject(at = @At("HEAD"), method = "setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V")
	private void onSetBlockBreaking(int entityId, BlockPos pos, int progress, @SuppressWarnings("unused") CallbackInfo info) {
		OnBlockBreakingCallback.EVENT.invoker().onBlockBreaking((ServerWorld) (Object) this, entityId, pos, progress);
	}
}
