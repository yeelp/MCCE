package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.util.Identifier;
import yeelp.mcce.model.chaoseffects.HotbarRouletteEffect;
import yeelp.mcce.network.HotbarRoulettePayload;
import yeelp.mcce.network.NetworkingConstants;

public final class HotbarRouletteReceiver implements ClientPacketReceiver<HotbarRoulettePayload> {

    @Override
    public void handlePayload(HotbarRoulettePayload hotbarRoulettePayload, Context context) {
        context.client().execute(() -> HotbarRouletteEffect.incrementHotbar(context.player()));
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.HOTBAR_ROULETTE_PACKET_ID;
    }
}
