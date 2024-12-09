package yeelp.mcce.api;

import yeelp.mcce.api.impl.MCCEAPIImpl;

/**
 * MCCEAPI. Collection of methods.
 * @author Yeelp
 *
 */
public abstract class MCCEAPI {
	public static MCCEAPIAccessor accessor;
	public static MCCEAPIMutator mutator;
	
	/**
	 * Initialize the API.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
		MCCEAPIImpl.values();
	}
}
