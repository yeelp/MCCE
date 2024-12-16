package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.model.chaoseffects.LookInversionEffect;
import yeelp.mcce.network.LookInversionStatusPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class LookInversionStatusPacketReceiver implements ClientPacketReceiver<LookInversionStatusPayload> {
    @Override
    public void handlePayload(LookInversionStatusPayload lookInversionStatusPayload, Context context) {
        context.client().execute(() -> LookInversionEffect.trackClient(context.player(), lookInversionStatusPayload));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.LOOK_INVERSION_STATUS_PACKET_ID;
    }
}
