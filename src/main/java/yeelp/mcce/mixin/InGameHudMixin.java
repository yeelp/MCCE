package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.client.event.ClientRenderCallbacks.RenderHealthCallback;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@SuppressWarnings("static-method")
	@Inject(at = @At("HEAD"), method = "renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIZ)V")
	private void onRenderHealthBefore(MatrixStack stack, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, @SuppressWarnings("unused") CallbackInfo info) {
		RenderHealthCallback.Phase.BEFORE.invoker().onRender(stack, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
	}
	
	@SuppressWarnings("static-method")
	@Inject(at = @At("TAIL"), method = "renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIZ)V")
	private void onRenderHealthAfter(MatrixStack stack, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, @SuppressWarnings("unused") CallbackInfo info) {
		RenderHealthCallback.Phase.AFTER.invoker().onRender(stack, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
	}
}
