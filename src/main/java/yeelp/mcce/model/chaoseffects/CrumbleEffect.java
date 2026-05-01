package yeelp.mcce.model.chaoseffects;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.PlayerUtils;

public final class CrumbleEffect extends AbstractTimedChaosEffect {

	private static final int DURATION_MIN = 2000, DURATION_MAX = 2500;
	public CrumbleEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		//no apply effect
	}

	@Override
	public String getName() {
		return "crumble";
	}

	@Override
	public void registerCallbacks() {
		AttackBlockCallback.EVENT.register(new OnBlockAttack());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		//no on end effect
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		//no effect logic
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.MIDAS_TOUCH, ChaosEffects.COLUMN_LIKE_YOU_SEE_EM, ChaosEffects.CHUNKY, ChaosEffects.UNBREAKABLE);
	}

	private static final class OnBlockAttack implements AttackBlockCallback {

		@Override
		public @NotNull ActionResult interact(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull BlockPos pos, @NotNull Direction direction) {
			if(isEffectActiveAndContextApplicable(player)) {
				BlockState state = world.getBlockState(pos);
				boolean drop = true;
				if(state.isToolRequired()) {
					drop = player.getMainHandStack().isSuitableFor(state);
				}
				world.breakBlock(pos, drop, player);
				player.getMainHandStack().postMine(world, state, pos, player);
			}
			return ActionResult.PASS;			
		}
		
		private static boolean isEffectActiveAndContextApplicable(PlayerEntity player) {
			return PlayerUtils.getServerPlayerIfServerWorld(player).filter((p) -> !p.interactionManager.getGameMode().equals(GameMode.SPECTATOR) && MCCEAPI.accessor.isChaosEffectActive(p, ChaosEffects.CRUMBLE)).isPresent();
		}
	}

}
