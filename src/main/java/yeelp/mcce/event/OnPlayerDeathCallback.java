package yeelp.mcce.event;

import com.google.common.collect.Iterators;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface OnPlayerDeathCallback {

    Event<OnPlayerDeathCallback> EVENT = EventFactory.createArrayBacked(OnPlayerDeathCallback.class, (listeners) -> (player, damageSource) -> {
        Iterators.forArray(listeners).forEachRemaining((callback) -> callback.onDeath(player, damageSource));
    });

    void onDeath(ServerPlayerEntity player, DamageSource source);
}
