package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
import com.google.common.collect.Streams;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import yeelp.mcce.util.PlayerUtils;

public final class ButterFingersEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerUtils.getHandItems(player).forEach((stack) -> player.dropItem(stack, true));
		PlayerInventory inv = player.getInventory();
		inv.getMainStacks().set(inv.getSelectedSlot(), ItemStack.EMPTY);
		player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
	}

	@Override
	public String getName() {
		return "butterfingers";
	}

	@Override
	public String getDisplayName() {
		return "Butter Fingers";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return Streams.stream(PlayerUtils.getHandItems(player)).anyMatch(Predicates.not(itemStack -> itemStack != null && itemStack.isEmpty()));
	}

}
