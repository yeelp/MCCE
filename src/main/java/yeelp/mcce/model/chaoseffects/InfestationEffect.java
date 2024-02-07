package yeelp.mcce.model.chaoseffects;

import java.util.UUID;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;

public class InfestationEffect extends AbstractInstantChaosEffect {

	private static final UUID GEORGE_HEALTH_BOOST = UUID.fromString("6d09f80a-804a-4d17-a158-25fcd27b491b");
	private static final String GEORGE_HEALTH_BOOST_NAME = "george_boss_boost";
	private static final int GEORGE_HEALTH_BOOST_AMOUNT = 192;
	
	@Override
	public void applyEffect(PlayerEntity player) {
		int amount = this.getRNG().nextInt(5) + 3;
		World world = player.getWorld();
		do {
			LivingEntity entity;
			if(Math.random() < 0.5) {
				entity = new EndermiteEntity(EntityType.ENDERMITE, world);
			}
			else {
				entity = new SilverfishEntity(EntityType.SILVERFISH, world);
				if(Math.random() < 0.33) {
					entity.setCustomName(Text.empty().append("George").formatted(Formatting.RED));
					entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(GEORGE_HEALTH_BOOST, GEORGE_HEALTH_BOOST_NAME, GEORGE_HEALTH_BOOST_AMOUNT, Operation.ADDITION));
					entity.heal(200.0f);
					entity.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.MUSIC_DISC_BLOCKS));
					((SilverfishEntity) entity).updateDropChances(EquipmentSlot.OFFHAND);
				}
			}
			entity.refreshPositionAndAngles(player.getX() + this.getRNG().nextDouble(-1.3, 1.3), player.getY() + this.getRNG().nextDouble(2, 2.6), player.getZ() + this.getRNG().nextDouble(-1.3, 1.3), 0.0f, 0.0f);
			entity.setVelocity(this.getRNG().nextDouble(-1, 1), this.getRNG().nextDouble(), this.getRNG().nextDouble(-1, 1));
			world.spawnEntity(entity);
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
