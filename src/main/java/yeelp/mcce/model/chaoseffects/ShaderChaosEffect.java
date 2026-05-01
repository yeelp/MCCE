package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload.StatusPayload;

import java.util.function.Function;

public abstract class ShaderChaosEffect<P extends StatusPayload> extends ClientPlayerTrackingChaosEffect<P> {

    protected ShaderChaosEffect(int durationMin, int durationMax, Function<Boolean, P> payloadGenerator) {
        super(durationMin, durationMax, payloadGenerator);
    }

    protected ShaderChaosEffect(int duration, Function<Boolean, P> payloadGenerator) {
        super(duration, duration, payloadGenerator);
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.getChaosEffect(player, ShaderChaosEffect.class).isEmpty() && MCCEAPI.accessor.areChaosEffectsNotActive(player, this.getSpecificExclusions());
    }

    protected abstract ChaosEffectRegistryEntry[] getSpecificExclusions();
}
