package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.ClientRenderCallbacks.AfterShaderSetCallback;

@Mixin(DrawContext.class)
public final class DrawContextMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V", shift = At.Shift.AFTER), method = "drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIIFFFF)V")
	private static void afterShaderSet(Identifier texture, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, @SuppressWarnings("unused") CallbackInfo info) {
		AfterShaderSetCallback.EVENT.invoker().afterShaderSet(texture, x0, x1, y0, y1, z, u0, u1, v0, v1);
	}
}
