package yeelp.mcce.event;

import net.minecraft.entity.Entity;
import yeelp.mcce.model.DespawnTimer;
import yeelp.mcce.model.ServerState;

import java.util.Objects;

public final class DespawnTimerTracker implements EntityTickCallback {

	@Override
	public void tick(Entity entity) {
		if (entity.getEntityWorld().isClient()) {
			return;
		}
		ServerState state = ServerState.getServerState(Objects.requireNonNull(entity.getEntityWorld().getServer()));
		DespawnTimer timer;
		(timer = state.getDespawnTimer(entity.getUuid())).tick();
		if(timer.isExpired()) {
			entity.discard();
		}
		state.markDirty();
	}

}
