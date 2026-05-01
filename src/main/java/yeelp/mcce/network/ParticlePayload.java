package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record ParticlePayload(byte id, float x, float y, float z, float dx, float dy, float dz) implements NetworkingPayloads.ChaosPayload {
    public static final Id<ParticlePayload> ID = new Id<>(NetworkingConstants.ParticlePacketConstants.PARTICLE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ParticlePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BYTE, ParticlePayload::id,
            PacketCodecs.FLOAT, ParticlePayload::x,
            PacketCodecs.FLOAT, ParticlePayload::y,
            PacketCodecs.FLOAT, ParticlePayload::z,
            PacketCodecs.FLOAT, ParticlePayload::dx,
            PacketCodecs.FLOAT, ParticlePayload::dy,
            PacketCodecs.FLOAT, ParticlePayload::dz,
            ParticlePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
