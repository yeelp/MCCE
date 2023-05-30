package yeelp.mcce.event;

import java.util.Arrays;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Callback for player tick
 * @author Yeelp
 *
 */
@FunctionalInterface
public interface PlayerTickCallback extends Comparable<PlayerTickCallback> {
	
	Event<PlayerTickCallback> EVENT = EventFactory.createArrayBacked(PlayerTickCallback.class, (listeners) -> (player) -> {
		Arrays.stream(listeners).sorted().forEach((ptc) -> ptc.tick(player));
	});

	void tick(PlayerEntity player);
	
	default int priority() {
		return 0;
	}

	@Override
	default int compareTo(PlayerTickCallback o) {
		return -(this.priority() - o.priority());
	}
}
