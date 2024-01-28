package yeelp.mcce.model;

import net.minecraft.nbt.NbtCompound;

/**
 * A despawn timer to force mob despawns.
 * 
 * @author Yeelp
 */
public final class DespawnTimer {

	private static final String TIMER_KEY = "timer";
	private static final String SET_KEY = "set";

	private int timer;
	private boolean set;

	/**
	 * Construct a new DespawnTimer
	 */
	public DespawnTimer() {
		this.timer = 0;
		this.set = false;
	}

	/**
	 * Construct a new DespawnTimer from NBT. This assumes the NBT was created from
	 * {@link DespawnTimer#writeToNbt()}.
	 * 
	 * @param nbt NBT to load from.
	 */
	public DespawnTimer(NbtCompound nbt) {
		this.timer = nbt.getInt(TIMER_KEY);
		this.set = nbt.getBoolean(SET_KEY);
	}

	/**
	 * Set this timer.
	 * 
	 * @param duration the duration to set.
	 */
	public void setTimer(int duration) {
		this.set = true;
		this.timer = duration;
	}

	/**
	 * Decrement the timer, only if it is set.
	 */
	public void tick() {
		if(this.set) {
			this.timer--;
		}
	}

	/**
	 * Has the timer expired? Only returns {@code true} if the timer has been
	 * previously set
	 * 
	 * @return True if the timer was previously set and the duration has elapsed.
	 */
	public boolean isExpired() {
		return this.set && this.timer == 0;
	}

	/**
	 * Get the time remaining on this timer.
	 * 
	 * @return The timer remaining
	 */
	public int getTimeRemaining() {
		return this.timer;
	}

	/**
	 * Write this timer's contents to NBT
	 * 
	 * @return an NbtCompound with this timer's data.
	 */
	public NbtCompound writeToNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt(TIMER_KEY, this.timer);
		nbt.putBoolean(SET_KEY, this.set);
		return nbt;
	}
}
