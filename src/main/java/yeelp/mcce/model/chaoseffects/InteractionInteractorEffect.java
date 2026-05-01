package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;

import java.util.List;

public final class InteractionInteractorEffect extends AbstractAttributeChaosEffect {

    private static final int DURATION_MIN = 2000, DURATION_MAX = 3600;
    private static final Identifier BLOCK_INTERACTION_ID = MCCE.createIdentifier("interactioninteractor_block");
    private static final Identifier ENTITY_INTERACTION_ID = MCCE.createIdentifier("interactioninteractor_entity");

    public InteractionInteractorEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected List<AttributeModifierFactory> getAttributeModifierFactories() {
        return ImmutableList.of(new InteractionAttributeModifierFactory(EntityAttributes.BLOCK_INTERACTION_RANGE, BLOCK_INTERACTION_ID), new InteractionAttributeModifierFactory(EntityAttributes.ENTITY_INTERACTION_RANGE, ENTITY_INTERACTION_ID));
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public String getName() {
        return "interactioninteractor";
    }

    @Override
    public String getDisplayName() {
        return "Interaction Interactor";
    }

    private static final class InteractionAttributeModifierFactory extends AttributeModifierFactory {

        private int t = 0;
        InteractionAttributeModifierFactory(RegistryEntry<EntityAttribute> attribute, Identifier id) {
            super(attribute, new EntityAttributeModifier(id, 0.0f, Operation.ADD_VALUE));
        }

        @Override
        protected boolean requiresUpdate() {
            return true;
        }

        @Override
        protected EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
            return new EntityAttributeModifier(this.getID(), this.getMod(), Operation.ADD_VALUE);
        }

        @SuppressWarnings("MagicNumber")
        private double getMod() {
            return 32 * Math.sin(++this.t) + 32;
        }
    }
}
