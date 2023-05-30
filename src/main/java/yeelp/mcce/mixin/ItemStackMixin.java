package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.model.chaoseffects.GlintEffect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {

	@Inject(method = "hasGlint()Z", at = @At("HEAD"), cancellable = true)
	private void hasGlint(CallbackInfoReturnable<Boolean> info) {
		NbtCompound nbt = ((ItemStack) (Object) this).getNbt();
		if(nbt != null && nbt.getByte(GlintEffect.GLINT_TAG) == GlintEffect.GLINT.byteValue()) {
			info.setReturnValue(true);
		}
	}
}
