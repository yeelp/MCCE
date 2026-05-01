package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public record GrayscaleStatusPayload(boolean status) implements StatusPayload {
    public static final Id<GrayscaleStatusPayload> ID = new Id<>(NetworkingConstants.GRAYSCALE_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, GrayscaleStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, GrayscaleStatusPayload::status, GrayscaleStatusPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }
}
