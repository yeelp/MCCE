package yeelp.mcce.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.client.event.ClientRenderCallbacks.BeforeClientRenderCallback;
import yeelp.mcce.model.chaoseffects.RotateEffect;

public final class RotateScreenHandler implements BeforeClientRenderCallback {
    @Override
    public void beforeClientRender() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null && RotateEffect.isClientTracked(player)) {
            RenderSystem.assertOnRenderThread();
            //noinspection MagicNumber
            RenderSystem.getModelViewStack().rotateZ((float) Math.toRadians(180.0));
        }
    }
}
