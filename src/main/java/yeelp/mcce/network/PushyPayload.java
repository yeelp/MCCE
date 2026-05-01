package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record PushyPayload(boolean locked, @Nullable Vec3d direction) implements ChaosPayload {
    public static final Id<PushyPayload> ID = new Id<>(NetworkingConstants.PUSHY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PushyPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN,
            PushyPayload::locked,
            PacketCodec.of((vec, registryByteBuf) -> registryByteBuf.writeNullable(vec, (buf, v) -> registryByteBuf.writeVec3d(v)), (registryByteBuf) -> registryByteBuf.readNullable((reg) -> reg.readVec3d())),
            PushyPayload::direction,
            PushyPayload::new);

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
