package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;

public final class SuddenDeathEffect extends AbstractAttributeChaosEffect {
	private static final UUID HEALTH_UUID = UUID.fromString("cee68503-98f7-443b-a041-596d567150da"),
			DAMAGE_UUID = UUID.fromString("4f5d51ed-68f6-4955-a3b3-0d5fded9633d");

	protected SuddenDeathEffect() {
		super(1300, 2400);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.heal(0.01f);
		if(player instanceof ServerPlayerEntity) {
			new SoundPacket(NetworkingConstants.SoundPacketConstants.POWER_UP_ID, 1.0f, 1.0f).sendPacket((ServerPlayerEntity) player);			
		}
	}



	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		player.heal(player.getMaxHealth());
		if(player instanceof ServerPlayerEntity) {
			new SoundPacket(NetworkingConstants.SoundPacketConstants.POWER_DOWN_ID, 1.0f, 1.0f).sendPacket((ServerPlayerEntity) player);			
		}
	}



	@Override
	public String getName() {
		return "suddendeath";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(HEALTH_UUID, "sudden_death_health", -1, Operation.MULTIPLY_TOTAL)) {
			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				return null;
			}

			@Override
			protected boolean requiresUpdate() {
				return false;
			}
		}, new AttributeModifierFactory(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(DAMAGE_UUID, "sudden_death_damage", 2048.0, Operation.ADDITION)) {

			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				return null;
			}

			@Override
			protected boolean requiresUpdate() {
				return false;
			}
		});
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, CycleOfLifeEffect.class) && !player.getActiveStatusEffects().containsKey(StatusEffects.WITHER);
	}

}
