package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record PolitePayload(String effect) implements ChaosPayload {
    public static final Id<PolitePayload> ID = new Id<>(NetworkingConstants.POLITE_ID);
    public static final PacketCodec<RegistryByteBuf, PolitePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, PolitePayload::effect, PolitePayload::new);

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
