package yeelp.mcce.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.ModifyBlockDrops;
import yeelp.mcce.model.chaoseffects.BouncyEffect;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
	
	public BlockMixin(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("static-method")
	@Inject(method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", shift = At.Shift.AFTER), cancellable = true)
	private void modifyBlockDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool, CallbackInfo info) {
		if(ModifyBlockDrops.EVENT.invoker().changeBlockDrops(world, player, pos, state, blockEntity, tool)) {
			info.cancel();
		}
	}
	
	@SuppressWarnings("static-method")
	@Inject(method = "onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void modifyLanding(@SuppressWarnings("unused") BlockView world, Entity entity, CallbackInfo info) {
		if(!(entity instanceof ServerPlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;
		if(player.fallDistance > 0.125f && !player.isSneaking() && MCCEAPI.accessor.isChaosEffectActive(player, BouncyEffect.class)) {
			player.setVelocity(player.getVelocity().multiply(1.0, -1.0, 1.0));
			player.velocityModified = true;
			info.cancel();
		}
	}
	
	@SuppressWarnings("static-method")
	@Inject(method = "onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
	private void preventFallDamage(@SuppressWarnings("unused") World world, @SuppressWarnings("unused") BlockState state, @SuppressWarnings("unused") BlockPos pos, Entity entity, @SuppressWarnings("unused") float fallDistance, CallbackInfo info) {
		if(!(entity instanceof ServerPlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) entity;
		if(player.fallDistance > 0.125f && !player.isSneaking() && MCCEAPI.accessor.isChaosEffectActive(player, BouncyEffect.class)) {
			info.cancel();
		}
	}
}
