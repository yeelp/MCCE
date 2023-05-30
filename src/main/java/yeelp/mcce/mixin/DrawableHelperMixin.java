package yeelp.mcce.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawableHelper;
import yeelp.mcce.client.event.ClientRenderCallbacks.AfterShaderSetCallback;

@Mixin(DrawableHelper.class)
public final class DrawableHelperMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V", shift = At.Shift.AFTER), method = "drawTexturedQuad(Lorg/joml/Matrix4f;IIIIIFFFF)V")
	private static void afterShaderSet(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, @SuppressWarnings("unused") CallbackInfo info) {
		AfterShaderSetCallback.EVENT.invoker().afterShaderSet(matrix, x0, x1, y0, y1, z, u0, u1, v0, v1);
	}
}
