package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

public class SluggishEffect extends AbstractAttributeChaosEffect {

	private static final UUID SPEED_UUID = UUID.fromString("7c189470-4f92-4b76-84ec-44fd6b604317");
	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	
	public SluggishEffect() {
		super(300, 600);
	}
	
	protected SluggishEffect(int duration) {
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
		return ImmutableList.of(new AttributeModifierFactory(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(SPEED_UUID, "sluggish_speed", -0.8, Operation.MULTIPLY_TOTAL)) {
			
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
		return !MCCEAPI.accessor.isChaosEffectActive(player, GottaBlastEffect.class);
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return AFFECTED_PLAYERS.tracked(player);
	}
	
	private static final class SluggishTickHandler implements PlayerTickCallback {

		@Override
		public void tick(PlayerEntity player) {
			if(SluggishEffect.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, SluggishEffect.class)) {
				MCCEAPI.mutator.addNewChaosEffect(player, new SluggishEffect(1));
			}
		}
		
	}

}
