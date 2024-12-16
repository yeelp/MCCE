package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.QuiverPayload;

public final class QuiverUpdatePacketReceiver implements ClientPacketReceiver<QuiverPayload> {

	@Override
	public void handlePayload(QuiverPayload quiverPayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			PlayerEntity player = context.player();
			if(player == null) {
				return;
			}
			player.setPitch(player.getPitch() + quiverPayload.pitch());
			player.setYaw(player.getYaw() + quiverPayload.yaw());
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.QUIVER_UPDATE_PACKET_ID;
	}

}
