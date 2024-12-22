package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SilentStatUpdatePayload;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

public abstract class AbstractStatCycleEffect extends SimpleTimedChaosEffect {

    static final int DURATION_MIN = 500, DURATION_MAX = 1000;
    static final float PITCH_MAX = 2.0f, VOLUME = 0.25f;

    protected AbstractStatCycleEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    public final void applyEffect(PlayerEntity player) {
        if(this.getBound(player) <= 1) {
            return;
        }
        this.alterStat(player, this.getRNG().nextFloat(this.getBound(player)));
        PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
            new SilentStatUpdatePayload(p).send(p);
            new SoundPayload(NetworkingConstants.SoundPacketConstants.UI_BUTTON_CLICK_ID, this.getRNG().nextFloat(0.0f, PITCH_MAX), VOLUME).send(p);
        });
    }

    @Override
    protected final boolean canStack() {
        return false;
    }

    @Override
    protected final boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return this.getBound(player) > 1 && MCCEAPI.accessor.areChaosEffectsNotActive(player, this.getMutualExclusiveEffects());
    }

    protected abstract void alterStat(PlayerEntity player, float value);

    protected abstract float getBound(PlayerEntity player);

    protected abstract ChaosEffectRegistryEntry[] getMutualExclusiveEffects();
}
