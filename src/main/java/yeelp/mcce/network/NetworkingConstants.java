package yeelp.mcce.network;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import yeelp.mcce.MCCE;

public interface NetworkingConstants {
	
	static final Identifier SILENT_UPDATE_PACKET_ID = new Identifier(MCCE.MODID, "silentupdate");
	static final Identifier MEMORY_GAME_STATUS_PACKET_ID = new Identifier(MCCE.MODID, "memorygamestatus");
	static final Identifier RAINBOW_STATUS_PACKET_ID = new Identifier(MCCE.MODID, "rainbow");
	static final Identifier QUIVER_UPDATE_PACKET_ID = new Identifier(MCCE.MODID, "quiverupdate");
	static final Identifier STUTTER_SOUND_STATUS_PACKET_ID = new Identifier(MCCE.MODID, "stuttersoundstatus");
	
	public static final class ParticlePacketConstants {
		private ParticlePacketConstants() {
			//not to be instantiated
		}
		
		public static final Identifier PARTICLE_PACKET_ID = new Identifier(MCCE.MODID, "particle");
		
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
			switch(b) {
				case DAMAGE_INDICATOR:
					return ParticleTypes.DAMAGE_INDICATOR;
				case NAUTILUS:
					return ParticleTypes.NAUTILUS;
				case EXPLOSION:
					return ParticleTypes.EXPLOSION;
				case SONIC_BOOM:
					return ParticleTypes.SONIC_BOOM;
				case CHERRY:
					return ParticleTypes.CHERRY_LEAVES;
				case SOUL:
					return ParticleTypes.SOUL;
				case HEART:
					return ParticleTypes.HEART;
				case NOTE:
					return ParticleTypes.NOTE;
				case TOTEM:
					return ParticleTypes.TOTEM_OF_UNDYING;
				case WITCH:
					return ParticleTypes.WITCH;
				case SPORE:
					return ParticleTypes.SPORE_BLOSSOM_AIR;
				case CAMPFIRE:
					return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
				case ASH:
					return ParticleTypes.ASH;
				case SPARK:
					return ParticleTypes.ELECTRIC_SPARK;
				case BUBBLE:
					return ParticleTypes.BUBBLE_COLUMN_UP;
				default:
					return null;
			}
		}
	}
	
	public static final class SoundPacketConstants {
		private SoundPacketConstants() {
			//not to be initialized
		}
		
		public static final Identifier SOUND_PACKET_ID = new Identifier(MCCE.MODID, "soundevent");
		
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
		
		public static SoundEvent getSound(byte b) {
			switch(b) {
				case UI_BUTTON_CLICK_ID:
					return SoundEvents.UI_BUTTON_CLICK.value();
				case FIREWORK_LAUNCHES_ID:
					return SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH;
				case KNOCKBACK_ID:
					return SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK;
				case POWER_UP_ID:
					return Math.random() < 0.5 ? SoundEvents.BLOCK_CONDUIT_ACTIVATE : SoundEvents.BLOCK_BEACON_ACTIVATE;
				case POWER_DOWN_ID:
					return Math.random() < 0.5 ? SoundEvents.BLOCK_CONDUIT_DEACTIVATE : SoundEvents.BLOCK_BEACON_DEACTIVATE;
				case IRON_HIT_ID:
					return SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
				case IRON_EQUIP_ID:
					return SoundEvents.ENTITY_IRON_GOLEM_REPAIR;
				case IRON_BREAK_ID:
					return SoundEvents.ENTITY_IRON_GOLEM_DAMAGE;
				case FIREBALL_ID:
					return SoundEvents.ENTITY_BLAZE_SHOOT;
				case EXTINGUISH_ID:
					return SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE;
				case ENCHANT_ID:
					return SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
				case INVERSE_START:
					return SoundEvents.ENTITY_ENDERMAN_TELEPORT;
				case INVERSE_END:
					return SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE;
				case BASALT_DELTAS_ADDITIONS:
					return SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS.value();
				case ARROW_HIT_PLAYER:
					return SoundEvents.ENTITY_ARROW_HIT_PLAYER;
				case BELL_RESONATE:
					return SoundEvents.BLOCK_BELL_RESONATE;
				case BLAZE_AMBIENT:
					return SoundEvents.ENTITY_BLAZE_AMBIENT;
				case CHEST_LOCKED:
					return SoundEvents.BLOCK_CHEST_LOCKED;
				case CREEPER_PRIMED:
					return SoundEvents.ENTITY_CREEPER_PRIMED;
				case EVOKER_WOLOLO:
					return SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO;
				case FIREWORK_TWINKLE:
					return SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE;
				case FOX_AMBIENT:
					return SoundEvents.ENTITY_FOX_AMBIENT;
				case GHAST_AMBIENT:
					return SoundEvents.ENTITY_GHAST_AMBIENT;
				case DRINK_HONEY:
					return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
				case ITEM_BREAK:
					return SoundEvents.ENTITY_ITEM_BREAK;
				case STRAD:
					return SoundEvents.MUSIC_DISC_STRAD;
				case STAL:
					return SoundEvents.MUSIC_DISC_STAL;
				case WARD:
					return SoundEvents.MUSIC_DISC_WARD;
				case PHANTOM_AMBIENT:
					return SoundEvents.ENTITY_PHANTOM_AMBIENT;
				case PHANTOM_SWOOP:
					return SoundEvents.ENTITY_PHANTOM_SWOOP;
				case SCULK_SENSOR:
					return SoundEvents.BLOCK_SCULK_SENSOR_CLICKING;
				case SILVERFISH_AMBIENT:
					return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
				case GOAT_HORN:
					return SoundEvents.GOAT_HORN_SOUNDS.get((int)(Math.random() * SoundEvents.GOAT_HORN_SOUND_COUNT)).value();
				case LOVABLE_PHANTOM_SPAWN_ID:
					return SoundEvents.ENTITY_PLAYER_LEVELUP;
				default:
					return null;
			}
		}
		
	}
}
