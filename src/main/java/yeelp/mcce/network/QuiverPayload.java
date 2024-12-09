package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record QuiverPayload(float pitch, float yaw) implements NetworkingPayloads.ChaosPayload {
    public static final Id<QuiverPayload> ID = new Id<>(NetworkingConstants.QUIVER_UPDATE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, QuiverPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, QuiverPayload::pitch,
            PacketCodecs.FLOAT, QuiverPayload::yaw,
            QuiverPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
