package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.ServerState;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.ParticlePacket;
import yeelp.mcce.network.SoundPacket;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public class LovablePhantomEffect extends AbstractIntervalTriggeredChaosEffect {

	private int phantoms = 0;
	private static final String PHANTOMS_KEY = "phantoms";
	
	public LovablePhantomEffect() {
		super(2000, 4000, 100, 100, AbstractLastingChaosEffect.getIntInRange(5, 10));
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(PlayerUtils.isPlayerWorldClient(player)) {
			return;
		}
		ServerState state = ServerState.getServerState(player.getServer());
		World world = player.getWorld();
		if(this.getTriggersRemaining() > 0 && world.getEntitiesByClass(PhantomEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 30), (phantom) -> state.getDespawnTimer(phantom.getUuid()).getTimeRemaining() > 0).size() == 0) {
			this.trigger();
			int spawnCount = (int) Math.pow(2.0, this.phantoms++);
			for(int i = 0; i < spawnCount; i++) {
				ChaosLib.getPosWithin(ChaosLib.getBoxCenteredOnPlayerWithRadius(player, 15).offset(0, 15, 0), null, (pos) -> world.isAir(pos) && world.isAir(pos.up()), 100, this.getRNG()).ifPresentOrElse((pos) -> this.summonPhantom(pos, world, player), () -> this.summonPhantom(player.getBlockPos(), world, player));
			}
			new SoundPacket(NetworkingConstants.SoundPacketConstants.LOVABLE_PHANTOM_SPAWN_ID, this.getRNG().nextFloat(0.5f, 2.5f), 1.3f).sendPacket((ServerPlayerEntity) player);
		}
	}

	@Override
	public String getName() {
		return "lovablephantom";
	}

	@Override
	public void registerCallbacks() {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, SuddenDeathEffect.class);
	}
	
	private void summonPhantom(BlockPos pos, World world, PlayerEntity player) {
		PhantomEntity phantom = new PhantomEntity(EntityType.PHANTOM, world);
		phantom.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, this.durationRemaining(), 0, true, true));
		phantom.refreshPositionAndAngles(pos, 0.0f, 0.0f);
		phantom.setTarget(player);
		MCCEAPI.mutator.setDespawnTimer(phantom, this.durationRemaining());
		world.spawnEntity(phantom);
		int particles = this.getRNG().nextInt(3, 5);
		ServerPlayerEntity spe = (ServerPlayerEntity) player;
		for(int i = 0; i++ < particles;	new ParticlePacket(NetworkingConstants.ParticlePacketConstants.HEART, (float) phantom.getX(), (float) phantom.getY(), (float) phantom.getZ(), 0.0f, this.getRNG().nextFloat(2.0f), 0.0f).sendPacket(spe));
	}

	@Override
	public final NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putInt(PHANTOMS_KEY, this.phantoms);
		return nbt;
	}

	@Override
	public final void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.phantoms = nbt.getInt(PHANTOMS_KEY);
	}
	
	

}
