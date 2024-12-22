package yeelp.mcce.model.chaoseffects;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import yeelp.mcce.util.PlayerUtils;

public final class GlintEffect extends AbstractInstantChaosEffect {

	private static final float PERCENT_CHANCE = 0.35f;
	
	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getInventoryIterator(player).forEachRemaining((stack) -> {
			if(!stack.isEmpty() && !stack.hasGlint() && this.getRNG().nextFloat(1.0f) < PERCENT_CHANCE) {
				stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
			}
		});
		ItemStack mainHand = player.getInventory().getMainHandStack();
		if(!mainHand.isEmpty()) {
			mainHand.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
		}
	}

	@Override
	public String getName() {
		return "glint";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !player.getInventory().isEmpty();
	}

}
