package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.Optional;

public final class SuperFishEffect extends AbstractEnchantedItemChaosEffect {

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
	public String getName() {
		return "superfish";
	}

	@Override
	public String getDisplayName() {
		return "SuperFish!";
	}

	@Override
	protected Map<RegistryKey<Enchantment>, Integer> getEnchantments() {
		return ENCHANTS;
	}

	@Override
	protected Optional<Text> getCustomName() {
		return Optional.of(Text.empty().formatted(Formatting.RESET).append(this.getDisplayName()).formatted(Formatting.BLUE));
	}

	@Override
	protected Item getItem() {
		return Items.COD;
	}

	@Override
	protected Optional<LoreComponent> getLore() {
		return Optional.empty();
	}
}
