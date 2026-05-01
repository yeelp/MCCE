package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.ClickyPayload;
import yeelp.mcce.util.PlayerUtils;

public final class ClickyEffect extends AbstractIntervalChaosEffect {

    public static final int DURATION_MIN = 1000, DURATION_MAX = 2500, INTERVAL_MIN = 1, INTERVAL_MAX = 3;

    public ClickyEffect() {
        super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }


    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new ClickyPayload()::send);
    }

    @Override
    public String getName() {
        return "clicky";
    }

    @Override
    public void registerCallbacks() {
        //no callbacks
    }
}
