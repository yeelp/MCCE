package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record MemoryGamePayload(boolean status) implements NetworkingPayloads.ChaosPayload.StatusPayload {
    public static final Id<MemoryGamePayload> ID = new Id<>(NetworkingConstants.MEMORY_GAME_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, MemoryGamePayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, MemoryGamePayload::status, MemoryGamePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }
}
