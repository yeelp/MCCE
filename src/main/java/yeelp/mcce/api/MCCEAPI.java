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
	public static final void init() {
		MCCEAPIImpl.values();
	}
}
