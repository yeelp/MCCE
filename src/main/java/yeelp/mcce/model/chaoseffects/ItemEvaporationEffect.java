package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import yeelp.mcce.util.ChaosLib;

import java.util.List;

public final class ItemEvaporationEffect extends SimpleTimedChaosEffect {

	private static final List<String> NAMES = ImmutableList.of("/dev/null/", "Null", "Void");

	private static final int DURATION_MIN = 750, DURATION_MAX = 2500;
	public ItemEvaporationEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		//no apply effect
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

	public static ItemStack getEvaporatedStack(ItemStack evaporatedStack) {
		ItemStack stack = evaporatedStack.copyComponentsToNewStack(Items.STRUCTURE_VOID, evaporatedStack.getCount());
		stack.set(DataComponentTypes.CUSTOM_NAME, Text.empty().append(ChaosLib.getRandomElementFrom(NAMES, ChaosLib.getStaticRandomInstance())));
		return stack;
	}
}
