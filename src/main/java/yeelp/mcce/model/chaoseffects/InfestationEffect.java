package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import yeelp.mcce.api.MCCEAPI;

public class InfestationEffect extends AbstractInstantChaosEffect {

	@Override
	public void applyEffect(PlayerEntity player) {
		int amount = this.getRNG().nextInt(5) + 3;
		do {
			Entity entity;
			if(Math.random() < 0.5) {
				entity = new EndermiteEntity(EntityType.ENDERMITE, player.world);
			}
			else {
				entity = new SilverfishEntity(EntityType.SILVERFISH, player.world);
				if(Math.random() < 0.33) {
					entity.setCustomName(Text.empty().append("George").formatted(Formatting.RED));
				}
			}
			entity.setPos(player.getX() + this.getRNG().nextDouble(-1.3, 1.3), player.getY() + this.getRNG().nextDouble(2, 2.6), player.getZ() + this.getRNG().nextDouble(-1.3, 1.3));
			entity.setVelocity(this.getRNG().nextDouble(-1, 1), this.getRNG().nextDouble(), this.getRNG().nextDouble(-1, 1));
			player.world.spawnEntity(entity);
		}while(--amount > 0);
	}

	@Override
	public String getName() {
		return "infestation";
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}

}
