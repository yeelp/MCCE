package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record StutterSoundStatusPayload(boolean status) implements NetworkingPayloads.ChaosPayload.StatusPayload {
    public static final Id<StutterSoundStatusPayload> ID = new Id<>(NetworkingConstants.STUTTER_SOUND_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, StutterSoundStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, StutterSoundStatusPayload::status, StutterSoundStatusPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        //nothing
    }
}
