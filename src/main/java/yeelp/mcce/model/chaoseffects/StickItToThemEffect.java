package yeelp.mcce.model.chaoseffects;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public final class StickItToThemEffect extends SimpleTimedChaosEffect {
	private static final Set<String> NAMES = ImmutableSet.<String>builder().add("Stick it to the Man", "Stick Around for More", "Sticky", "Debug Stick", "Stick to the Plan!", "Short End of the Stick", "Stick to Your Guns!", "Stick to it!", "Stick in the Mud!", "Stick This One Out!", "Stick Together Team!", "Stick With Me", "The River Sticks", "Stick 'Em Up!", "Never Gonna Stick You Up", "Never Gonna Stick You Down", "Fantas-Stick", "Lip-Stick", "Stick Astley", "The Stickening", "Glow Stick", "Pumped Up Sticks", "Chop Sticks", "Joystick", "Hockey Stick", "Stick-er", "Popsicle Stick").build();

	protected StickItToThemEffect() {
		super(6*64, 14*64);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.giveItemStack(this.getStickStack());
	}

	@Override
	public String getName() {
		return "stickittothem";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.getInventory().getEmptySlot() >= 0;
	}
	
	private final ItemStack getStickStack() {
		ItemStack stack = new ItemStack(Items.STICK);
		stack.setCount(this.getRNG().nextInt(1, 3));
		stack.setCustomName(Text.literal(getRandomName(this.getRNG())));
		return stack;
	}
	
	private static final String getRandomName(Random rand) {
		return NAMES.stream().skip(rand.nextLong(0, NAMES.size())).findFirst().get();
	}

}
