package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import yeelp.mcce.mixin.MouseASMMixin;
import yeelp.mcce.network.ClickyPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class ClickyReceiver implements ClientPacketReceiver<ClickyPayload> {

    private static boolean click = true;

    @Override
    public void handlePayload(ClickyPayload clickyPayload, Context context) {
        context.client().execute(() -> {
            int mod = context.client().isShiftPressed() ? GLFW.GLFW_MOD_SHIFT : 0;
            click = !click;
            ((MouseASMMixin) MinecraftClient.getInstance().mouse).mcce$onMouseButton(context.client().getWindow().getHandle(), new MouseInput(0, mod), click ? InputUtil.GLFW_PRESS : 0);
        });
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.CLICKY_PACKET_ID;
    }
}
