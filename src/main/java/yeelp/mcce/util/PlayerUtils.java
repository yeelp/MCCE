package yeelp.mcce.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class PlayerUtils {

	private PlayerUtils() {
		throw new RuntimeException("Not to be instantiated");
	}
	
	public static boolean isPlayerWorldClient(PlayerEntity player) {
		return player.getWorld().isClient;
	}
	
	public static boolean isPlayerWorldServer(PlayerEntity player) {
		return !isPlayerWorldClient(player);
	}
	
	public static Optional<ServerPlayerEntity> getServerPlayer(PlayerEntity player) {
		return Optional.ofNullable(player).filter(ServerPlayerEntity.class::isInstance).map(ServerPlayerEntity.class::cast);
	}
	
	public static Optional<ServerPlayerEntity> getServerPlayerIfServerWorld(PlayerEntity player) {
		if(isPlayerWorldClient(player)) {
			return Optional.empty();
		}
		return getServerPlayer(player);
	}

	public static boolean isPlayerInDimension(PlayerEntity player, RegistryKey<World> world) {
		return player.getWorld().getRegistryKey().equals(world);
	}
}
