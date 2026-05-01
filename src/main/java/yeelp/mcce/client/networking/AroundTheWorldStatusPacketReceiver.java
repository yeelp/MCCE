package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.AroundTheWorldScreenHandler;
import yeelp.mcce.network.AroundTheWorldStatusPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class AroundTheWorldStatusPacketReceiver implements ClientPacketReceiver<AroundTheWorldStatusPayload> {

    @Override
    public void handlePayload(AroundTheWorldStatusPayload aroundTheWorldStatusPayload, Context context) {
        context.client().execute(() -> AroundTheWorldScreenHandler.trackPlayer(context.player(), aroundTheWorldStatusPayload));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.AROUND_THE_WORLD_STATUS_PACKET_ID;
    }
}
