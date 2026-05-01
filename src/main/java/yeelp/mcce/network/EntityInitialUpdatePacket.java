package yeelp.mcce.network;

import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

import java.util.UUID;
import java.util.function.Consumer;

public record EntityInitialUpdatePacket(UUID id, UpdateAction action) implements NetworkingPayloads.ChaosPayload {
    public static final PacketCodec<RegistryByteBuf, EntityInitialUpdatePacket> CODEC = PacketCodec.tuple(PacketCodecs.LONG_ARRAY.xmap((a) -> new UUID(a[0], a[1]), (uuid) -> new long[] {uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()}), EntityInitialUpdatePacket::id, PacketCodecs.BYTE, (p) -> (byte) p.action().ordinal(), EntityInitialUpdatePacket::new);
    public static final Id<EntityInitialUpdatePacket> ID = new Id<>(NetworkingConstants.ENTITY_INITIAL_UPDATE);

    private EntityInitialUpdatePacket(UUID id, byte actionId) {
        this(id, UpdateAction.values()[actionId]);
    }
    public enum UpdateAction {
        SET_NO_CLIP((entity) -> entity.noClip = true);

        private final Consumer<Entity> action;

        UpdateAction(Consumer<Entity> action) {
            this.action = action;
        }

        public void update(Entity entity) {
            this.action.accept(entity);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return new Id<>(NetworkingConstants.ENTITY_INITIAL_UPDATE);
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }

    @Override
    public void onSendCallback(ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
