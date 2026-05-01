package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

import java.util.List;

public final class SmackDownEffect extends AbstractAttributeChaosEffect {

    private static final int DURATION_MIN = 1000, DURATION_MAX = 1500;
    private static final double FALL_DAMAGE_MULTIPLIER = 4.0, SAFE_FALL_DISTANCE_CHANGE = -3;
    private static final int TICK_INTERVAL = 60;
    private static final float PITCH = 0.8f, VOLUME = 1.0f;
    private static final Identifier GRAVITY_ID = MCCE.createIdentifier("smackdowngravity"), FALL_DAMAGE_MULT_ID = MCCE.createIdentifier("smackdownfalldamage"), SAFE_FALL_DISTANCE_MULT_ID = MCCE.createIdentifier("smackdownsafedistance");
    private boolean activated = false;
    private boolean soundPlayed = false;

    public SmackDownEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected List<AttributeModifierFactory> getAttributeModifierFactories() {
        return List.of(this.new SmackDownAttributeFactory(EntityAttributes.GRAVITY, new EntityAttributeModifier(GRAVITY_ID, 1.0, Operation.ADD_VALUE)),
                this.new SmackDownAttributeFactory(EntityAttributes.FALL_DAMAGE_MULTIPLIER, new EntityAttributeModifier(FALL_DAMAGE_MULT_ID, FALL_DAMAGE_MULTIPLIER, Operation.ADD_MULTIPLIED_BASE)),
                this.new SmackDownAttributeFactory(EntityAttributes.SAFE_FALL_DISTANCE, new EntityAttributeModifier(SAFE_FALL_DISTANCE_MULT_ID, SAFE_FALL_DISTANCE_CHANGE, Operation.ADD_VALUE)));
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !player.isSubmergedInWater() && MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.TO_THE_MOON, ChaosEffects.CLIPPY, ChaosEffects.PRESS_L_TO_LEVITATE, ChaosEffects.RAVE);
    }

    @Override
    public String getName() {
        return "smackdown";
    }

    @Override
    public String getDisplayName() {
        return "Smack Down";
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        super.tickAdditionalEffectLogic(player);
        if(this.activated && !this.soundPlayed) {
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.WIND_BLAST, PITCH, VOLUME)::send);
            this.soundPlayed = true;
        }
    }

    private final class SmackDownAttributeFactory extends AttributeModifierFactory {

        private final EntityAttributeModifier mod;
        private SmackDownAttributeFactory(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier mod) {
            super(attribute, new EntityAttributeModifier(mod.id(), 0.0, mod.operation()));
            this.mod = mod;
        }

        @Override
        protected boolean requiresUpdate() {
            return true;
        }

        @Override
        protected EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
            if(!player.isOnGround() && !player.isSubmergedInWater() && SmackDownEffect.this.durationRemaining() % TICK_INTERVAL == 0) {
                SmackDownEffect.this.activated = true;
                return mod;
            }
            return attribute;
        }
    }
}
