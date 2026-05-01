package yeelp.mcce.network;

import com.google.common.collect.Lists;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.particle.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.ChaosLib;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public interface NetworkingConstants {
	
	Identifier SILENT_UPDATE_PACKET_ID = MCCE.createIdentifier("silentupdate");
	Identifier MEMORY_GAME_STATUS_PACKET_ID = MCCE.createIdentifier("memorygamestatus");
	Identifier RAINBOW_STATUS_PACKET_ID = MCCE.createIdentifier("rainbow");
	Identifier QUIVER_UPDATE_PACKET_ID = MCCE.createIdentifier("quiverupdate");
	Identifier STUTTER_SOUND_STATUS_PACKET_ID = MCCE.createIdentifier("stuttersoundstatus");
	Identifier LOOK_INVERSION_STATUS_PACKET_ID = MCCE.createIdentifier("lookinversion");
	Identifier INVERSE_STATUS_PACKET_ID = MCCE.createIdentifier("inverse");
	Identifier CLIPPY_STATUS_PACKET_ID = MCCE.createIdentifier("clippy");
	Identifier PAINT_STATUS_PACKET_ID = MCCE.createIdentifier("paint");
	Identifier ROTATE_STATUS_PACKET_ID = MCCE.createIdentifier("rotate");
	Identifier AROUND_THE_WORLD_STATUS_PACKET_ID = MCCE.createIdentifier("aroundtheworld");
	Identifier SPIN_TO_WIN_STATUS_PACKET_ID = MCCE.createIdentifier("spintowin");
	Identifier GRAYSCALE_STATUS_PACKET_ID = MCCE.createIdentifier("grayscale");
	Identifier MOSAIC_STATUS_PACKET_ID = MCCE.createIdentifier("mosaic");
	Identifier COLOUR_INVERSION_STATUS_PACKET_ID = MCCE.createIdentifier("colourinversion");
	Identifier BAKE_STATUS_PACKET_ID = MCCE.createIdentifier("bake");
	Identifier SEPIA_STATUS_PACKET_ID = MCCE.createIdentifier("sepia");
	Identifier CLICKY_PACKET_ID = MCCE.createIdentifier("clicky");
	Identifier HOTBAR_ROULETTE_PACKET_ID = MCCE.createIdentifier("horbarroulette");
	Identifier PUSHY_PACKET_ID = MCCE.createIdentifier("pushy");
	Identifier SCATTER_PACKET_ID = MCCE.createIdentifier("scatter");
	Identifier ICONIC_ID = MCCE.createIdentifier("iconic");
	Identifier POLITE_ID = MCCE.createIdentifier("polite");
	Identifier FLIPPING_OUT_STATUS_PACKET_ID = MCCE.createIdentifier("flippingout");
    Identifier GAMMEGA_STATUS_PACKET_ID = MCCE.createIdentifier("gammega");

	Identifier ENTITY_INITIAL_UPDATE = MCCE.createIdentifier("initialupdate");

	final class ParticlePacketConstants {
		private ParticlePacketConstants() {
			//not to be instantiated
		}

		@FunctionalInterface
		protected interface FloatSupplier {
			float getAsFloat();
		}

		@FunctionalInterface
		protected interface ParticleSupplier {
			ParticleEffect get(BlockRenderView world, double x, double y, double z);
		}

		public interface ParticleGenerator extends ParticleSupplier {
			float[] calculatePositionOffset(float x, float y, float z);
		}

		private record SimpleParticleSupplier(ParticleEffect effect) implements ParticleSupplier {
			@Override
			public ParticleEffect get(BlockRenderView world, double x, double y, double z) {
				return this.effect();
			}
		}

		private record PositionGenerator(float offset, float range, boolean centered) implements FloatSupplier {

			PositionGenerator(float offset, float range) {
				this(offset, range, true);
			}

			PositionGenerator(float range) {
				this(0.0f, range, true);
			}

			@Override
			public float getAsFloat() {
				return ChaosLib.getStaticRandomInstance().nextFloat(this.centered ? -this.range : 0, this.range) + this.offset;
			}
		}

		private static abstract class AbstractParticleGenerator implements ParticleGenerator {

			private final FloatSupplier horizontal, vertical;

			protected AbstractParticleGenerator(FloatSupplier horizontal, FloatSupplier vertical) {
				this.horizontal = horizontal;
				this.vertical = vertical;
			}

			protected AbstractParticleGenerator() {
				this(STANDARD_HORIZONTAL_OFFSET, MID_SECTION_VERTICAL_OFFSET);
			}

			protected AbstractParticleGenerator(FloatSupplier vertical) {
				this(STANDARD_HORIZONTAL_OFFSET, vertical);
			}

			@Override
			public float[] calculatePositionOffset(float x, float y, float z) {
				return new float[] {
						x + this.horizontal.getAsFloat(),
						y + this.vertical.getAsFloat(),
						z + this.horizontal.getAsFloat()
				};
			}
		}

		private static abstract class TypedParticleGenerator<T extends ParticleType<?>> extends AbstractParticleGenerator {
			private final T particle;

			TypedParticleGenerator(T particle, FloatSupplier horizontal, FloatSupplier vertical) {
				super(horizontal, vertical);
				this.particle = particle;
			}

			TypedParticleGenerator(T particle) {
				super();
				this.particle = particle;
			}

			protected T getParticle() {
				return this.particle;
			}
		}

		private static final class BasicParticleGenerator extends TypedParticleGenerator<SimpleParticleType> {

			BasicParticleGenerator(SimpleParticleType particle, FloatSupplier horizontal, FloatSupplier vertical) {
				super(particle, horizontal, vertical);
			}

			BasicParticleGenerator(SimpleParticleType particle, FloatSupplier vertical) {
				this(particle, STANDARD_HORIZONTAL_OFFSET, vertical);
			}

			BasicParticleGenerator(SimpleParticleType particle) {
				super(particle);
			}

			@Override
			public ParticleEffect get(BlockRenderView world, double x, double y, double z) {
				return this.getParticle();
			}
		}

		private static final class ParticleComboTypeGenerator extends AbstractParticleGenerator {
			private final Collection<ParticleSupplier> options;

			@Override
			public ParticleEffect get(BlockRenderView world, double x, double y, double z) {
				return ChaosLib.getRandomElementFrom(this.options, ChaosLib.getStaticRandomInstance()).get(world, x, y, z);
			}

			ParticleComboTypeGenerator(Collection<ParticleSupplier> options) {
				this(options, MID_SECTION_VERTICAL_OFFSET);
			}

			ParticleComboTypeGenerator(Collection<ParticleSupplier> options, FloatSupplier vertical) {
				super(vertical);
				this.options = options;
			}

			ParticleComboTypeGenerator(ParticleSupplier... effects) {
				this(Lists.newArrayList(effects));
			}

			ParticleComboTypeGenerator(SimpleParticleType... effects) {
				this(Arrays.stream(effects).map(SimpleParticleSupplier::new).collect(Collectors.toSet()));
			}
		}
		
		public static final Identifier PARTICLE_PACKET_ID = MCCE.createIdentifier("particle");

		private static final FloatSupplier STANDARD_HORIZONTAL_OFFSET = new PositionGenerator(0.5f);
		private static final FloatSupplier MID_SECTION_VERTICAL_OFFSET = new PositionGenerator(1.0f, 0.8f, false);
		private static final FloatSupplier LOW_SECTION_VERTICAL_OFFSET = new PositionGenerator(0.25f, 0.5f, false);

		private static final List<ParticleGenerator> GENERATORS = Lists.newArrayList();
		public static final ParticleGenerator DAMAGE_INDICATOR = new BasicParticleGenerator(ParticleTypes.DAMAGE_INDICATOR, LOW_SECTION_VERTICAL_OFFSET);
		public static final ParticleGenerator NAUTILUS = new BasicParticleGenerator(ParticleTypes.NAUTILUS);
		public static final ParticleGenerator EXPLOSION = new BasicParticleGenerator(ParticleTypes.EXPLOSION, LOW_SECTION_VERTICAL_OFFSET);
		public static final ParticleGenerator SONIC_BOOM = new BasicParticleGenerator(ParticleTypes.SONIC_BOOM, () -> 0.0f, () -> 0.5f);
		public static final ParticleGenerator SOUL = new BasicParticleGenerator(ParticleTypes.SOUL, new PositionGenerator(0.125f, 0.125f));
		public static final ParticleGenerator HEART = new BasicParticleGenerator(ParticleTypes.HEART);
		public static final ParticleGenerator NOTE = new BasicParticleGenerator(ParticleTypes.NOTE);
		public static final ParticleGenerator TOTEM = new BasicParticleGenerator(ParticleTypes.TOTEM_OF_UNDYING, LOW_SECTION_VERTICAL_OFFSET);
		public static final ParticleGenerator WITCH = new BasicParticleGenerator(ParticleTypes.WITCH);
		public static final ParticleGenerator SPORE = new BasicParticleGenerator(ParticleTypes.SPORE_BLOSSOM_AIR);
		public static final ParticleGenerator CAMPFIRE = new BasicParticleGenerator(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
		public static final ParticleGenerator ASH = new BasicParticleGenerator(ParticleTypes.ASH);
		public static final ParticleGenerator SPARK = new BasicParticleGenerator(ParticleTypes.ELECTRIC_SPARK);
		public static final ParticleGenerator BUBBLE = new BasicParticleGenerator(ParticleTypes.BUBBLE_COLUMN_UP);
		public static final ParticleGenerator OMEN = new ParticleComboTypeGenerator(Lists.newArrayList(ParticleTypes.TRIAL_OMEN, ParticleTypes.RAID_OMEN).stream().map(SimpleParticleSupplier::new).collect(Collectors.toSet()), LOW_SECTION_VERTICAL_OFFSET);
		public static final ParticleGenerator TRIAL = new ParticleComboTypeGenerator(ParticleTypes.TRIAL_SPAWNER_DETECTION, ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS);
		public static final ParticleGenerator TORCH = new ParticleComboTypeGenerator(ParticleTypes.FLAME, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.COPPER_FIRE_FLAME);
		public static final ParticleGenerator LEAF = new ParticleComboTypeGenerator(new SimpleParticleSupplier(ParticleTypes.CHERRY_LEAVES), new SimpleParticleSupplier(ParticleTypes.PALE_OAK_LEAVES), (world, x, y, z) -> TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, BiomeColors.getFoliageColor(world, new BlockPos((int) x, (int) y, (int) z))));
		public static final ParticleGenerator DUST = new AbstractParticleGenerator() {
			@Override
			public ParticleEffect get(BlockRenderView world, double x, double y, double z) {
				return new DustParticleEffect(ChaosLib.getRandomColour(), 1.0f);
			}
		};
		public static final ParticleGenerator EFFECT = new AbstractParticleGenerator() {
			@Override
			public ParticleEffect get(BlockRenderView world, double x, double y, double z) {
				Random rand = ChaosLib.getStaticRandomInstance();
				return EffectParticleEffect.of(ParticleTypes.EFFECT, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			}
		};

		static {
			GENERATORS.add(DAMAGE_INDICATOR);
			GENERATORS.add(NAUTILUS);
			GENERATORS.add(EXPLOSION);
			GENERATORS.add(SONIC_BOOM);
			GENERATORS.add(LEAF);
			GENERATORS.add(SOUL);
			GENERATORS.add(HEART);
			GENERATORS.add(NOTE);
			GENERATORS.add(TOTEM);
			GENERATORS.add(WITCH);
			GENERATORS.add(SPORE);
			GENERATORS.add(CAMPFIRE);
			GENERATORS.add(ASH);
			GENERATORS.add(SPARK);
			GENERATORS.add(BUBBLE);
			GENERATORS.add(OMEN);
			GENERATORS.add(TRIAL);
			GENERATORS.add(TORCH);
			GENERATORS.add(DUST);
			GENERATORS.add(EFFECT);
		}

		public static ParticleGenerator getGeneratorById(byte b) {
			return GENERATORS.get(b);
		}

		public static byte getId(ParticleGenerator generator) {
			return (byte) GENERATORS.indexOf(generator);
		}

		public static int getTotalNumberOfGenerators() {
			return GENERATORS.size();
		}
	}
	
	final class SoundPacketConstants {
		private SoundPacketConstants() {
			//not to be initialized
		}
		
		public static final Identifier SOUND_PACKET_ID = MCCE.createIdentifier("soundevent");
		
		public static final byte UI_BUTTON_CLICK_ID = 0;
		public static final byte FIREWORK_LAUNCHES_ID = 1;
		public static final byte KNOCKBACK_ID = 2;
		public static final byte POWER_UP_ID = 3;
		public static final byte POWER_DOWN_ID = 4;
		public static final byte IRON_HIT_ID = 5;
		public static final byte IRON_EQUIP_ID = 6;
		public static final byte IRON_BREAK_ID = 7;
		public static final byte FIREBALL_ID = 8;
		public static final byte EXTINGUISH_ID = 9;
		public static final byte ENCHANT_ID = 10;
		public static final byte INVERSE_START = 11;
		public static final byte INVERSE_END = 12;
		public static final byte BASALT_DELTAS_ADDITIONS = 13;
		public static final byte ARROW_HIT_PLAYER = 14;
		public static final byte BELL_RESONATE = 15;
		public static final byte BLAZE_AMBIENT = 16;
		public static final byte CHEST_LOCKED = 17;
		public static final byte CREEPER_PRIMED = 18;
		public static final byte EVOKER_WOLOLO = 19;
		public static final byte FIREWORK_TWINKLE = 20;
		public static final byte FOX_AMBIENT = 21;
		public static final byte GHAST_AMBIENT = 22;
		public static final byte DRINK_HONEY = 23;
		public static final byte ITEM_BREAK = 24;
		public static final byte STRAD = 25;
		public static final byte STAL = 26;
		public static final byte WARD = 27;
		public static final byte PHANTOM_AMBIENT = 28;
		public static final byte PHANTOM_SWOOP = 29;
		public static final byte SCULK_SENSOR = 30;
		public static final byte SILVERFISH_AMBIENT = 31;
		public static final byte GOAT_HORN = 32;
		public static final byte LOVABLE_PHANTOM_SPAWN_ID = 33;
		public static final byte ENDER_DRIVE_THRU_ACTIVATE = 34;
		public static final byte SHAKE = 35;
		public static final byte VAULT_ACTIVATE = 36;
		public static final byte OMINOUS_SPAWNER = 37;
		public static final byte OMINOUS_PREPARE = 38;
		public static final byte AMBIENT_CAVE = 39;
		public static final byte PLING = 40;
		public static final byte BOOP = 41;
		public static final byte POTION_BREW = 42;
		public static final byte WIND_BLAST = 43;
		public static final byte WITHER_SPAWN = 44;
		public static final byte WITHER_DEATH = 45;
		public static final byte COPPER_SPIN = 46;
		public static final byte CREAKING_ACTIVATE = 47;
		public static final byte ANGRY_PIGLIN = 48;
		public static final byte WAX_ON = 49;
		
		@SuppressWarnings("MagicNumber")
        public static SoundEvent getSound(byte b) {
            return switch (b) {
                case UI_BUTTON_CLICK_ID -> SoundEvents.UI_BUTTON_CLICK.value();
                case FIREWORK_LAUNCHES_ID -> SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH;
                case KNOCKBACK_ID -> SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK;
                case POWER_UP_ID ->
                        Math.random() < 0.5 ? SoundEvents.BLOCK_CONDUIT_ACTIVATE : SoundEvents.BLOCK_BEACON_ACTIVATE;
                case POWER_DOWN_ID ->
                        Math.random() < 0.5 ? SoundEvents.BLOCK_CONDUIT_DEACTIVATE : SoundEvents.BLOCK_BEACON_DEACTIVATE;
                case IRON_HIT_ID -> SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
                case IRON_EQUIP_ID -> SoundEvents.ENTITY_IRON_GOLEM_REPAIR;
                case IRON_BREAK_ID -> SoundEvents.ENTITY_IRON_GOLEM_DAMAGE;
                case FIREBALL_ID -> SoundEvents.ENTITY_BLAZE_SHOOT;
                case EXTINGUISH_ID -> SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE;
                case ENCHANT_ID -> SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
                case INVERSE_START -> SoundEvents.ENTITY_ENDERMAN_TELEPORT;
                case INVERSE_END -> SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE;
                case BASALT_DELTAS_ADDITIONS -> SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS.value();
                case ARROW_HIT_PLAYER -> SoundEvents.ENTITY_ARROW_HIT_PLAYER;
                case BELL_RESONATE -> SoundEvents.BLOCK_BELL_RESONATE;
                case BLAZE_AMBIENT -> SoundEvents.ENTITY_BLAZE_AMBIENT;
                case CHEST_LOCKED -> SoundEvents.BLOCK_CHEST_LOCKED;
                case CREEPER_PRIMED -> SoundEvents.ENTITY_CREEPER_PRIMED;
                case EVOKER_WOLOLO -> SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO;
                case FIREWORK_TWINKLE -> SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE;
                case FOX_AMBIENT -> SoundEvents.ENTITY_FOX_AMBIENT;
                case GHAST_AMBIENT -> SoundEvents.ENTITY_GHAST_AMBIENT;
                case DRINK_HONEY -> SoundEvents.ITEM_HONEY_BOTTLE_DRINK.value();
                case ITEM_BREAK -> SoundEvents.ENTITY_ITEM_BREAK.value();
                case STRAD -> SoundEvents.MUSIC_DISC_STRAD.value();
                case STAL -> SoundEvents.MUSIC_DISC_STAL.value();
                case WARD -> SoundEvents.MUSIC_DISC_WARD.value();
                case PHANTOM_AMBIENT -> SoundEvents.ENTITY_PHANTOM_AMBIENT;
                case PHANTOM_SWOOP -> SoundEvents.ENTITY_PHANTOM_SWOOP;
                case SCULK_SENSOR -> SoundEvents.BLOCK_SCULK_SENSOR_CLICKING;
                case SILVERFISH_AMBIENT -> SoundEvents.ENTITY_SILVERFISH_AMBIENT;
                case GOAT_HORN ->
                        SoundEvents.GOAT_HORN_SOUNDS.get((int) (Math.random() * SoundEvents.GOAT_HORN_SOUND_COUNT)).value();
                case LOVABLE_PHANTOM_SPAWN_ID -> SoundEvents.ENTITY_PLAYER_LEVELUP;
				case ENDER_DRIVE_THRU_ACTIVATE -> SoundEvents.BLOCK_ENDER_CHEST_OPEN;
				case SHAKE -> SoundEvents.ENTITY_PLAYER_ATTACK_WEAK;
				case VAULT_ACTIVATE -> SoundEvents.BLOCK_VAULT_ACTIVATE;
				case OMINOUS_SPAWNER -> SoundEvents.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE;
				case OMINOUS_PREPARE -> SoundEvents.BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM;
				case AMBIENT_CAVE -> SoundEvents.AMBIENT_CAVE.value();
				case PLING -> SoundEvents.BLOCK_NOTE_BLOCK_PLING.value();
				case BOOP -> SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
				case POTION_BREW -> SoundEvents.BLOCK_BREWING_STAND_BREW;
				case WIND_BLAST -> SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST.value();
				case WITHER_SPAWN -> SoundEvents.ENTITY_WITHER_SPAWN;
				case WITHER_DEATH -> SoundEvents.ENTITY_WITHER_DEATH;
				case COPPER_SPIN -> SoundEvents.ENTITY_COPPER_GOLEM_SPIN;
				case CREAKING_ACTIVATE -> SoundEvents.ENTITY_CREAKING_ACTIVATE;
				case ANGRY_PIGLIN -> SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY;
				case WAX_ON -> SoundEvents.ITEM_HONEYCOMB_WAX_ON;
                default -> null;
            };
		}
		
	}
}
