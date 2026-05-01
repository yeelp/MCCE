package yeelp.mcce.client.networking;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.network.EntityInitialUpdatePacket;
import yeelp.mcce.network.EntityInitialUpdatePacket.UpdateAction;
import yeelp.mcce.network.NetworkingConstants;

import java.util.UUID;

public final class EntityInitialUpdatePacketReceiver implements ClientPacketReceiver<EntityInitialUpdatePacket>, EntityTickCallback {

    private static final Multimap<UUID, UpdateAction> UPDATE_ACTIONS = MultimapBuilder.hashKeys().enumSetValues(UpdateAction.class).build();

    @Override
    public void handlePayload(EntityInitialUpdatePacket entityInitialUpdatePacket, Context context) {
        UPDATE_ACTIONS.put(entityInitialUpdatePacket.id(), entityInitialUpdatePacket.action());
    }

    @Override
    public Identifier getID() {
        return NetworkingConstants.ENTITY_INITIAL_UPDATE;
    }

    @Override
    public void tick(Entity entity) {
        UPDATE_ACTIONS.removeAll(entity.getUuid()).forEach((action) -> action.update(entity));
    }
}
