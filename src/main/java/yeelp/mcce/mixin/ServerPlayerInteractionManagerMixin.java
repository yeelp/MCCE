package yeelp.mcce.mixin;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import yeelp.mcce.event.BeforePlayerBreakBlockCallback;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.OnBlockInteractCallback;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @SuppressWarnings("static-method")
    @Inject(at = @At("HEAD"), method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
    private void onBlockInteract(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, @SuppressWarnings("unused") CallbackInfoReturnable<ActionResult> info) {
        OnBlockInteractCallback.EVENT.invoker().onBlockInteract(player, world, stack, hand, hitResult);
    }

    @Inject(method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"), cancellable = true)
    private void beforeBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(BeforePlayerBreakBlockCallback.EVENT.invoker().beforeBlockBreak(pos, this.player).getCancelState() == CancelState.CANCEL) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
