package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public interface NetworkingPayloads {

    interface ChaosPayload extends CustomPayload {
        default void send(ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, this);
            this.onSendCallback(this, player);
        }

        void onSendCallback(ChaosPayload payload, ServerPlayerEntity player);

        PacketCodec<RegistryByteBuf, ? extends ChaosPayload> getCodec();

        interface StatusPayload extends ChaosPayload {
            boolean status();
        }
    }

    static void initialize() {
        PayloadTypeRegistry.playS2C().register(MemoryGamePayload.ID, MemoryGamePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ParticlePayload.ID, ParticlePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(QuiverPayload.ID, QuiverPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RainbowStatusPayload.ID, RainbowStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SilentStatUpdatePayload.ID, SilentStatUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SoundPayload.ID, SoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StutterSoundStatusPayload.ID, StutterSoundStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LookInversionStatusPayload.ID, LookInversionStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ClippyStatusPayload.ID, ClippyStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(InverseStatusPayload.ID, InverseStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PaintStatusPayload.ID, PaintStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RotateStatusPayload.ID, RotateStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AroundTheWorldStatusPayload.ID, AroundTheWorldStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SpinToWinStatusPayload.ID, SpinToWinStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GrayscaleStatusPayload.ID, GrayscaleStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MosaicStatusPayload.ID, MosaicStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ColourInversionStatusPayload.ID, ColourInversionStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BakeStatusPayload.ID, BakeStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SepiaStatusPayload.ID, SepiaStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ClickyPayload.ID, ClickyPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HotbarRoulettePayload.ID, HotbarRoulettePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PushyPayload.ID, PushyPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ScatterStatusPayload.ID, ScatterStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(IconicPayload.ID, IconicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PolitePayload.ID, PolitePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FlippingOutStatusPayload.ID, FlippingOutStatusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GammegaStatusPayload.ID, GammegaStatusPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(EntityInitialUpdatePacket.ID, EntityInitialUpdatePacket.CODEC);

        PayloadTypeRegistry.playC2S().register(PolitePayload.ID, PolitePayload.CODEC);
    }
}
