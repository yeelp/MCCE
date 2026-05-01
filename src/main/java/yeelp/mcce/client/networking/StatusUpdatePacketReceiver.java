package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

import java.util.function.BiConsumer;

public class StatusUpdatePacketReceiver implements ClientPacketReceiver<StatusPayload> {
    private final BiConsumer<PlayerEntity, StatusPayload> trackingAction;
    private final Identifier id;

    public StatusUpdatePacketReceiver(Identifier id, BiConsumer<PlayerEntity, StatusPayload> trackingAction) {
        this.id = id;
        this.trackingAction = trackingAction;
    }

    @Override
    public void handlePayload(StatusPayload statusPayload, Context context) {
        context.client().execute(() -> {
            this.trackingAction.accept(context.player(), statusPayload);
        });
    }

    @Override
    public Identifier getID() {
        return this.id;
    }
}
