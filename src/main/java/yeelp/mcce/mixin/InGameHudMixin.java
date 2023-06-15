package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.client.event.ClientRenderCallbacks.RenderHealthCallback;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin extends DrawContext {

	public InGameHudMixin(MinecraftClient client, Immediate vertexConsumers) {
		super(client, vertexConsumers);
	}

	@SuppressWarnings("static-method")
	@Inject(at = @At("HEAD"), method = "renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V")
	private void onRenderHealthBefore(DrawContext context, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, @SuppressWarnings("unused") CallbackInfo info) {
		RenderHealthCallback.Phase.BEFORE.invoker().onRender(context, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
	}
	
	@SuppressWarnings("static-method")
	@Inject(at = @At("TAIL"), method = "renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V")
	private void onRenderHealthAfter(DrawContext context, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, @SuppressWarnings("unused") CallbackInfo info) {
		RenderHealthCallback.Phase.AFTER.invoker().onRender(context, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
	}
}
