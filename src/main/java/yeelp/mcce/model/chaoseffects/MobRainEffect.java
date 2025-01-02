package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.MCCESpawnCap;

import java.util.Set;
import java.util.function.Function;

public final class MobRainEffect extends AbstractRainEffect {

	private static final Set<Function<World, ? extends Entity>> VALID_MOBS = Sets.newHashSet();
	
	static {
		VALID_MOBS.add((world) -> new ArrowEntity(EntityType.ARROW, world));
		VALID_MOBS.add((world) -> new AxolotlEntity(EntityType.AXOLOTL, world));
		VALID_MOBS.add((world) -> new BoggedEntity(EntityType.BOGGED, world));
		VALID_MOBS.add((world) -> new CamelEntity(EntityType.CAMEL, world));
		VALID_MOBS.add((world) -> new CaveSpiderEntity(EntityType.CAVE_SPIDER, world));
		VALID_MOBS.add((world) -> new CodEntity(EntityType.COD, world));
		VALID_MOBS.add((world) -> new CowEntity(EntityType.COW, world));
		VALID_MOBS.add((world) -> new CreeperEntity(EntityType.CREEPER, world));
		VALID_MOBS.add((world) -> new DolphinEntity(EntityType.DOLPHIN, world));
		VALID_MOBS.add((world) -> new DonkeyEntity(EntityType.DONKEY, world));
		VALID_MOBS.add((world) -> new DrownedEntity(EntityType.DROWNED, world));
		VALID_MOBS.add((world) -> new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world));
		VALID_MOBS.add((world) -> new EndermanEntity(EntityType.ENDERMAN, world));
		VALID_MOBS.add((world) -> new EndermiteEntity(EntityType.ENDERMITE, world));
		VALID_MOBS.add((world) -> new EvokerEntity(EntityType.EVOKER, world));
		VALID_MOBS.add((world) -> new ExperienceBottleEntity(EntityType.EXPERIENCE_BOTTLE, world));
		VALID_MOBS.add((world) -> new FoxEntity(EntityType.FOX, world));
		VALID_MOBS.add((world) -> new FrogEntity(EntityType.FROG, world));
		VALID_MOBS.add((world) -> new GlowSquidEntity(EntityType.GLOW_SQUID, world));
		VALID_MOBS.add((world) -> new GoatEntity(EntityType.GOAT, world));
		VALID_MOBS.add((world) -> new GuardianEntity(EntityType.GUARDIAN, world));
		VALID_MOBS.add((world) -> new HoglinEntity(EntityType.HOGLIN, world));
		VALID_MOBS.add((world) -> new HorseEntity(EntityType.HORSE, world));
		VALID_MOBS.add((world) -> new HuskEntity(EntityType.HUSK, world));
		VALID_MOBS.add((world) -> new IllusionerEntity(EntityType.ILLUSIONER, world));
		VALID_MOBS.add((world) -> new LlamaEntity(EntityType.LLAMA, world));
		VALID_MOBS.add((world) -> new MooshroomEntity(EntityType.MOOSHROOM, world));
		VALID_MOBS.add((world) -> new MuleEntity(EntityType.MULE, world));
		VALID_MOBS.add((world) -> new PandaEntity(EntityType.PANDA, world));
		VALID_MOBS.add((world) -> new PigEntity(EntityType.PIG, world));
		VALID_MOBS.add((world) -> new PiglinEntity(EntityType.PIGLIN, world));
		VALID_MOBS.add((world) -> new PiglinBruteEntity(EntityType.PIGLIN_BRUTE, world));
		VALID_MOBS.add((world) -> new PolarBearEntity(EntityType.POLAR_BEAR, world));
		VALID_MOBS.add((world) -> new PufferfishEntity(EntityType.PUFFERFISH, world));
		VALID_MOBS.add((world) -> new RabbitEntity(EntityType.RABBIT, world));
		VALID_MOBS.add((world) -> new RavagerEntity(EntityType.RAVAGER, world));
		VALID_MOBS.add((world) -> new SalmonEntity(EntityType.SALMON, world));
		VALID_MOBS.add((world) -> new SheepEntity(EntityType.SHEEP, world));
		VALID_MOBS.add((world) -> new SilverfishEntity(EntityType.SILVERFISH, world));
		VALID_MOBS.add((world) -> new SkeletonEntity(EntityType.SKELETON, world));
		VALID_MOBS.add((world) -> new SkeletonHorseEntity(EntityType.SKELETON_HORSE, world));
		VALID_MOBS.add((world) -> new SnifferEntity(EntityType.SNIFFER, world));
		VALID_MOBS.add((world) -> new SnowGolemEntity(EntityType.SNOW_GOLEM, world));
		VALID_MOBS.add((world) -> new SpectralArrowEntity(EntityType.SPECTRAL_ARROW, world));
		VALID_MOBS.add((world) -> new SpiderEntity(EntityType.SPIDER, world));
		VALID_MOBS.add((world) -> new SquidEntity(EntityType.SQUID, world));
		VALID_MOBS.add((world) -> new StrayEntity(EntityType.STRAY, world));
		VALID_MOBS.add((world) -> new StriderEntity(EntityType.STRIDER, world));
		VALID_MOBS.add((world) -> new TadpoleEntity(EntityType.TADPOLE, world));
		VALID_MOBS.add((world) -> new TraderLlamaEntity(EntityType.TRADER_LLAMA, world));
		VALID_MOBS.add((world) -> new TridentEntity(EntityType.TRIDENT, world));
		VALID_MOBS.add((world) -> new TropicalFishEntity(EntityType.TROPICAL_FISH, world));
		VALID_MOBS.add((world) -> new TurtleEntity(EntityType.TURTLE, world));
		VALID_MOBS.add((world) -> new VillagerEntity(EntityType.VILLAGER, world));
		VALID_MOBS.add((world) -> new VindicatorEntity(EntityType.VINDICATOR, world));
		VALID_MOBS.add((world) -> new WanderingTraderEntity(EntityType.WANDERING_TRADER, world));
		VALID_MOBS.add((world) -> new WardenEntity(EntityType.WARDEN, world));
		VALID_MOBS.add((world) -> new WitchEntity(EntityType.WITCH, world));
		VALID_MOBS.add((world) -> new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world));
		VALID_MOBS.add((world) -> new WolfEntity(EntityType.WOLF, world));
		VALID_MOBS.add((world) -> new ZoglinEntity(EntityType.ZOGLIN, world));
		VALID_MOBS.add((world) -> new ZombieEntity(EntityType.ZOMBIE, world));
		VALID_MOBS.add((world) -> new ZombieHorseEntity(EntityType.ZOMBIE_HORSE, world));
		VALID_MOBS.add((world) -> new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world));
		VALID_MOBS.add((world) -> new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world));
		
	}

	private static final int DURATION_MIN = 1200, DURATION_MAX = 2400;
	private static final int RADIUS = 20;
	private static final double INITIAL_Y_VELOCITY = 0.1;
	public MobRainEffect() {
		super(DURATION_MIN, DURATION_MAX);
	}
	
	@Override
	public String getName() {
		return "mobrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		Entity e = ChaosLib.getRandomElementFrom(VALID_MOBS, this.getRNG()).apply(player.getWorld());
		e.setPos(player.getX() + this.getRNG().nextDouble(-RADIUS, RADIUS), player.getWorld().getTopYInclusive(), player.getZ() + this.getRNG().nextDouble(-RADIUS, RADIUS));
		e.setVelocity(0.0, INITIAL_Y_VELOCITY, 0.0);
		if(e instanceof MobEntity mob) {
			mob.setPersistent();
		}
		MCCEAPI.mutator.setDespawnTimer(e, this.durationRemaining() + DURATION_MAX/2);
		return e;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return super.isApplicableIgnoringStackability(player) && !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.SIZE_EM_UP);
	}

	@Override
	protected MCCESpawnCap getSpawnCap() {
		return MCCESpawnCap.MOB;
	}
}
