package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import yeelp.mcce.util.ChaosLib;

import java.util.Set;

public final class GrummboneEffect extends SimpleTimedChaosEffect {

	private static final Set<String> NAMES = Sets.newHashSet("Dinnerbone", "Grumm", "Yeelp");
	private static final int DURATION_MIN = 2000, DURATION_MAX = 3200;
	private static final int EFFECT_RADIUS = 15;

	public GrummboneEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getEntityWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, EFFECT_RADIUS), (entity) -> !(entity instanceof PlayerEntity)).forEach((entity) -> {
			if(NAMES.contains(entity.getName().getString())) {
				entity.setCustomName(null);
			}
			else {
				entity.setCustomName(Text.of(ChaosLib.getRandomElementFrom(NAMES, this.getRNG())));				
			}
		});
	}

	@Override
	public String getName() {
		return "grummbone";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

}
