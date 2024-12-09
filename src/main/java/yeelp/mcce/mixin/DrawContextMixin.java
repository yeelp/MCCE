package yeelp.mcce.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeTextureColour;

import java.util.function.Function;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

	@Unique
	private static boolean firstPass = true;

	@Shadow
    protected abstract void drawTexturedQuad(Function<Identifier, RenderLayer> renderLayers, Identifier sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, int color);

	@SuppressWarnings("static-method")
	@Inject(at = @At("TAIL"), method = "drawTexturedQuad(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIIFFFFI)V")
	private void afterShaderSet(Function<Identifier, RenderLayer> renderLayers, Identifier sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, int color, CallbackInfo ci) {
		if(firstPass) {
			int newColor = ChangeTextureColour.EVENT.invoker().changeColor(renderLayers, sprite, x1, x2, y1, y2, u1, u2, v1, v2, color);
			if (newColor != color) {
				firstPass = false;
				this.drawTexturedQuad(renderLayers, sprite, x1, x2, y1, y2, u1, u2, v1, v2, newColor);
			}
			return;
		}
		firstPass = true;
	}
}