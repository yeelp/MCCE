package yeelp.mcce.network;

import net.minecraft.entity.EntityEquipment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

public record PaintStatusPayload(boolean status) implements StatusPayload {
    public static final Id<PaintStatusPayload> ID = new Id<>(NetworkingConstants.PAINT_STATUS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, PaintStatusPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, PaintStatusPayload::status, PaintStatusPayload::new);

    @Override
    public void onSendCallback(ChaosPayload payload, ServerPlayerEntity player) {
        if(!((StatusPayload) payload).status()) {
            PlayerInventory temp = new PlayerInventory(player, new EntityEquipment());
            PlayerInventory inv = player.getInventory();
            temp.clone(inv);
            inv.clear();
            inv.clone(temp);
        }
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
