package yeelp.mcce.model.chaoseffects;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public final class ItemEvaporationEffect extends SimpleTimedChaosEffect {

	private static final List<String> NAMES = ImmutableList.of("/dev/null/", "Null", "Void");
	
	protected ItemEvaporationEffect() {
		super(2500, 3700);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		return;
	}

	@Override
	public String getName() {
		return "itemevaporation";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	public static final ItemStack getEvaporatedStack(ItemStack evaporatedStack) {
		ItemStack stack = new ItemStack(Items.STRUCTURE_VOID, evaporatedStack.getCount());
		stack.setNbt(evaporatedStack.getNbt());
		stack.setCustomName(Text.empty().append(NAMES.get((int)(NAMES.size() * Math.random()))));
		return stack;
	}
}
