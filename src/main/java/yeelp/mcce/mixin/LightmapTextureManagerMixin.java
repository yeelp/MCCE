package yeelp.mcce.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import yeelp.mcce.client.event.RainbowHandler;

@Mixin(LightmapTextureManager.class)
@Environment(EnvType.CLIENT)
public abstract class LightmapTextureManagerMixin implements AutoCloseable {

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper;toRgbVector(I)Lorg/joml/Vector3f;"), index = 0)
    private int changeLightColour(int curr) {
        return RainbowHandler.changeColour().orElse(curr);
    }
}
