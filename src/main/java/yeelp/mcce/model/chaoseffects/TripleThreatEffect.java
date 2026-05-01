package yeelp.mcce.model.chaoseffects;

public final class TripleThreatEffect extends MultiChaosEffect {

	public TripleThreatEffect() {
		super(3);
	}

	@Override
	public String getName() {
		return "triplethreat";
	}

	@Override
	public String getDisplayName() {
		return "Triple Threat";
	}
}
