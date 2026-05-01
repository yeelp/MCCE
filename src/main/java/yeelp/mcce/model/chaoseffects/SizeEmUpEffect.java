package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;

import java.util.Objects;

public final class SizeEmUpEffect extends AbstractIntervalChaosEffect {

    private static final int DURATION_MIN = 2200, DURATION_MAX = 3200;
    private static final int INTERVAL_MIN = 40, INTERVAL_MAX = 180;
    private static final int RADIUS = 16;
    private static final double SCALE_MAX = ((ClampedEntityAttribute) EntityAttributes.SCALE.value()).getMaxValue();
    private static final double SCALE_KILL_THRESHOLD_ON_EFFECT_END = SCALE_MAX/2;

    public SizeEmUpEffect() {
        super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MOB_RAIN);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        player.getEntityWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), SizeEmUpEffect::isValidEntity).forEach((entity) -> {
            EntityAttributeInstance instance = Objects.requireNonNull(entity.getAttributeInstance(EntityAttributes.SCALE));
            double mod = this.getRNG().nextDouble(1, SCALE_MAX);
            mod = this.getRNG().nextBoolean() ? 1/mod : mod;
            instance.setBaseValue(mod);
        });
    }

    @Override
    public String getName() {
        return "sizeemup";
    }

    @Override
    public String getDisplayName() {
        return "Size 'Em Up";
    }

    @Override
    public void registerCallbacks() {
        //no callbacks
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        player.getEntityWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS * 2), SizeEmUpEffect::isValidEntity).forEach((entity) -> {
            if(this.getRNG().nextBoolean() && Objects.requireNonNull(entity.getAttributeInstance(EntityAttributes.SCALE)).getValue() >= SCALE_KILL_THRESHOLD_ON_EFFECT_END) {
                entity.discard();
            }
        });
    }

    private static boolean isValidEntity(LivingEntity entity) {
        return !(entity instanceof PlayerEntity) && !(entity instanceof SlimeEntity);
    }
}
