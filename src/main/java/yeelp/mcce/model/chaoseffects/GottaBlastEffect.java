package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;

import java.util.List;

public final class GottaBlastEffect extends AbstractAttributeChaosEffect {

	private static final int DURATION_MIN = 1000, DURATION_MAX = 2000;
	private static final double SPEED_INCREASE_MIN = 0.000001, SPEED_INCREASE_MAX = 0.001;
	public GottaBlastEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	private static final Identifier MODIFIER_NAME = MCCE.createIdentifier("gotta_blast_speed");
	
	@Override
	public String getName() {
		return "gottablast";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.MOVEMENT_SPEED, new EntityAttributeModifier(MODIFIER_NAME, 0, Operation.ADD_VALUE)) {
			
			@Override
			protected EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				return new EntityAttributeModifier(MODIFIER_NAME, attribute.value() + GottaBlastEffect.this.getRNG().nextDouble(SPEED_INCREASE_MIN, SPEED_INCREASE_MAX), Operation.ADD_VALUE);
			}
			
			@Override
			protected boolean requiresUpdate() {
				return true;
			}
		});
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SLUGGISH);
	}

}
