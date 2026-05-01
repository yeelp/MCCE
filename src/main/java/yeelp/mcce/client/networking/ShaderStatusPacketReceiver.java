package yeelp.mcce.client.networking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

import java.util.function.BiConsumer;

public final class ShaderStatusPacketReceiver extends StatusUpdatePacketReceiver {
    public ShaderStatusPacketReceiver(Identifier id, BiConsumer<PlayerEntity, StatusPayload> trackingAction) {
        super(id, trackingAction.andThen((player, payload) -> {
            if(!payload.status()) {
                MinecraftClient.getInstance().gameRenderer.clearPostProcessor();
            }
        }));
    }
}
