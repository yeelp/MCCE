package yeelp.mcce.model.chaoseffects;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SuperFishEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		ItemStack stack = new ItemStack(Items.COD);
		stack.addEnchantment(Enchantments.SHARPNESS, 10);
		stack.addEnchantment(Enchantments.IMPALING, 10);
		stack.addEnchantment(Enchantments.LUCK_OF_THE_SEA, 10);
		stack.addEnchantment(Enchantments.DEPTH_STRIDER, 3);
		stack.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
		stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
		stack.setCustomName(Text.empty().formatted(Formatting.RESET).append("SuperFish!").formatted(Formatting.BLUE));
		player.giveItemStack(stack);
	}

	@Override
	public String getName() {
		return "superfish";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getInventory().getEmptySlot() >= 0;
	}

}
