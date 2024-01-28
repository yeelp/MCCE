package yeelp.mcce.event;

import net.minecraft.entity.Entity;
import yeelp.mcce.model.DespawnTimer;
import yeelp.mcce.model.ServerState;

public final class DespawnTimerTracker implements EntityTickCallback {

	@SuppressWarnings("resource")
	@Override
	public void tick(Entity entity) {
		if (entity.getWorld().isClient) {
			return;
		}
		ServerState state = ServerState.getServerState(entity.getServer());
		DespawnTimer timer;
		(timer = state.getDespawnTimer(entity.getUuid())).tick();
		if(timer.isExpired()) {
			entity.discard();
		}
		state.markDirty();
	}

}
