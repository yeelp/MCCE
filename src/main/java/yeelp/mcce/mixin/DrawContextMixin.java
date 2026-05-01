package yeelp.mcce.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeSpriteCallback;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeSpriteColourCallback;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnSpriteDrawCallback;
import yeelp.mcce.event.CallbackResult.CancelState;

@Mixin(DrawContext.class)
@Environment(EnvType.CLIENT)
public abstract class DrawContextMixin {

	@ModifyVariable(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V", at = @At("HEAD"), ordinal = 4, argsOnly = true)
	public int alterColour(int input, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int colour) {
		return ChangeSpriteColourCallback.EVENT.invoker().changeColour(pipeline, sprite, input);
	}

	@ModifyVariable(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIIII)V", at = @At("HEAD"), ordinal = 8, argsOnly = true)
	public int alterColour(int input, RenderPipeline pipeline, Identifier sprite, int texWidth, int texHeight, int u, int v, int x, int y, int width, int height, int colour) {
		return ChangeSpriteColourCallback.EVENT.invoker().changeColour(pipeline, sprite, input);
	}

	@ModifyVariable(method = "drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIIII)V", at = @At("HEAD"), ordinal = 8, argsOnly = true)
	public int alterColour(int input, RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int colour) {
		return ChangeSpriteColourCallback.EVENT.invoker().changeColour(pipeline, sprite, input);
	}

	@ModifyVariable(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public Identifier changeSprite(Identifier input, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int colour) {
		return ChangeSpriteCallback.EVENT.invoker().getNewSprite(sprite, pipeline);
	}

	@Inject(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V", at = @At("HEAD"), cancellable = true)
	public void onDraw(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int colour, CallbackInfo ci) {
		callSpriteDrawCallback((DrawContext) (Object) this, pipeline, sprite, x, y, width, height, ci);
	}

	@Inject(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIIII)V", at = @At("HEAD"), cancellable = true)
	public void onDraw(RenderPipeline pipeline, Identifier sprite, int texWidth, int texHeight, int u, int v, int x, int y, int width, int height, int colour, CallbackInfo ci) {
		callSpriteDrawCallback((DrawContext) (Object) this, pipeline, sprite, x, y, width, height, ci);
	}

	@Inject(method = "drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIIII)V", at = @At("HEAD"), cancellable = true)
	public void onDraw(RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int colour, CallbackInfo ci) {
		callSpriteDrawCallback((DrawContext) (Object) this, pipeline, sprite, x, y, width, height, ci);
	}

	@Inject(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V", at = @At("TAIL"))
	public void onDrawEnd(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int colour, CallbackInfo ci) {
		((DrawContext) (Object) this).getMatrices().popMatrix();
	}

	@Inject(method = "drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIIIIIII)V", at = @At("TAIL"))
	public void onDrawEnd(RenderPipeline pipeline, Identifier sprite, int texWidth, int texHeight, int u, int v, int x, int y, int width, int height, int colour, CallbackInfo ci) {
		((DrawContext) (Object) this).getMatrices().popMatrix();
	}

	@Inject(method = "drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIIII)V", at = @At("TAIL"))
	public void onDrawEnd(RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int colour, CallbackInfo ci) {
		((DrawContext) (Object) this).getMatrices().popMatrix();
	}

	@Unique
	private static void callSpriteDrawCallback(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, CallbackInfo ci) {
		context.getMatrices().pushMatrix();
		if(OnSpriteDrawCallback.EVENT.invoker().shouldDraw(context, pipeline, sprite, x, y, width, height).getCancelState() == CancelState.CANCEL) {
			ci.cancel();
			context.getMatrices().popMatrix();
		}
	}
}