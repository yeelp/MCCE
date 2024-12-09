package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public final class AmbidextrousEffect extends AbstractTimedChaosEffect {

    private static final int DURATION_MIN = 4500, DURATION_MAX = 7000;
    private static final float SWAP_CHANCE = 0.7f;

    public AmbidextrousEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //no additional effect logic
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        player.setMainArm(player.getMainArm() == Arm.RIGHT ? Arm.LEFT : Arm.RIGHT);
        if(this.rand.nextFloat() < SWAP_CHANCE) {
            ItemStack temp = player.getMainHandStack();
            player.setStackInHand(Hand.MAIN_HAND, player.getOffHandStack());
            player.setStackInHand(Hand.OFF_HAND, temp);
        }
    }

    @Override
    public String getName() {
        return "ambidextrous";
    }

    @Override
    public void registerCallbacks() {
        //no callbacks
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        this.applyEffect(player);
    }
}
