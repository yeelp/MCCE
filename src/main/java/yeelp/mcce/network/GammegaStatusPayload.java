package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public record GammegaStatusPayload(boolean status) implements StatusPayload {
    public static final Id<GammegaStatusPayload> ID = new Id<>(NetworkingConstants.GAMMEGA_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, GammegaStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, GammegaStatusPayload::status, GammegaStatusPayload::new);

    @Override
    public void onSendCallback(ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
