package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.AttributeUtils;

public final class MyBodyAsAShieldEffect extends AbstractInstantChaosEffect {

	private static final float ABSORPTION_CAP = 20.0f;
	private static final Identifier MAX_ID = MCCE.createIdentifier("max_absorption_boost");
	@Override
	public void applyEffect(PlayerEntity player) {
		AttributeUtils.addAttributeModifierIfNotPresent(player, EntityAttributes.MAX_ABSORPTION, new EntityAttributeModifier(MAX_ID, ABSORPTION_CAP, Operation.ADD_VALUE));
		player.setAbsorptionAmount(this.getRNG().nextFloat(1.0f, ABSORPTION_CAP));
	}

	@Override
	public String getName() {
		return "mybodyasashield";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getAbsorptionAmount() == 0;
	}

}
