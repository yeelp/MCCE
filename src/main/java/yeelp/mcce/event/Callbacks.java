package yeelp.mcce.event;

public final class Callbacks {

	public static void registerCallbacks() {
		PlayerTickCallback.EVENT.register(new ChaosEffectTracker());
	}
}
