package yeelp.mcce.client.event;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.TiltScreenCallback;
import yeelp.mcce.util.Tracker;

public final class CycleOfLifeTiltHandler implements TiltScreenCallback {

    private static final Tracker TRACKED = new Tracker();

    @Override
    public CallbackResult shouldAllowTilt(PlayerEntity player, double deltaX, double deltaY) {
        if (TRACKED.tracked(player)) {
            TRACKED.remove(player);
            return new CallbackResult(CancelState.CANCEL);
        }
        return new CallbackResult();
    }

    public static void addPlayer(PlayerEntity player) {
        TRACKED.add(player);
    }
}
