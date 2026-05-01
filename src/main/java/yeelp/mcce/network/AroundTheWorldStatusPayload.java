package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;
import yeelp.mcce.util.ChaosLib;

public record AroundTheWorldStatusPayload(boolean status, RotationAmount rot) implements StatusPayload {
    public static final Id<AroundTheWorldStatusPayload> ID = new Id<>(NetworkingConstants.AROUND_THE_WORLD_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, AroundTheWorldStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BYTE_ARRAY, (packet) -> new byte[]{(byte) (packet.status() ? 1 : 0), (byte) packet.rot().ordinal()}, AroundTheWorldStatusPayload::new);

    public AroundTheWorldStatusPayload(boolean status) {
        this(status, ChaosLib.getRandomElementFrom(RotationAmount.values()));
    }

    private AroundTheWorldStatusPayload(byte[] status) {
        this(status[0] > 0, RotationAmount.values()[status[1]]);
    }

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

    public enum RotationAmount {
        CW_90(-Math.PI/2),
        CCW_90(Math.PI/2),
        FULL_180(Math.PI);

        private final float rotationAmount;

        RotationAmount(double ang) {
            this.rotationAmount = (float) ang;
        }

        public float getRoatationAngle() {
            return this.rotationAmount;
        }
    }
}
