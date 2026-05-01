package yeelp.mcce.client.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnSpriteDrawCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.model.chaoseffects.SpinToWinEffect;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class SpinToWinSpriteHandler implements OnSpriteDrawCallback {

    private static final Map<UUID, Float> ROTATION_AMOUNT = Maps.newHashMap();
    private static final Set<RenderPipeline> AFFECTED_PIPELINES = Sets.newHashSet(
            RenderPipelines.CROSSHAIR,
            RenderPipelines.GUI,
            RenderPipelines.GUI_TEXTURED);

    private static final Set<String> BAD_IDENTIFIERS = Sets.newHashSet("toast", "attack", "hotbar", "experience_bar", "background", "button", "jump", "locator", "frame", "progress");

    public static void incrementRotationAmount(UUID uuid) {
        //noinspection MagicNumber
        ROTATION_AMOUNT.merge(uuid, 0.01f, Float::sum);
    }

    @Override
    public CallbackResult shouldDraw(DrawContext context, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null && AFFECTED_PIPELINES.contains(pipeline) && BAD_IDENTIFIERS.stream().noneMatch(sprite.getPath()::contains) && SpinToWinEffect.isClientTracked(player)) {
            //noinspection MagicNumber
            context.getMatrices().rotateAbout(ROTATION_AMOUNT.get(player.getUuid()), x + width / 2.0f, y + height / 2.0f);
        }
        return new CallbackResult();
    }
}
