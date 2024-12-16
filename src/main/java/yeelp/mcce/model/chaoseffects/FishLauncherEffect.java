package yeelp.mcce.model.chaoseffects;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import yeelp.mcce.event.EntityTickCallback;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

public final class FishLauncherEffect extends AbstractIntervalChaosEffect implements EntityTickCallback {

	private static final Set<Function<World, ? extends LivingEntity>> FISH_CHOICES = Sets.newHashSet();
	private static final Set<UUID> FISHES = Sets.newHashSet();
	private static final int DURATION_MIN = 2000, DURATION_MAX = 3000, INTERVAL_MIN = 1, INTERVAL_MAX = 5;
	private static final double VELOCITY_MAX = 6.0, VERTICAL_VELOCITY_MIN = 0.5;
	
	static {
		FISH_CHOICES.add((world) -> new CodEntity(EntityType.COD, world));
		FISH_CHOICES.add((world) -> new TropicalFishEntity(EntityType.TROPICAL_FISH, world));
		FISH_CHOICES.add((world) -> new SalmonEntity(EntityType.SALMON, world));
	}
	
	public FishLauncherEffect() {
		super(DURATION_MIN, DURATION_MAX, INTERVAL_MIN, INTERVAL_MAX);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		LivingEntity entity = ChaosLib.getRandomElementFrom(FISH_CHOICES, this.getRNG()).apply(world);
		entity.refreshPositionAndAngles(player.getX(), player.getY() + Math.E, player.getZ(), 0.0f, 0.0f);
		if(entity instanceof TropicalFishEntity && PlayerUtils.isPlayerWorldServer(player)) {
			((TropicalFishEntity) entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
		}
		entity.setVelocity(this.getRNG().nextDouble(-VELOCITY_MAX, VELOCITY_MAX), this.getRNG().nextDouble(VERTICAL_VELOCITY_MIN, VELOCITY_MAX), this.getRNG().nextDouble(-VELOCITY_MAX, VELOCITY_MAX));
		FISHES.add(entity.getUuid());
		world.spawnEntity(entity);
	}

	@Override
	public String getName() {
		return "fishlauncher";
	}

	@Override
	protected boolean canStack() {
		return true;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return PlayerUtils.doesPlayerHaveValidPosition(player);
	}

	@Override
	public void registerCallbacks() {
		EntityTickCallback.EVENT.register(this);
	}

	@Override
	public void tick(Entity entity) {
		UUID uuid = entity.getUuid();
		if(FISHES.contains(uuid)) {
			FISHES.remove(uuid);
			Vec3d v = entity.getVelocity();
			if(entity.getVelocity().length() < 5) {
				entity.addVelocityInternal(v);
			}
		}
	}

}
