package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record SoundPayload(byte id, float pitch, float volume) implements NetworkingPayloads.ChaosPayload {
    public static final Id<SoundPayload> ID = new Id<>(NetworkingConstants.SoundPacketConstants.SOUND_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SoundPayload> CODEC = PacketCodec.tuple(PacketCodecs.BYTE, SoundPayload::id, PacketCodecs.FLOAT, SoundPayload::pitch, PacketCodecs.FLOAT, SoundPayload::volume, SoundPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
