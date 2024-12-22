package yeelp.mcce.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.DoubleStream;

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

	public static boolean doesPlayerHaveValidPosition(PlayerEntity player) {
		return DoubleStream.of(player.getX(), player.getY(), player.getZ()).anyMatch(Double::isNaN);
	}

	public static Iterator<ItemStack> getInventoryIterator(PlayerEntity player) {
		return new InventoryIterator(player);
	}

	private static final class InventoryIterator implements Iterator<ItemStack> {

		private final Iterator<Iterator<ItemStack>> its;
		private Iterator<ItemStack> curr;

		InventoryIterator(PlayerEntity player) {
			PlayerInventory inv = player.getInventory();
			this.its = Lists.newArrayList(inv.main.iterator(), inv.armor.iterator(), inv.offHand.iterator()).iterator();
			this.curr = this.its.next();
		}

		@Override
		public boolean hasNext() {
			return this.curr.hasNext() || this.its.hasNext();
		}

		@Override
		public ItemStack next() {
			if(this.curr.hasNext()) {
				return this.curr.next();
			}
			this.curr = this.its.next();
			return this.next();
		}
	}
}
