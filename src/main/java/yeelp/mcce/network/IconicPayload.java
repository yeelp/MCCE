package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record IconicPayload(byte heartIcon, byte hungerIcon, boolean hardcore) implements ChaosPayload {
    public static final Id<IconicPayload> ID = new Id<>(NetworkingConstants.ICONIC_ID);
    public static final PacketCodec<RegistryByteBuf, IconicPayload> CODEC = PacketCodec.tuple(PacketCodecs.BYTE, IconicPayload::heartIcon, PacketCodecs.BYTE, IconicPayload::hungerIcon, PacketCodecs.BOOLEAN, IconicPayload::hardcore, IconicPayload::new);

    public IconicPayload() {
        this((byte) -1, (byte) -1, false);
    }

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
