package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.OnPlayerDeathCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.PlayerUtils;

import java.util.List;

public final class SuddenDeathEffect extends AbstractAttributeChaosEffect {

	private static final Identifier HEALTH_ID = MCCE.createIdentifier("sudden_death_health"), DAMAGE_ID = MCCE.createIdentifier("sudden_death_damage");

	private static final int DURATION_MIN = 1300, DURATION_MAX = 2400;
	private static final double DAMAGE_BUFF_AMOUNT = 2048.0;
	private static final int DEATH_CAP = 20, DEATH_LOOP_TICK_LENGTH = 120, DEATH_LOOP_COUNT_THRESHOLD = 5;
	private int deaths = 0, deathLoopCount = 0, lastDeathTimestamp;
	private static final String DEATH_COUNT = "death_count", DEATH_LOOP_COUNT = "death_loop_count", LAST_DEATH_TIME = "last_death_timestamp";

	public SuddenDeathEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@SuppressWarnings("MagicNumber")
    @Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		player.heal(0.01f);
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, this.durationRemaining(), 200, true, true));
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.POWER_UP_ID, 1.0f, 1.0f)::send);
	}



	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		player.heal(player.getMaxHealth());
		player.removeStatusEffect(StatusEffects.STRENGTH);
		PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(NetworkingConstants.SoundPacketConstants.POWER_DOWN_ID, 1.0f, 1.0f)::send);
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putInt(DEATH_COUNT, this.deaths);
		nbt.putInt(DEATH_LOOP_COUNT, this.deathLoopCount);
		nbt.putInt(LAST_DEATH_TIME, this.lastDeathTimestamp);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.deaths = nbt.getInt(DEATH_COUNT).orElse(0);
		this.deathLoopCount = nbt.getInt(DEATH_LOOP_COUNT).orElse(0);
		this.lastDeathTimestamp = nbt.getInt(LAST_DEATH_TIME).orElse(0);
	}

	@Override
	public String getName() {
		return "suddendeath";
	}

	@Override
	public String getDisplayName() {
		return "Sudden Death";
	}

	@Override
	public void registerCallbacks() {
		super.registerCallbacks();
		OnPlayerDeathCallback.EVENT.register((player, source) -> {
			MCCEAPI.accessor.getChaosEffect(player, SuddenDeathEffect.class).ifPresent((effect) -> {
				if(Math.abs(effect.lastDeathTimestamp - player.age) > DEATH_LOOP_TICK_LENGTH) {
					effect.deathLoopCount = -1;
					effect.lastDeathTimestamp = player.age;
				}
				if(++effect.deaths >= DEATH_CAP || ++effect.deathLoopCount >= DEATH_LOOP_COUNT_THRESHOLD) {
					this.setDuration(1);
					this.tickEffect(player);
				}
			});
		});
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
