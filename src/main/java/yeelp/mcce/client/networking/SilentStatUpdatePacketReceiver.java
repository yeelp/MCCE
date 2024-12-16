package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.CycleOfLifeTiltHandler;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SilentStatUpdatePayload;

public final class SilentStatUpdatePacketReceiver implements ClientPacketReceiver<SilentStatUpdatePayload> {

	@Override
	public void handlePayload(SilentStatUpdatePayload silentStatUpdatePayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> {
			context.player().setHealth(silentStatUpdatePayload.health());
			context.player().getHungerManager().setFoodLevel(silentStatUpdatePayload.hunger());
			context.player().getHungerManager().setSaturationLevel(silentStatUpdatePayload.sat());
			CycleOfLifeTiltHandler.addPlayer(context.player());
		});
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.SILENT_UPDATE_PACKET_ID;
	}

}
