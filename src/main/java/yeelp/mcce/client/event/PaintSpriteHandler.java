package yeelp.mcce.client.event;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnSpriteDrawCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.CallbackResult.ProcessState;
import yeelp.mcce.model.chaoseffects.PaintEffect;

import java.util.Set;

public final class PaintSpriteHandler implements OnSpriteDrawCallback {
    private static final Set<RenderPipeline> CANCELED_PIPELINES = Sets.newHashSet(RenderPipelines.VIGNETTE);

    @Override
    public CallbackResult shouldDraw(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null && CANCELED_PIPELINES.contains(pipeline) && PaintEffect.isClientTracked(MinecraftClient.getInstance().player)) {
            return new CallbackResult(ProcessState.CANCEL, CancelState.CANCEL);
        }
        return new CallbackResult();
    }
}
