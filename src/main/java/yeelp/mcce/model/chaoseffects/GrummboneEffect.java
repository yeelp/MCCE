package yeelp.mcce.model.chaoseffects;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import yeelp.mcce.util.ChaosLib;

public class GrummboneEffect extends SimpleTimedChaosEffect {

	private static final Set<String> NAMES = new HashSet<String>();
	
	static {
		NAMES.add("Dinnerbone");
		NAMES.add("Grumm");
		NAMES.add("Yeelp");
	}
	public GrummboneEffect() {
		super(2000, 3200);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		player.getWorld().getEntitiesByClass(LivingEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 15), (entity) -> !(entity instanceof PlayerEntity)).forEach((entity) -> {
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
