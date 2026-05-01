package yeelp.mcce.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

public abstract class ClientSideSyncHandler implements PlayerTickCallback {
    protected final ChaosEffectRegistryEntry entry;

    public ClientSideSyncHandler(ChaosEffectRegistryEntry entry) {
        this.entry = entry;
    }

    @Override
    public final void tick(PlayerEntity player) {
        //If the player saves and quits while the ChaosEffect is active and then joins a different world where the ChaosEffect is inactive
        //They will be "tracked" but will not have the effect active, so send a packet to disable the client side effects and stop tracking them.
        if(PlayerUtils.isPlayerWorldClient(player)) {
            return;
        }
        Tracker tracker = this.getTracker();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        boolean tracked = tracker.tracked(player);
        boolean active = MCCEAPI.accessor.isChaosEffectActive(player, this.entry);
        if(tracked && !active) {
            this.getPayload(player, true).send(serverPlayer);
            tracker.remove(player);
        }
        if(!tracked && active) {
            this.getPayload(player, false).send(serverPlayer);
            tracker.add(player);
        }
    }

    @Override
    public int priority() {
        return -1;
    }

    protected abstract Tracker getTracker();

    protected abstract ChaosPayload getPayload(PlayerEntity player, boolean isBeingRemoved);
}
