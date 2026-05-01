package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record ClickyPayload() implements ChaosPayload {
    public static final Id<ClickyPayload> ID = new Id<>(NetworkingConstants.CLICKY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ClickyPayload> CODEC = PacketCodec.unit(new ClickyPayload());

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClickyPayload;
    }
}
