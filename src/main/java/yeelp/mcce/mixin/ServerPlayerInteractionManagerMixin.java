package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
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
import yeelp.mcce.event.OnBlockInteractCallback;

@Mixin(ServerPlayerInteractionManager.class)
public final class ServerPlayerInteractionManagerMixin {
	
	@SuppressWarnings("static-method")
	@Inject(at = @At("HEAD"), method = "interactBlock(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
	private void onBlockInteract(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, @SuppressWarnings("unused") CallbackInfoReturnable<ActionResult> info) {
		OnBlockInteractCallback.EVENT.invoker().onBlockInteract(player, world, stack, hand, hitResult);
	}
}
