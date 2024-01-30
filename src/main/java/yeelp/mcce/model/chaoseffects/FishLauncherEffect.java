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

public class FishLauncherEffect extends AbstractIntervalChaosEffect implements EntityTickCallback {

	private static final Set<Function<World, ? extends LivingEntity>> FISH_CHOICES = Sets.newHashSet();
	private static final Set<UUID> FISHES = Sets.newHashSet();
	
	static {
		FISH_CHOICES.add((world) -> new CodEntity(EntityType.COD, world));
		FISH_CHOICES.add((world) -> new TropicalFishEntity(EntityType.TROPICAL_FISH, world));
		FISH_CHOICES.add((world) -> new SalmonEntity(EntityType.SALMON, world));
	}
	
	public FishLauncherEffect() {
		super(2000, 3000, 1, 5);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		World world = player.getWorld();
		LivingEntity entity = ChaosLib.getRandomElementFrom(FISH_CHOICES, this.getRNG()).apply(world);
		entity.refreshPositionAndAngles(player.getX(), player.getY() + Math.E, player.getZ(), 0.0f, 0.0f);
		if(entity instanceof TropicalFishEntity && PlayerUtils.isPlayerWorldServer(player)) {
			((TropicalFishEntity) entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
		}
		entity.setVelocity(this.getRNG().nextDouble(-6, 6), this.getRNG().nextDouble(0.5, 6), this.getRNG().nextDouble(-6, 6));
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
		return true;
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
				entity.addVelocity(v);
			}
		}
	}

}
