package yeelp.mcce.model.chaoseffects;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;

public class MobRainEffect extends AbstractRainEffect {

	private static final Set<Function<World, ? extends Entity>> VALID_MOBS = new HashSet<Function<World, ? extends Entity>>();
	
	static {
		VALID_MOBS.add((world) -> new ArrowEntity(EntityType.ARROW, world));
		VALID_MOBS.add((world) -> new AxolotlEntity(EntityType.AXOLOTL, world));
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
		VALID_MOBS.add((world) -> {
			MagmaCubeEntity entity = new MagmaCubeEntity(EntityType.MAGMA_CUBE, world);
			entity.setSize((int) (10*Math.random() + 1), true);
			return entity;
		});
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
		VALID_MOBS.add((world) -> {
			SlimeEntity entity = new SlimeEntity(EntityType.SLIME, world);
			entity.setSize((int) (10*Math.random() + 1), true);
			return entity;
		});
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
	
	public MobRainEffect() {
		super(1200, 2400);
	}
	
	@Override
	public String getName() {
		return "mobrain";
	}

	@Override
	protected Entity getEntityToSpawn(PlayerEntity player) {
		Entity e = ChaosLib.getRandomElementFrom(VALID_MOBS, this.getRNG()).apply(player.getWorld());
		e.setPos(player.getX() + this.getRNG().nextDouble(-20, 20), player.getWorld().getTopY(), player.getZ() + this.getRNG().nextDouble(-20, 20));
		e.setVelocity(0.0, 0.1, 0.0);
		if(e instanceof MobEntity && !(e instanceof SlimeEntity)) {
			((MobEntity) e).setPersistent();
		}
		MCCEAPI.mutator.setDespawnTimer(e, this.durationRemaining() + 2400);
		return e;
	}

}
