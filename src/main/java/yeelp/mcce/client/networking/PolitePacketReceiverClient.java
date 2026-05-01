package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.screen.GuiPoliteScreen;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.PolitePayload;

public final class PolitePacketReceiverClient implements ClientPacketReceiver<PolitePayload> {

    @Override
    public void handlePayload(PolitePayload politePayload, Context context) {
        context.client().execute(() -> context.client().setScreen(new GuiPoliteScreen(politePayload.effect())));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.POLITE_ID;
    }
}
