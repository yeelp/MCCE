package yeelp.mcce.model.chaoseffects;

/**
 * An Optional Chaos Effect. Chaos Effects that can be disabled should implement this.
 * 
 * @author Yeelp
 */
public interface OptionalEffect {

	/**
	 * Is this enabled?
	 * @return {@code true} if enabled.
	 */
	boolean enabled();
}
