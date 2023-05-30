package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ItemEvaporationEffect;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	private int pickupDelay;
	
	@ModifyVariable(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("STORE"), ordinal = 0)
	private ItemStack modifyItemPickup(ItemStack stack, PlayerEntity player) {
		if(this.shouldEvaporate(player)) {
			return ItemEvaporationEffect.getEvaporatedStack(stack);
		}
		return stack;
	}

	private boolean shouldEvaporate(PlayerEntity player) {
		return this.pickupDelay == 0 && MCCEAPI.accessor.isChaosEffectActive(player, ItemEvaporationEffect.class);
	}
}
