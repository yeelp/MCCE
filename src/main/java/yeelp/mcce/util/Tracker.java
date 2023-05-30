package yeelp.mcce.util;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;

public final class Tracker {
	private final Set<UUID> tracked = Sets.newHashSet();
	
	public void add(UUID id) {
		this.tracked.add(id);
	}
	
	public void add(PlayerEntity player) {
		this.add(player.getUuid());
	}
	
	public boolean tracked(UUID id) {
		return this.tracked.contains(id);
	}
	
	public boolean tracked(PlayerEntity player) {
		return this.tracked(player.getUuid());
	}
	
	public void remove(UUID id) {
		this.tracked.remove(id);
	}
	
	public void remove(PlayerEntity player) {
		this.remove(player.getUuid());
	}
}
