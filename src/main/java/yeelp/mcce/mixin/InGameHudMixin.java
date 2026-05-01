package yeelp.mcce.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.ScatterSpriteHandler;
import yeelp.mcce.client.event.SpinToWinSpriteHandler;
import yeelp.mcce.model.chaoseffects.ScatterEffect;
import yeelp.mcce.model.chaoseffects.SpinToWinEffect;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) {
            return;
        }
        if(SpinToWinEffect.isClientTracked(player)) {
            SpinToWinSpriteHandler.incrementRotationAmount(player.getUuid());
        }
        if(ScatterEffect.isClientTracked(player)) {
            ScatterSpriteHandler.reset(player);
        }
    }
}
