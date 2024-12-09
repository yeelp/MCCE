package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

import java.util.List;

public final class SuddenDeathEffect extends AbstractAttributeChaosEffect {

	private static final Identifier HEALTH_ID = MCCE.createIdentifier("sudden_death_health"), DAMAGE_ID = MCCE.createIdentifier("sudden_death_damage");

	private static final int DURATION_MIN = 1300, DURATION_MAX = 2400;
	private static final double DAMAGE_BUFF_AMOUNT = 2048.0;

	public SuddenDeathEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@SuppressWarnings("MagicNumber")
    @Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.heal(0.01f);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.POWER_UP_ID, 1.0f, 1.0f)::send);
	}



	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		player.heal(player.getMaxHealth());
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.POWER_DOWN_ID, 1.0f, 1.0f)::send);
	}



	@Override
	public String getName() {
		return "suddendeath";
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.MAX_HEALTH, new EntityAttributeModifier(HEALTH_ID, -1, Operation.ADD_MULTIPLIED_TOTAL)) {
			@Override
			protected @Nullable EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
				return null;
			}

			@Override
			protected boolean requiresUpdate() {
				return false;
			}
		}, new AttributeModifierFactory(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(DAMAGE_ID, DAMAGE_BUFF_AMOUNT, Operation.ADD_VALUE)) {

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
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.CYCLE_OF_LIFE, ChaosEffects.EQUILIBRIUM, ChaosEffects.UNDEAD, ChaosEffects.BAT_BOMB, ChaosEffects.LOVABLE_PHANTOM, ChaosEffects.HEARTY) && !player.getActiveStatusEffects().containsKey(StatusEffects.WITHER);
	}

}
