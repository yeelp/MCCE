package yeelp.mcce.client.event;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeSpriteCallback;
import yeelp.mcce.model.chaoseffects.IconicEffect;

public final class IconicSpriteSwapper implements ChangeSpriteCallback {

    @Override
    public Identifier getNewSprite(Identifier sprite, RenderPipeline pipeline) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) {
            return sprite;
        }
        String path = sprite.getPath();
        if(path.contains("hud/heart") && !path.contains("container")) {
            //we have a hardcore status because the heart type is present if get is called.
            //noinspection OptionalGetWithoutIsPresent
            return IconicEffect.getHeartType(player).map((type) -> type.swap(sprite, IconicEffect.getHardcoreStatus(player).get())).orElse(sprite);
        }
        else if(path.contains("hud/food")) {
            return IconicEffect.getHungerType(player).map((type) -> type.swap(sprite)).orElse(sprite);
        }
        return sprite;
    }
}
