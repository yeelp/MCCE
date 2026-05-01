package yeelp.mcce.model.chaoseffects;

public final class SocialDistancingEffect extends AbstractPushPullEffect {

    @Override
    public String getName() {
        return "socialdistancing";
    }

    @Override
    public String getDisplayName() {
        return "Social Distancing";
    }

    @Override
    protected float positionMultiplier() {
        return -1;
    }
}
