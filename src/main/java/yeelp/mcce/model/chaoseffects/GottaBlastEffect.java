package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;

public class GottaBlastEffect extends AbstractAttributeChaosEffect {

	protected GottaBlastEffect() {
		super(1000, 2000);
	}

	private static final UUID SPEED_UUID = UUID.fromString("c3b5f61c-b243-4d9c-afee-960cd5c72465");
	private static final String MODIFIER_NAME = "gotta_blast_speed";
	
	@Override
	public String getName() {
		return "gottablast";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(SPEED_UUID, MODIFIER_NAME, 0, Operation.ADDITION)) {
			
			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				return new EntityAttributeModifier(SPEED_UUID, MODIFIER_NAME, attribute.getValue() + GottaBlastEffect.this.getRNG().nextDouble(0.000001, 0.001), Operation.ADDITION);
			}
			
			@Override
			protected boolean requiresUpdate() {
				return true;
			}
		});
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
