package yeelp.mcce.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ClientRenderCallbacks.AfterClientRenderCallback;
import yeelp.mcce.client.event.ClientRenderCallbacks.BeforeClientRenderCallback;

@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public abstract class MinecraftClientMixin {

    @Inject(method = "render(Z)V", at = @At("HEAD"))
    private void renderStart(boolean tick, CallbackInfo ci) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.getModelViewStack().pushMatrix();
        BeforeClientRenderCallback.EVENT.invoker().beforeClientRender();
    }

    @Inject(method = "render(Z)V", at = @At("TAIL"))
    private void renderEnd(boolean tick, CallbackInfo ci) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.getModelViewStack().popMatrix();
        AfterClientRenderCallback.EVENT.invoker().afterClientRender();
    }
}
