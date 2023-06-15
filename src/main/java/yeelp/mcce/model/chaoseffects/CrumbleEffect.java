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
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.PlayerUtils;

public final class CrumbleEffect extends AbstractTimedChaosEffect {

	protected CrumbleEffect() {
		super(2000, 2500);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		return;
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
		return;
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.areAnyChaosEffectsActive(player, MidasTouchEffect.class, ColumnLikeYouSeeEmEffect.class, ChunkyEffect.class);
	}
	
	private static final class OnBlockAttack implements AttackBlockCallback {

		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
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
			return PlayerUtils.getServerPlayerIfServerWorld(player).filter((p) -> p.interactionManager.getGameMode().equals(GameMode.SPECTATOR) && MCCEAPI.accessor.isChaosEffectActive(p, CrumbleEffect.class)).isPresent();
		}
	}

}
