package yeelp.mcce.network;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;

public interface NetworkingConstants {
	
	Identifier SILENT_UPDATE_PACKET_ID = MCCE.createIdentifier("silentupdate");
	Identifier MEMORY_GAME_STATUS_PACKET_ID = MCCE.createIdentifier("memorygamestatus");
	Identifier RAINBOW_STATUS_PACKET_ID = MCCE.createIdentifier("rainbow");
	Identifier QUIVER_UPDATE_PACKET_ID = MCCE.createIdentifier("quiverupdate");
	Identifier STUTTER_SOUND_STATUS_PACKET_ID = MCCE.createIdentifier("stuttersoundstatus");
	Identifier LOOK_INVERSION_STATUS_PACKET_ID = MCCE.createIdentifier("lookinversion");

	final class ParticlePacketConstants {
		private ParticlePacketConstants() {
			//not to be instantiated
		}
		
		public static final Identifier PARTICLE_PACKET_ID = MCCE.createIdentifier("particle");
		
		public static final byte DAMAGE_INDICATOR = 0;
		public static final byte NAUTILUS = 1;
		public static final byte EXPLOSION = 2;
		public static final byte SONIC_BOOM = 3;
		public static final byte CHERRY = 4;
		public static final byte SOUL = 5;
		public static final byte HEART = 6;
		public static final byte NOTE = 7;
		public static final byte TOTEM = 8;
		public static final byte WITCH = 9;
		public static final byte SPORE = 10;
		public static final byte CAMPFIRE = 11;
		public static final byte ASH = 12;
		public static final byte SPARK = 13;
		public static final byte BUBBLE = 14;
		
		public static ParticleEffect getParticle(byte b) {
            return switch (b) {
                case DAMAGE_INDICATOR -> ParticleTypes.DAMAGE_INDICATOR;
                case NAUTILUS -> ParticleTypes.NAUTILUS;
                case EXPLOSION -> ParticleTypes.EXPLOSION;
                case SONIC_BOOM -> ParticleTypes.SONIC_BOOM;
                case CHERRY -> ParticleTypes.CHERRY_LEAVES;
                case SOUL -> ParticleTypes.SOUL;
                case HEART -> ParticleTypes.HEART;
                case NOTE -> ParticleTypes.NOTE;
                case TOTEM -> ParticleTypes.TOTEM_OF_UNDYING;
                case WITCH -> ParticleTypes.WITCH;
                case SPORE -> ParticleTypes.SPORE_BLOSSOM_AIR;
                case CAMPFIRE -> ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
                case ASH -> ParticleTypes.ASH;
                case SPARK -> ParticleTypes.ELECTRIC_SPARK;
                case BUBBLE -> ParticleTypes.BUBBLE_COLUMN_UP;
                default -> null;
            };
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
                case ITEM_BREAK -> SoundEvents.ENTITY_ITEM_BREAK;
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
                default -> null;
            };
		}
		
	}
}
