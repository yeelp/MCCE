package yeelp.mcce.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public interface NetworkingPayloads {

    interface ChaosPayload extends CustomPayload {
        default void send(ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, this);
            this.onSendCallback(this, player);
        }

        void onSendCallback(ChaosPayload payload, ServerPlayerEntity player);

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
    }
}
