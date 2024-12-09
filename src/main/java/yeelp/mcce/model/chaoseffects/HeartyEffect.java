package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;

import java.util.List;

public final class HeartyEffect extends AbstractAttributeChaosEffect {

	private static final Identifier HEARTY_NAME = MCCE.createIdentifier("hearty_health");
	private static final float HEARTY_AMOUNT = 2.0f;
	private static final int DURATION_MIN = 800, DURATION_MAX = 1200;
	private static final int TICK_INTERVAL = 40;

	public HeartyEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}
	
	@Override
	public String getName() {
		return "hearty";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.MAX_HEALTH, new EntityAttributeModifier(HEARTY_NAME, HEARTY_AMOUNT, Operation.ADD_VALUE)) {
			
			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				if(HeartyEffect.this.durationRemaining() % TICK_INTERVAL == 0) {
					return new EntityAttributeModifier(HEARTY_NAME, attribute.value() + HEARTY_AMOUNT, Operation.ADD_VALUE);
				}
				return attribute;
			}
			
			@Override
			protected boolean requiresUpdate() {
				return true;
			}
		});
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SUDDEN_DEATH);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		super.tickAdditionalEffectLogic(player);
		if(this.durationRemaining() % TICK_INTERVAL == 0) {
			player.heal(HEARTY_AMOUNT);			
		}
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.heal(HEARTY_AMOUNT);
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		player.heal(HEARTY_AMOUNT);
	}
	
	
}
