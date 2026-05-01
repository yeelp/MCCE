package yeelp.mcce.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnCameraUpdate;
import yeelp.mcce.client.event.ClientRenderCallbacks.SetShaderCallback;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements SynchronousResourceReloader, AutoCloseable {

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private Identifier postProcessorId;

    @Shadow protected abstract void setPostProcessor(Identifier id);

    @Inject(method = "updateCamera", at = @At("RETURN"))
    public void onRender(RenderTickCounter tickCounter, CallbackInfo ci) {
        if(this.client.player != null && this.client.world != null) {
            OnCameraUpdate.EVENT.invoker().updateCamera(this.camera);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = Shift.AFTER))
    public void setShader(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if(this.client.player != null && this.client.world != null) {
            this.postProcessorId = SetShaderCallback.EVENT.invoker().setShaderIdentifier(this.postProcessorId);
            if(this.postProcessorId != null) {
                this.setPostProcessor(this.postProcessorId);
            }
        }
    }
}
