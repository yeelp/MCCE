package yeelp.mcce.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public abstract class PlayerUtils {

	private PlayerUtils() {
		throw new RuntimeException("Not to be instantiated");
	}
	
	public static boolean isPlayerWorldClient(PlayerEntity player) {
		return player.getEntityWorld().isClient();
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
		return player.getEntityWorld().getRegistryKey().equals(world);
	}

	public static boolean doesPlayerHaveValidPosition(PlayerEntity player) {
		return DoubleStream.of(player.getX(), player.getY(), player.getZ()).noneMatch(Double::isNaN);
	}

	public static void updatePlayerVelocity(PlayerEntity player, Vec3d newV) {
		player.setVelocity(newV);
		getServerPlayer(player).ifPresent((sPlayer) -> {
			sPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(sPlayer));
			sPlayer.networkHandler.resetFloatingTicks();
		});
	}

	public static void addPlayerVelocity(PlayerEntity player, Vec3d addedV) {
		updatePlayerVelocity(player, player.getVelocity().add(addedV));
	}

	public static Iterator<ItemStack> getInventoryIterator(PlayerEntity player) {
		return new InventoryIterator(player);
	}

	public static Iterable<ItemStack> getHandItems(PlayerEntity player) {
		return Lists.newArrayList(player.getEquippedStack(EquipmentSlot.MAINHAND), player.getEquippedStack(EquipmentSlot.OFFHAND));
	}

	private static final class InventoryIterator implements Iterator<ItemStack> {

		private final Iterator<Iterator<ItemStack>> its;
		private Iterator<ItemStack> curr;
		private static final Iterable<EquipmentSlot> OTHERS = Lists.newArrayList(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, EquipmentSlot.OFFHAND);

		InventoryIterator(PlayerEntity player) {
			PlayerInventory inv = player.getInventory();
			Builder<EquipmentSlot> builder = Stream.builder();
			OTHERS.forEach(builder::add);
			this.its = Lists.newArrayList(inv.getMainStacks().iterator(), builder.build().map(player::getEquippedStack).iterator()).iterator();
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
