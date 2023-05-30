package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
import com.google.common.collect.Streams;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public final class ButterFingersEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getHandItems().forEach((stack) -> {
			player.dropItem(stack, true);
		});
		PlayerInventory inv = player.getInventory();
		inv.main.set(inv.selectedSlot, ItemStack.EMPTY);
		inv.offHand.set(0, ItemStack.EMPTY);
	}

	@Override
	public String getName() {
		return "butterfingers";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return Streams.stream(player.getHandItems()).anyMatch(Predicates.not(ItemStack::isEmpty));
	}

}
