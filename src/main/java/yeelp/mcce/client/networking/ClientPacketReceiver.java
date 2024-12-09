package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public interface ClientPacketReceiver<T extends CustomPayload> {

	void handlePayload(T t, ClientPlayNetworking.Context context);
	
	Identifier getID();

	default CustomPayload.Id<T> getPayloadID() {
		return new CustomPayload.Id<>(this.getID());
	}
	
	default void register() {
		ClientPlayNetworking.registerGlobalReceiver(this.getPayloadID(), this::handlePayload);
	}
}
