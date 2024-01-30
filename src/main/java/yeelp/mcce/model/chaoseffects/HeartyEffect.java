package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;

public class HeartyEffect extends AbstractAttributeChaosEffect {

	private static final UUID HEARTY_ID = UUID.fromString("3345697a-c1a8-4889-8342-555d64fc8ba8");
	private static final String HEARTY_NAME = "Hearty Health";
	private static final float HEARTY_AMOUNT = 2.0f;

	public HeartyEffect() {
		super(800, 1200);
	}
	
	@Override
	public String getName() {
		return "hearty";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(HEARTY_ID, HEARTY_NAME, HEARTY_AMOUNT, Operation.ADDITION)) {
			
			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				if(HeartyEffect.this.durationRemaining() % 40 == 0) {
					return new EntityAttributeModifier(HEARTY_ID, HEARTY_NAME, attribute.getValue() + HEARTY_AMOUNT, Operation.ADDITION);
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
		return !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

	@Override
	protected final void tickAdditionalEffectLogic(PlayerEntity player) {
		super.tickAdditionalEffectLogic(player);
		if(this.durationRemaining() % 40 == 0) {
			player.heal(HEARTY_AMOUNT);			
		}
	}

	@Override
	public final void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.heal(HEARTY_AMOUNT);
	}

	@Override
	public final void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		player.heal(HEARTY_AMOUNT);
	}
	
	
}
