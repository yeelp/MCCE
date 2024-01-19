package yeelp.mcce.model.chaoseffects;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.Queue;
import java.util.stream.IntStream;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class ReorganizeEffect extends AbstractInstantChaosEffect {

	private static final int INVENTORY_SIZE_TOTAL = PlayerInventory.MAIN_SIZE + PlayerInventory.ARMOR_SLOTS.length + 1;
	private static final List<Integer> ORDER = Lists.newArrayList(IntStream.range(0, INVENTORY_SIZE_TOTAL).iterator());
	
	@Override
	public void applyEffect(PlayerEntity player) {
		PlayerInventory inv = player.getInventory();
		Collections.shuffle(ORDER, this.getRNG());
		Iterator<ItemStack> invIt = Iterators.concat(inv.main.iterator(), inv.armor.iterator(), inv.offHand.iterator());
		Iterators.filter(invIt, (stack) -> !stack.isEmpty());
		Queue<ItemStack> temp = Lists.newLinkedList();
		invIt.forEachRemaining((stack) -> temp.add(stack.copy()));
		inv.clear();
		OfInt order = ORDER.stream().mapToInt((i) -> i).iterator();
		for(; temp.size() > 0; inv.setStack(order.nextInt(), temp.poll()));	
	}

	@Override
	public String getName() {
		return "reorganize";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !player.getInventory().isEmpty();
	}

	
}
