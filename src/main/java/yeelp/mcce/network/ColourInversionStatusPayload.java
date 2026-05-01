package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public record ColourInversionStatusPayload(boolean status) implements StatusPayload {
    public static final Id<ColourInversionStatusPayload> ID = new Id<>(NetworkingConstants.COLOUR_INVERSION_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ColourInversionStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, ColourInversionStatusPayload::status, ColourInversionStatusPayload::new);

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
