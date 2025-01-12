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
import yeelp.mcce.network.NetworkingConstants.ParticlePacketConstants;
import yeelp.mcce.network.ParticlePayload;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.MCCESpawnCap;
import yeelp.mcce.util.PlayerUtils;

import java.util.Objects;

public final class LovablePhantomEffect extends AbstractIntervalTriggeredChaosEffect {

	private int phantoms = 0;
	private static final String PHANTOMS_KEY = "phantoms";
	private static final int DURATION_MIN = 2000, DURATION_MAX = 4000, INTERVAL = 100;
	private static final int RADIUS = 30;
	private static final float PITCH_MIN = 0.5f, PITCH_MAX= 2.5f, VOLUME = 1.3f;
	private static final float PARTICLE_Y_SPEED_MAX = 2.0f;
	private static final byte HEART_ID = ParticlePacketConstants.getId(ParticlePacketConstants.HEART);
	
	public LovablePhantomEffect() {
		super(DURATION_MIN, DURATION_MAX, INTERVAL, INTERVAL, AbstractLastingChaosEffect.getIntInRange(5, 10));
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(PlayerUtils.isPlayerWorldClient(player)) {
			return;
		}
		ServerState state = ServerState.getServerState(Objects.requireNonNull(player.getServer()));
		World world = player.getWorld();
		if(this.getTriggersRemaining() > 0 && world.getEntitiesByClass(PhantomEntity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS), (phantom) -> state.getDespawnTimer(phantom.getUuid()).getTimeRemaining() > 0).isEmpty()) {
			this.trigger();
			int spawnCount = (int) Math.pow(2, this.phantoms++);
			for(int i = 0; i < spawnCount; i++) {
				ChaosLib.getPosWithin(ChaosLib.getBoxCenteredOnPlayerWithRadius(player, RADIUS/2).offset(0, (double) RADIUS /2, 0), null, (pos) -> world.isAir(pos) && world.isAir(pos.up()), 100, this.getRNG()).ifPresentOrElse((pos) -> this.summonPhantom(pos, world, player), () -> this.summonPhantom(player.getBlockPos(), world, player));
			}
			new SoundPayload(NetworkingConstants.SoundPacketConstants.LOVABLE_PHANTOM_SPAWN_ID, this.getRNG().nextFloat(PITCH_MIN, PITCH_MAX), VOLUME).send((ServerPlayerEntity) player);
		}
	}

	@Override
	public String getName() {
		return "lovablephantom";
	}

	@Override
	public void registerCallbacks() {
		//no callbacks
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SUDDEN_DEATH);
	}
	
	private void summonPhantom(BlockPos pos, World world, PlayerEntity player) {
		PhantomEntity phantom = new PhantomEntity(EntityType.PHANTOM, world);
		phantom.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, this.durationRemaining(), 0, true, true));
		phantom.refreshPositionAndAngles(pos, 0.0f, 0.0f);
		phantom.setTarget(player);
		MCCEAPI.mutator.setDespawnTimer(phantom, this.durationRemaining());
		MCCESpawnCap.MOB.attemptEntitySpawn(world, phantom);
		int particles = this.getRNG().nextInt(3, 5);
		ServerPlayerEntity spe = (ServerPlayerEntity) player;
		for(int i = 0; i < particles; i++) {
			float[] offset = ParticlePacketConstants.HEART.calculatePositionOffset((float) phantom.getX(), (float) phantom.getY(), (float) phantom.getZ());
			new ParticlePayload(HEART_ID, offset[0], offset[1], offset[2], 0, this.getRNG().nextFloat(PARTICLE_Y_SPEED_MAX), 0.0f).send(spe);
		}
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putInt(PHANTOMS_KEY, this.phantoms);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.phantoms = nbt.getInt(PHANTOMS_KEY);
	}

	@Override
	public boolean canBeFirstEffect() {
		return false;
	}
}
