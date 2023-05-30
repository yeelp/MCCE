package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;

public final class GlintEffect extends AbstractInstantChaosEffect {

	public static final NbtByte GLINT = NbtByte.ONE;
	public static final String GLINT_TAG = "mcce:glint";
	private static final float PERCENT_CHANCE = 0.35f;
	
	@Override
	public void applyEffect(PlayerEntity player) {
		player.getInventory().main.forEach((stack) -> {
			if(!stack.isEmpty() && !stack.hasGlint() && this.getRNG().nextFloat(1.0f) < PERCENT_CHANCE) {
				stack.getOrCreateNbt().put(GLINT_TAG, GLINT);
			}
		});
		ItemStack mainHand = player.getInventory().getMainHandStack();
		if(!mainHand.isEmpty()) {
			mainHand.getOrCreateNbt().put(GLINT_TAG, GLINT);
		}
	}

	@Override
	public String getName() {
		return "glint";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !player.getInventory().main.isEmpty();
	}

}
