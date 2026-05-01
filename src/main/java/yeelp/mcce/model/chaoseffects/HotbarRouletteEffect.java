package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.HotbarRoulettePayload;
import yeelp.mcce.util.PlayerUtils;

public final class HotbarRouletteEffect extends SimpleTimedChaosEffect {

    public static final int DURATION_MIN = 800, DURATION_MAX = 1600;

    public HotbarRouletteEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new HotbarRoulettePayload()::send);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    public String getName() {
        return "hotbarroulette";
    }

    @Override
    public String getDisplayName() {
        return "Hotbar Roulette";
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    public static void incrementHotbar(PlayerEntity player) {
        player.getInventory().setSelectedSlot((player.getInventory().getSelectedSlot() + 1) % 9);
    }
}
