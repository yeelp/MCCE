package yeelp.mcce.client.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeSpriteColourCallback;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.model.chaoseffects.RainbowEffect;
import yeelp.mcce.util.ColourUtil;

import java.util.*;

public final class RainbowHandler implements ChangeSpriteColourCallback, PlayerTickCallback {

	private static final Map<UUID, Float> HUE_SHIFT = Maps.newHashMap();
	private static final Set<RenderPipeline> AFFECTED_PIPELINES = Sets.newHashSet(
			RenderPipelines.CROSSHAIR,
			RenderPipelines.GUI,
			RenderPipelines.GUI_TEXT,
			RenderPipelines.GUI_TEXTURED,
			RenderPipelines.GUI_TEXT_HIGHLIGHT,
			RenderPipelines.VIGNETTE,
			RenderPipelines.RENDERTYPE_TEXT,
			RenderPipelines.RENDERTYPE_TEXT_BG,
			RenderPipelines.GUI_INVERT,
			RenderPipelines.GLINT,
			RenderPipelines.RENDERTYPE_TEXT_BG_SEETHROUGH,
			RenderPipelines.RENDERTYPE_TEXT_INTENSITY,
			RenderPipelines.RENDERTYPE_TEXT_POLYGON_OFFSET,
			RenderPipelines.ANIMATE_SPRITE_BLIT,
			RenderPipelines.LINES,
			RenderPipelines.GUI_OPAQUE_TEX_BG,
			RenderPipelines.GUI_TEXT_INTENSITY,
			RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA);

	@Override
	public int changeColour(RenderPipeline pipeline, Identifier sprite, int currentColour) {
		if(!AFFECTED_PIPELINES.contains(pipeline)) {
			return currentColour;
		}
		return changeColour().orElse(currentColour);
	}

	@Override
	@SuppressWarnings("MagicNumber")
	public void tick(PlayerEntity player) {
		if(RainbowEffect.isClientTracked(player)) {
			HUE_SHIFT.merge(player.getUuid(), 0.2f, (old, curr) -> (old + curr) % 360);
		}
	}

	public static OptionalInt changeColour() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if(Optional.ofNullable(player).filter(RainbowEffect::isClientTracked).isEmpty()) {
			return OptionalInt.empty();
		}
		short[] colour = ColourUtil.HSLtoRGB(HUE_SHIFT.getOrDefault(player.getUuid(), 0.0f));
		return OptionalInt.of(ColorHelper.getArgb(colour[0], colour[1], colour[2]));
	}
}
