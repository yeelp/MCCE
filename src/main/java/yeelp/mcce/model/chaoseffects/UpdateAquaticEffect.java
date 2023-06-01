package yeelp.mcce.model.chaoseffects;

import java.util.stream.Stream;

import com.google.common.base.Predicates;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public final class UpdateAquaticEffect extends SimpleTimedChaosEffect {

	protected UpdateAquaticEffect() {
		super(1600, 2400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		Stream<StatusEffect> effects = Stream.of(StatusEffects.CONDUIT_POWER, StatusEffects.DOLPHINS_GRACE);
		if(player.isWet()) {
			effects.filter(Predicates.not(player::hasStatusEffect)).forEach((effect) -> player.addStatusEffect(new StatusEffectInstance(effect, this.durationRemaining())));
		}
		else {
			effects.forEach(player::removeStatusEffect);
			if(this.durationRemaining() % 30 == 0) {
				player.damage(player.getDamageSources().dryOut(), 2);
			}
		}
	}

	@Override
	public String getName() {
		return "updateaquatic";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return player.isWet();
	}

}
