package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

import java.util.List;

public final class ArmorerEffect extends AbstractAttributeChaosEffect {

    private static final Identifier ARMORER_ID = MCCE.createIdentifier("armorer");
    private static final double ARMOR_MAX = 20;
    public ArmorerEffect() {
        super(AbstractStatCycleEffect.DURATION_MIN, AbstractStatCycleEffect.DURATION_MAX);
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    protected List<AttributeModifierFactory> getAttributeModifierFactories() {
        return List.of(new AttributeModifierFactory(EntityAttributes.ARMOR, new EntityAttributeModifier(ARMORER_ID, 0.0, Operation.ADD_VALUE)) {
            @Override
            protected boolean requiresUpdate() {
                return true;
            }

            @Override
            protected EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
                @SuppressWarnings("DataFlowIssue")
                double armor = player.getAttributeInstance(EntityAttributes.ARMOR).getValue();
                double armorMin = -armor + 1, armorMax = ARMOR_MAX - armor;
                return new EntityAttributeModifier(ARMORER_ID, ArmorerEffect.this.getRNG().nextDouble(armorMin, armorMax), Operation.ADD_VALUE);
            }
        });
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        super.tickAdditionalEffectLogic(player);
        PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.UI_BUTTON_CLICK_ID, this.getRNG().nextFloat(0.0f, AbstractStatCycleEffect.PITCH_MAX), AbstractStatCycleEffect.VOLUME)::send);
    }

    @Override
    public String getName() {
        return "armorer";
    }
}
