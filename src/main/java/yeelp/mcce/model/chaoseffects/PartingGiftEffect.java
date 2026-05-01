package yeelp.mcce.model.chaoseffects;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class PartingGiftEffect extends AbstractIntervalChaosEffect {

    private static final int DURATION_MIN = 2400, DURATION_MAX = 3200, INTERVAL_MIN = 40, INTERVAL_MAX = 60, RADIUS = 20;
    private static final float PITCH_MIN = 0.8f, PITCH_MAX = 1.2f, VOLUME = 0.5f;

    public PartingGiftEffect() {
        super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        List<LivingEntity> targets = player.getEntityWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), LivingEntity::isAlive);
        targets.forEach((entity) -> {
            Arrays.stream(PartingEffects.values()).map(PartingEffects::getStatusEffect).forEach(entity::removeStatusEffect);
            for(int i = this.getRNG().nextInt(1,4); i > 0; i--) {
                entity.addStatusEffect(PartingEffects.getRandom(this.getRNG()).createStatusEffectInstance(this.durationRemaining()));
            }
        });
        if(!targets.isEmpty()) {
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.POTION_BREW, this.getRNG().nextFloat(PITCH_MIN, PITCH_MAX), VOLUME)::send);
        }
    }

    @Override
    public String getName() {
        return "partinggift";
    }

    @Override
    public String getDisplayName() {
        return "Parting Gift";
    }

    @Override
    public void registerCallbacks() {
        ServerLivingEntityEvents.AFTER_DEATH.register(((livingEntity, damageSource) -> {
            if(damageSource.getAttacker() instanceof ServerPlayerEntity player && this.getRNG().nextBoolean() && livingEntity.hasStatusEffect(PartingEffects.INFESTED.getStatusEffect())) {
                MCCEAPI.accessor.getChaosEffect(player, PartingGiftEffect.class).ifPresent((effect) -> MCCEAPI.mutator.addNewChaosEffect(player, ChaosEffects.INFESTATION.createChaosEffect()));
            }
        }));
    }

    private enum PartingEffects {
            WEAVING(StatusEffects.WEAVING, 0.3f),
            WIND_CHARGED(StatusEffects.WIND_CHARGED, 0.2f),
            OOZING(StatusEffects.OOZING, 0.3f),
            INFESTED(StatusEffects.INFESTED, 0.2f);

        private final RegistryEntry<StatusEffect> statusEffect;
        private final float weight;

        PartingEffects(RegistryEntry<StatusEffect> statusEffect, float weight) {
            this.statusEffect = statusEffect;
            this.weight = weight;
        }

        float getWeight() {
            return this.weight;
        }

        RegistryEntry<StatusEffect> getStatusEffect() {
            return this.statusEffect;
        }

        static PartingEffects getRandom(Random rand) {
            float weight = rand.nextFloat();
            for(PartingEffects effect : PartingEffects.values()) {
                weight -= effect.getWeight();
                if(weight <= 0) {
                    return effect;
                }
            }
            return PartingEffects.INFESTED;
        }
        
        StatusEffectInstance createStatusEffectInstance(int duration) {
            return new StatusEffectInstance(this.statusEffect, duration);
        }
    }
}
