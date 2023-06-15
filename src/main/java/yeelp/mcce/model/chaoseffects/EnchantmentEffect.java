package yeelp.mcce.model.chaoseffects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;

public final class EnchantmentEffect extends AbstractInstantChaosEffect {

	private static final List<Enchantment> ENCHANTS = Lists.newArrayList(Enchantments.BINDING_CURSE, Enchantments.AQUA_AFFINITY, Enchantments.BANE_OF_ARTHROPODS, Enchantments.DEPTH_STRIDER, Enchantments.EFFICIENCY, Enchantments.FIRE_PROTECTION, Enchantments.IMPALING, Enchantments.INFINITY, Enchantments.LOOTING, Enchantments.LOYALTY, Enchantments.MENDING, Enchantments.POWER, Enchantments.PROTECTION, Enchantments.SMITE, Enchantments.SILK_TOUCH, Enchantments.SWIFT_SNEAK, Enchantments.UNBREAKING, Enchantments.VANISHING_CURSE);
	
	@Override
	public void applyEffect(PlayerEntity player) {
		ItemStack mainHand = player.getInventory().getMainHandStack();
		if(mainHand.hasEnchantments()) {
			mainHand.removeSubNbt(ItemStack.ENCHANTMENTS_KEY);
		}
		int times = this.getRNG().nextInt(5);
		Collections.shuffle(ENCHANTS, this.getRNG());
		Queue<Enchantment> enchants = new LinkedList<Enchantment>(ENCHANTS);
		do {
			Enchantment enchant = enchants.remove();			
			int level = enchant.isCursed() ? 1 : this.getRNG().nextInt(10) + 1;
			mainHand.addEnchantment(enchant, level);
		}while(times-- > 0);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.ENCHANT_ID, this.getRNG().nextFloat(0.5f, 1.0f), 1.0f)::sendPacket);
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
