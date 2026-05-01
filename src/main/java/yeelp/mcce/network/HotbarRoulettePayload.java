package yeelp.mcce.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.model.chaoseffects.HotbarRouletteEffect;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;

public record HotbarRoulettePayload() implements ChaosPayload {
    public static final Id<HotbarRoulettePayload> ID = new Id<>(NetworkingConstants.HOTBAR_ROULETTE_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, HotbarRoulettePayload> CODEC = PacketCodec.unit(new HotbarRoulettePayload());

    @Override
    public void onSendCallback(ChaosPayload payload, ServerPlayerEntity player) {
        HotbarRouletteEffect.incrementHotbar(player);
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
        return obj instanceof HotbarRoulettePayload;
    }
}
