package yeelp.mcce.mixin;

import com.google.common.base.Predicates;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.ModifyBlockDrops;
import yeelp.mcce.event.OnBlockPlaceCallback;
import yeelp.mcce.model.chaoseffects.ChaosEffects;
import yeelp.mcce.util.PlayerUtils;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {

	@Unique
	private static final Predicate<PlayerEntity> BOUNCY_ACTIVE = (p) -> MCCEAPI.accessor.isChaosEffectActive(p, ChaosEffects.BOUNCY);
	@Unique
	private static final Predicate<PlayerEntity> BOUNCY_CHECK = BOUNCY_ACTIVE.and((p) -> p.fallDistance > 0.125f).and(Predicates.not(playerEntity -> playerEntity != null && playerEntity.isSneaking()));
	
	@SuppressWarnings("unused")
    public BlockMixin(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("static-method")
	@Inject(method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", shift = Shift.AFTER), cancellable = true)
	private void modifyBlockDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool, CallbackInfo info) {
		if(ModifyBlockDrops.EVENT.invoker().changeBlockDrops(world, player, pos, state, blockEntity, tool)) {
			info.cancel();
		}
	}
	
	@SuppressWarnings("static-method")
	@Inject(method = "onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void modifyLanding(@SuppressWarnings("unused") BlockView world, Entity entity, CallbackInfo info) {
		getPlayerIfBouncy(entity).ifPresent((p) -> {
			PlayerUtils.updatePlayerVelocity(p, p.getVelocity().multiply(1.0, -1.0, 1.0));
			info.cancel();
		});
	}
	
	@SuppressWarnings("static-method")
	@Inject(method = "onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;D)V", at = @At("HEAD"), cancellable = true)
	private void preventFallDamage(@SuppressWarnings("unused") World world, @SuppressWarnings("unused") BlockState state, @SuppressWarnings("unused") BlockPos pos, Entity entity, @SuppressWarnings("unused") double fallDistance, CallbackInfo info) {
		if(getPlayerIfBouncy(entity).isPresent()) {
			info.cancel();
		}
	}

	@Inject(method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private void onPlace(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, CallbackInfo info) {
		if(OnBlockPlaceCallback.EVENT.invoker().onBlockPlace(world, pos, state, placer, stack).getCancelState() == CancelState.CANCEL) {
			info.cancel();
		}
	}
	
	@Unique
	private static Optional<PlayerEntity> getPlayerIfBouncy(Entity entity) {
		return Optional.of(entity).filter(ServerPlayerEntity.class::isInstance).map(PlayerEntity.class::cast).filter(BOUNCY_CHECK);
	}
}
