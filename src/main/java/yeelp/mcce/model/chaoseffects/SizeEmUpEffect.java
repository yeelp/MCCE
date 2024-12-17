package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.util.ChaosLib;

import java.util.Objects;

public final class SizeEmUpEffect extends AbstractIntervalChaosEffect {

    private static final int DURATION_MIN = 2200, DURATION_MAX = 3200;
    private static final int INTERVAL_MIN = 40, INTERVAL_MAX = 180;
    private static final int RADIUS = 16;
    private static final double SCALE_MAX = ((ClampedEntityAttribute) EntityAttributes.SCALE.value()).getMaxValue();

    public SizeEmUpEffect() {
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
        player.getWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (entity) -> entity != player).forEach((entity) -> {
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
    public void registerCallbacks() {
        //no callbacks
    }
}
