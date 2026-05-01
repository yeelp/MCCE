package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record StutterSoundStatusPayload(boolean status) implements NetworkingPayloads.ChaosPayload.StatusPayload {
    public static final Id<StutterSoundStatusPayload> ID = new Id<>(NetworkingConstants.STUTTER_SOUND_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, StutterSoundStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, StutterSoundStatusPayload::status, StutterSoundStatusPayload::new);

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
