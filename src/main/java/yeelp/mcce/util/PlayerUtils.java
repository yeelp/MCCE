package yeelp.mcce.util;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerUtils {

	private PlayerUtils() {
		throw new RuntimeException("Not to be instantiated");
	}
	
	@SuppressWarnings("resource")
	public static final boolean isPlayerWorldClient(PlayerEntity player) {
		return player.getWorld().isClient;
	}
	
	public static final boolean isPlayerWorldServer(PlayerEntity player) {
		return !isPlayerWorldClient(player);
	}
	
	public static final Optional<ServerPlayerEntity> getServerPlayer(PlayerEntity player) {
		return Optional.ofNullable(player).filter(ServerPlayerEntity.class::isInstance).map(ServerPlayerEntity.class::cast);
	}
	
	public static final Optional<ServerPlayerEntity> getServerPlayerIfServerWorld(PlayerEntity player) {
		if(isPlayerWorldClient(player)) {
			return Optional.empty();
		}
		return getServerPlayer(player);
	}
}
