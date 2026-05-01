package yeelp.mcce.client.event;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.client.event.ClientRenderCallbacks.OnCameraUpdate;
import yeelp.mcce.network.AroundTheWorldStatusPayload;
import yeelp.mcce.network.AroundTheWorldStatusPayload.RotationAmount;

import java.util.Map;
import java.util.UUID;

public final class AroundTheWorldScreenHandler implements OnCameraUpdate {

    private static final Map<UUID, RotationAmount> TRACKED_PLAYERS = Maps.newHashMap();

    @Override
    public void updateCamera(Camera camera) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) {
            return;
        }
        RotationAmount rot = TRACKED_PLAYERS.get(player.getUuid());
        if(rot != null) {
            camera.getRotation().rotateZ(rot.getRoatationAngle());
        }
    }

    public static void trackPlayer(PlayerEntity player, AroundTheWorldStatusPayload payload) {
        if(payload.status()) {
            TRACKED_PLAYERS.put(player.getUuid(), payload.rot());
        }
        else {
            TRACKED_PLAYERS.remove(player.getUuid());
        }
    }
}
