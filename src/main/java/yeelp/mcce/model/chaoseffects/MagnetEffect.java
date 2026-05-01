package yeelp.mcce.model.chaoseffects;

public final class MagnetEffect extends AbstractPushPullEffect {
	@Override
	public String getName() {
		return "magnet";
	}

	@Override
	protected float positionMultiplier() {
		return 1;
	}
}
