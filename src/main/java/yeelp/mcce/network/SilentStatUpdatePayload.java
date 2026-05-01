package yeelp.mcce.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.mixin.ServerPlayerASMMixin;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record SilentStatUpdatePayload(float health, float sat, int hunger, int air) implements NetworkingPayloads.ChaosPayload {
    public static final Id<SilentStatUpdatePayload> ID = new Id<>(NetworkingConstants.SILENT_UPDATE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SilentStatUpdatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, SilentStatUpdatePayload::health,
            PacketCodecs.FLOAT, SilentStatUpdatePayload::sat,
            PacketCodecs.INTEGER, SilentStatUpdatePayload::hunger,
            PacketCodecs.INTEGER, SilentStatUpdatePayload::air,
            SilentStatUpdatePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Override
    public PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec() {
        return CODEC;
    }

    public SilentStatUpdatePayload(PlayerEntity p) {
        this(p.getHealth(), p.getHungerManager().getSaturationLevel(), p.getHungerManager().getFoodLevel(), p.getAir());
    }

    @Override
    public void onSendCallback(NetworkingPayloads.ChaosPayload payload, ServerPlayerEntity player) {
        ServerPlayerASMMixin asmPlayer = (ServerPlayerASMMixin) player;
        asmPlayer.setSyncedHealth(player.getHealth());
        asmPlayer.setSyncedFoodLevel(player.getHungerManager().getFoodLevel());
        asmPlayer.setSyncedSaturationIsZero((player.getHungerManager().getSaturationLevel() == 0.0f));
    }
}
