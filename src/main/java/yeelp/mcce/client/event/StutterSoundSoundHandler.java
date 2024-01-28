package yeelp.mcce.client.event;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.util.Tracker;

public final class StutterSoundSoundHandler implements PlayerTickCallback {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final Map<UUID, SoundData> CURRENT_SOUND = Maps.newHashMap();
	
	protected record SoundData(SoundEvent sound, float volume, float pitch) {
		public SoundData {
			Objects.requireNonNull(sound);
		}
	}
	
	@Override
	public void tick(PlayerEntity player) {
		if(player instanceof ClientPlayerEntity && AFFECTED_PLAYERS.tracked(player)) {
			SoundData sound = CURRENT_SOUND.get(player.getUuid());
			if(sound != null) {
				player.playSound(sound.sound(), sound.volume(), sound.pitch());
			}
		}
	}
	
	public static void addPlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player);
	}
	
	public static void removePlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player);
		CURRENT_SOUND.remove(player.getUuid());
	}
	
	public static void setSound(PlayerEntity player, SoundEvent sound, float volume, float pitch) {
		if(AFFECTED_PLAYERS.tracked(player)) {
			CURRENT_SOUND.put(player.getUuid(), new SoundData(sound, volume, pitch));
		}
	}

}
