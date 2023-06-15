package yeelp.mcce.model.chaoseffects;

import java.util.function.Predicate;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import yeelp.mcce.util.Tracker;

public final class InsomniaEffect extends AbstractTimedChaosEffect {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final Predicate<World> IS_APPLICABLE = (w) -> w.getDimension().bedWorks() && w.isNight();

	protected InsomniaEffect() {
		super(2000, 3000);
	}
	
	@Override
	public void applyEffect(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player.getUuid());
	}

	@Override
	public String getName() {
		return "insomnia";
	}

	@Override
	public void registerCallbacks() {
		EntitySleepEvents.START_SLEEPING.register((sleeper, pos) -> {
			if(sleeper instanceof PlayerEntity && AFFECTED_PLAYERS.tracked(sleeper.getUuid())) {
				((PlayerEntity) sleeper).wakeUp(false, true);
				sleeper.sendMessage(Text.literal("No sleeping on the job!"));
			}
		});
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player.getUuid());
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return IS_APPLICABLE.test(player.getWorld());
	}

	@Override
	protected boolean canStack() {
		return false;
	}

}
