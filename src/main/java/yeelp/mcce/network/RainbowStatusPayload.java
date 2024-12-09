package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record RainbowStatusPayload(boolean status) implements NetworkingPayloads.ChaosPayload.StatusPayload {
    public static final Id<RainbowStatusPayload> ID = new Id<>(NetworkingConstants.RAINBOW_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, RainbowStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, RainbowStatusPayload::status, RainbowStatusPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
