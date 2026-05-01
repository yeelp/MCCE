package yeelp.mcce.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ClientRenderCallbacks.BeforeModelRenderCallback;

@Mixin(Model.class)
@Environment(EnvType.CLIENT)
public class ModelMixin {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", at = @At("HEAD"))
    public void onRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
        Model<?> model = (Model<?>) (Object) this;
        BeforeModelRenderCallback.EVENT.invoker().beforeRender(matrices, model);
    }
}
