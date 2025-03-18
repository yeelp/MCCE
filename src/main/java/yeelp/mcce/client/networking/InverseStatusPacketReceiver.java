package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.model.chaoseffects.InverseEffect;
import yeelp.mcce.network.InverseStatusPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class InverseStatusPacketReceiver implements ClientPacketReceiver<InverseStatusPayload> {

    @Override
    public void handlePayload(InverseStatusPayload inverseStatusPayload, Context context) {
        context.client().execute(() -> InverseEffect.trackClient(context.player(), inverseStatusPayload));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.INVERSE_STATUS_PACKET_ID;
    }
}
