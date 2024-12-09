package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import yeelp.mcce.util.EnchantmentUtils;

import java.util.Map;

public final class SuperFishEffect extends AbstractInstantChaosEffect {

	private static final Map<RegistryKey<Enchantment>, Integer> ENCHANTS = Maps.newHashMap();

	static {
		ENCHANTS.put(Enchantments.SHARPNESS, 10);
		ENCHANTS.put(Enchantments.IMPALING, 10);
		ENCHANTS.put(Enchantments.LUCK_OF_THE_SEA, 10);
		ENCHANTS.put(Enchantments.DEPTH_STRIDER, 3);
		ENCHANTS.put(Enchantments.AQUA_AFFINITY, 1);
		ENCHANTS.put(Enchantments.VANISHING_CURSE, 1);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		ItemStack stack = new ItemStack(Items.COD);
		DynamicRegistryManager manager = player.getRegistryManager();
		ENCHANTS.forEach((enchant, level) -> stack.addEnchantment(EnchantmentUtils.getEntry(enchant, manager), level));
		stack.set(DataComponentTypes.CUSTOM_NAME, Text.empty().formatted(Formatting.RESET).append("SuperFish!").formatted(Formatting.BLUE));
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
