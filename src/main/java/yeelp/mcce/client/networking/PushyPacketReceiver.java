package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.model.chaoseffects.PushyEffect;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.PushyPayload;

public final class PushyPacketReceiver implements ClientPacketReceiver<PushyPayload> {

    @Override
    public void handlePayload(PushyPayload pushyPayload, Context context) {
        context.client().execute(() -> PushyEffect.trackEffect(context.player(), pushyPayload));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.PUSHY_PACKET_ID;
    }
}
