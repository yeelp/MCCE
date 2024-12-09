package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

import java.util.List;

public final class SluggishEffect extends AbstractAttributeChaosEffect {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final double SPEED_DEBUFF = -0.8;
	private static final Identifier SPEED_ID = MCCE.createIdentifier("sluggish_speed");

	private static final int DURATION_MIN = 300, DURATION_MAX = 600;
	public SluggishEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}
	
	private SluggishEffect(int duration) {
		super(duration, duration);
	}

	@Override
	public String getName() {
		return "sluggish";
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		super.applyEffect(player);
		AFFECTED_PLAYERS.add(player);
	}

	@Override
	public void registerCallbacks() {
		super.registerCallbacks();
		PlayerTickCallback.EVENT.register(new SluggishTickHandler());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		super.onEffectEnd(player);
		AFFECTED_PLAYERS.remove(player);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		super.tickAdditionalEffectLogic(player);
		if(!AFFECTED_PLAYERS.tracked(player)) {
			AFFECTED_PLAYERS.add(player);
		}
	}

	@Override
	protected List<AttributeModifierFactory> getAttributeModifierFactories() {
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.MOVEMENT_SPEED, new EntityAttributeModifier(SPEED_ID, SPEED_DEBUFF, Operation.ADD_MULTIPLIED_TOTAL)) {
			
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
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.GOTTA_BLAST);
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return AFFECTED_PLAYERS.tracked(player);
	}
	
	private static final class SluggishTickHandler implements PlayerTickCallback {

		@Override
		public void tick(PlayerEntity player) {
			if(SluggishEffect.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SLUGGISH)) {
				MCCEAPI.mutator.addNewChaosEffect(player, new SluggishEffect(1));
			}
		}
		
	}

}
