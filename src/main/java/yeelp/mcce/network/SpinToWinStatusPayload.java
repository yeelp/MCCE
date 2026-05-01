package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public record SpinToWinStatusPayload(boolean status) implements StatusPayload {
    public static final Id<SpinToWinStatusPayload> ID = new Id<>(NetworkingConstants.SPIN_TO_WIN_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SpinToWinStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, SpinToWinStatusPayload::status, SpinToWinStatusPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
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
