package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.model.chaoseffects.IconicEffect;
import yeelp.mcce.network.IconicPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class IconicPacketReceiver implements ClientPacketReceiver<IconicPayload> {

    @Override
    public void handlePayload(IconicPayload iconicPayload, Context context) {
        context.client().execute(() -> IconicEffect.trackEffect(context.player(), iconicPayload));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.ICONIC_ID;
    }
}
