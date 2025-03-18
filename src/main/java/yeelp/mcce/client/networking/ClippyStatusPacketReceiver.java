package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.mixin.EntityASMMixin;
import yeelp.mcce.model.chaoseffects.ClippyEffect;
import yeelp.mcce.network.ClippyStatusPayload;
import yeelp.mcce.network.NetworkingConstants;

public final class ClippyStatusPacketReceiver implements ClientPacketReceiver<ClippyStatusPayload> {
    @Override
    public void handlePayload(ClippyStatusPayload clippyStatusPayload, Context context) {
        context.client().execute(() -> {
            PlayerEntity player = context.player();
            ClippyEffect.trackClient(player, clippyStatusPayload);
            player.noClip = true;
            ((EntityASMMixin) player).setOnGround(false);
        });
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.CLIPPY_STATUS_PACKET_ID;
    }
}
