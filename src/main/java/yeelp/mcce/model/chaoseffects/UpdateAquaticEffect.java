package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import yeelp.mcce.util.SimpleUtil;

import java.util.stream.Stream;

public final class UpdateAquaticEffect extends SimpleTimedChaosEffect {

	private static final int DURATION_MIN = 1600, DURATION_MAX = 2400;
	private static final int DAMAGE_INTERVAL = 30;
	public UpdateAquaticEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Stream<RegistryEntry<StatusEffect>> effects = Stream.of(StatusEffects.CONDUIT_POWER, StatusEffects.DOLPHINS_GRACE);
		if(player.isTouchingWaterOrRain()) {
			effects.filter(Predicates.not(player::hasStatusEffect)).forEach((effect) -> player.addStatusEffect(new StatusEffectInstance(effect, this.durationRemaining())));
		}
		else {
			effects.forEach(player::removeStatusEffect);
			if(this.durationRemaining() % DAMAGE_INTERVAL == 0) {
				player.damage(SimpleUtil.getServerWorldFromEntity(player), player.getDamageSources().dryOut(), 2);
			}
		}
	}

	@Override
	public String getName() {
		return "updateaquatic";
	}

	@Override
	public String getDisplayName() {
		return "Update Aquatic";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.isTouchingWaterOrRain();
	}

}
