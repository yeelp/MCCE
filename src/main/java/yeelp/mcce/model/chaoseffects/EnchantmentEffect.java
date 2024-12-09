package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Lists;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.EnchantmentUtils;
import yeelp.mcce.util.PlayerUtils;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public final class EnchantmentEffect extends AbstractInstantChaosEffect {

	private static final List<RegistryKey<Enchantment>> ENCHANTS = Lists.newArrayList(Enchantments.BINDING_CURSE, Enchantments.AQUA_AFFINITY, Enchantments.BANE_OF_ARTHROPODS, Enchantments.DEPTH_STRIDER, Enchantments.EFFICIENCY, Enchantments.FIRE_PROTECTION, Enchantments.IMPALING, Enchantments.INFINITY, Enchantments.LOOTING, Enchantments.LOYALTY, Enchantments.MENDING, Enchantments.POWER, Enchantments.PROTECTION, Enchantments.SMITE, Enchantments.SILK_TOUCH, Enchantments.SWIFT_SNEAK, Enchantments.UNBREAKING, Enchantments.VANISHING_CURSE);
	private static final float PITCH_MIN = 0.5f, PITCH_MAX = 1.0f, VOLUME = 1.0f;

	@Override
	public void applyEffect(PlayerEntity player) {
		ItemStack mainHand = player.getInventory().getMainHandStack();
		if(mainHand.hasEnchantments()) {
			mainHand.remove(DataComponentTypes.ENCHANTMENTS);
		}
		int times = this.getRNG().nextInt(5);
		Collections.shuffle(ENCHANTS, this.getRNG());
		Queue<RegistryKey<Enchantment>> enchants = Lists.newLinkedList(ENCHANTS);
		DynamicRegistryManager manager = player.getRegistryManager();
		do {
			RegistryKey<Enchantment> key = enchants.remove();
			RegistryEntry<Enchantment> enchant;
			int level = (enchant = EnchantmentUtils.getEntry(key, manager)).value().getMaxLevel() == 1 ? 1 : this.getRNG().nextInt(10) + 1;
			mainHand.addEnchantment(enchant, level);
		}while(times-- > 0);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.ENCHANT_ID, this.getRNG().nextFloat(PITCH_MIN, PITCH_MAX), VOLUME)::send);
	}

	@Override
	public String getName() {
		return "enchantment";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !player.getInventory().getMainHandStack().isEmpty();
	}

}
