package yeelp.mcce.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

/**
 * Library class for a couple reused methods.
 * 
 * @author Yeelp
 */
public final class ChaosLib {

	private ChaosLib() {
		throw new UnsupportedOperationException("Class not to be instantiated!");
	}

	/**
	 * Get a random element from the array.
	 * 
	 * @param <T>  The type of elements in the array.
	 * @param arr  The array.
	 * @param rand The random generator to use.
	 * @return A random element from the array. This is not a copy of the element,
	 *         so changes to the returned element will change the element in the
	 *         array.
	 * @throws IllegalArgumentException If the array is empty.
	 */
	public static <T> T getRandomElementFrom(T[] arr, Random rand) {
		if(arr.length == 0) {
			throw new IllegalArgumentException("Array is empty!");
		}
		return arr[rand.nextInt(arr.length)];
	}

	/**
	 * Gets a random element from a List.
	 * 
	 * @param <T>  The type of elements in the list.
	 * @param list The list to get a random element from.
	 * @param rand The random generator to use.
	 * @return A random element from the list. This is not a copy of the element, so
	 *         changes to the returned element will change the element in the list.
	 * @throws IllegalArgumentException If {@code list} is empty.
	 */
	public static <T> T getRandomElementFrom(List<T> list, Random rand) {
		return ChaosLib.checkConditionsThenOperateOn(list, "List is empty!", List::isEmpty, (l) -> l.get(rand.nextInt(list.size())));
	}

	/**
	 * Gets a random element from a Collection.
	 * 
	 * @param <T>        The type of elements in the collection.
	 * @param collection The collection to get a random element from.
	 * @param rand       The random generator to use.
	 * @return A random element from the collection. This is not a copy of the
	 *         element, so changes to the returned element will change the element
	 *         in the collection.
	 * @throws IllegalArgumentException If {@code collection} is empty.
	 */
	public static <T> T getRandomElementFrom(Collection<T> collection, Random rand) {
		return ChaosLib.checkConditionsThenOperateOn(collection, "Collection is empty!", Collection::isEmpty, (c) -> {
			Iterator<T> it = c.iterator();
			for(int i = rand.nextInt(c.size()); i-- > 0; it.next());
			return it.next();
		});
	}

	private static <T, U> U checkConditionsThenOperateOn(T t, String exceptionMsg, Predicate<T> p, Function<T, U> f) {
		if(p.test(t)) {
			throw new IllegalArgumentException(exceptionMsg);
		}
		return f.apply(t);
	}

	/**
	 * Get a {@link Box} centered on a {@code player} and with the specified
	 * {@code radius}. The sides of the box will be {@code radius} blocks away in
	 * every cardinal direction (including up and down).
	 * 
	 * @param player The player the center the box on.
	 * @param radius The radius to make the box.
	 * @return A Box object with radius {@code radius} that is centered on
	 *         {@code player} using {@link PlayerEntity#getBlockPos()}.
	 * 
	 * @see ChaosLib#getBoxCenteredOnPosWithRadius(BlockPos, int)
	 */
	public static Box getBoxCenteredOnPlayerWithRadius(PlayerEntity player, int radius) {
		return ChaosLib.getBoxCenteredOnPosWithRadius(player.getBlockPos(), radius);
	}

	/**
	 * Get a {@link Box} centered on a {@link BlockPos} and with the specified
	 * {@code radius}. The sides of the box will be {@code radius} blocks away in
	 * every cardinal direction (including up and down).
	 * 
	 * @param pos    The BlockPos object to center the box on.
	 * @param radius The radius to make the box.
	 * @return A Box object with radius {@code radius} that is centered on
	 *         {@code pos}.
	 */
	public static Box getBoxCenteredOnPosWithRadius(BlockPos pos, int radius) {
		return new Box(pos.east(radius).north(radius).up(radius), pos.west(radius).south(radius).down(radius));
	}
}
