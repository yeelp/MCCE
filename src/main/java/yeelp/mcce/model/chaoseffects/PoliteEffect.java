package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.network.PolitePayload;
import yeelp.mcce.util.PlayerUtils;

public final class PoliteEffect extends AbstractInstantChaosEffect {

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        String name = ChaosEffectRegistry.getRandomApplicableEffectForPlayer(player).getName();
        PlayerUtils.getServerPlayer(player).ifPresent(new PolitePayload(name)::send);
    }

    @Override
    public String getName() {
        return "polite";
    }
}
