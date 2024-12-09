package yeelp.mcce.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.ParticlePayload;

import java.util.Objects;

public class ParticlePacketReceiver implements ClientPacketReceiver<ParticlePayload> {

	@Override
	public void handlePayload(ParticlePayload particlePayload, ClientPlayNetworking.Context context) {
		context.client().execute(() -> Objects.requireNonNull(context.client().world).addParticle(NetworkingConstants.ParticlePacketConstants.getParticle(particlePayload.id()), particlePayload.x(), particlePayload.y(), particlePayload.z(), particlePayload.dx(), particlePayload.dy(), particlePayload.dz()));
	}

	@Override
	public Identifier getID() {
		return NetworkingConstants.ParticlePacketConstants.PARTICLE_PACKET_ID;
	}

}
