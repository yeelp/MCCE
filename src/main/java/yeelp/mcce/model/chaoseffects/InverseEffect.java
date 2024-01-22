package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

public final class InverseEffect extends AbstractTimedChaosEffect {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private boolean silent = false;
	
	public InverseEffect() {
		super(1200, 1800);
	}
	
	protected InverseEffect(int duration) {
		super(duration, duration);
		this.silent = true;
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player);
		if(!this.silent) {
			PlayerUtils.getServerPlayer(player).ifPresent(new SoundPacket(NetworkingConstants.SoundPacketConstants.INVERSE_START, 1.0f, 1.0f)::sendPacket);
		}
	}

	@Override
	public String getName() {
		return "inverse";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return true;
	}

	@Override
	public void registerCallbacks() {
		PlayerTickCallback.EVENT.register(new InverseTickHandler());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player);
		if(!this.silent && player instanceof ServerPlayerEntity) {
			new SoundPacket(NetworkingConstants.SoundPacketConstants.INVERSE_END, 1.0f, 1.0f).sendPacket((ServerPlayerEntity) player);
		}
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		if(AFFECTED_PLAYERS.tracked(player)) {
			return;
		}
		AFFECTED_PLAYERS.add(player);
	}
	
	public static boolean isAffected(PlayerEntity player) {
		return AFFECTED_PLAYERS.tracked(player);
	}
	
	private static final class InverseTickHandler implements PlayerTickCallback {

		@Override
		public void tick(PlayerEntity player) {
			if(InverseEffect.isAffected(player) && PlayerUtils.isPlayerWorldServer(player) && !MCCEAPI.accessor.isChaosEffectActive(player, InverseEffect.class)) {
				MCCEAPI.mutator.addNewChaosEffect(player, new InverseEffect(1));
			}
		}
		
	}

}
