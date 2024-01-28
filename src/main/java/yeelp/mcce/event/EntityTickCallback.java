package yeelp.mcce.event;

import java.util.Arrays;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;

@FunctionalInterface
public interface EntityTickCallback extends Comparable<EntityTickCallback> {

	Event<EntityTickCallback> EVENT = EventFactory.createArrayBacked(EntityTickCallback.class, (listeners) -> (entity) -> {
		Arrays.stream(listeners).sorted().forEach((etc) -> etc.tick(entity));
	});
	
	void tick(Entity entity);
	
	default int priority() {
		return 0;
	}
	
	@Override
	default int compareTo(EntityTickCallback o) {
		return -(this.priority() - o.priority());
	}

}
