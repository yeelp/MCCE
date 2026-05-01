package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;

import java.util.List;

public final class GoingBigEffect extends AbstractAttributeChaosEffect {

    private static final int DURATION_MIN = 800, DURATION_MAX = 1350;
    private static final double SCALE_RATE = 0.02;
    private static final double SCALE_MAX = 8, SCALE_MIN = 2;
    private static final Identifier SCALE_ID = MCCE.createIdentifier("goingbigscale");
    private static final String TARGET_KEY = "scaleTarget";
    private double scaleTo;

    public GoingBigEffect() {
        super(DURATION_MIN, DURATION_MAX);
        this.scaleTo = this.getRNG().nextDouble(SCALE_MIN, SCALE_MAX);
    }

    @Override
    protected List<AttributeModifierFactory> getAttributeModifierFactories() {
        return List.of(new AttributeModifierFactory(EntityAttributes.SCALE, new EntityAttributeModifier(SCALE_ID, 0.0, Operation.ADD_VALUE)) {
            @Override
            protected boolean requiresUpdate() {
                return true;
            }

            @Override
            protected EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute) {
                double amount = 0;
                double target = GoingBigEffect.this.scaleTo;
                if(target / SCALE_RATE >= GoingBigEffect.this.durationRemaining()) {
                    amount = -SCALE_RATE;
                }
                else if(attribute.value() < target) {
                    amount = SCALE_RATE;
                }
                return new EntityAttributeModifier(SCALE_ID, Math.max(0, attribute.value() + amount), Operation.ADD_VALUE);
            }
        });
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        World world = player.getEntityWorld();
        BlockPos pos = player.getBlockPos().up();
        return world.isAir(pos = pos.up()) && world.isAir(pos.up());
    }

    @Override
    public String getName() {
        return "goingbig";
    }

    @Override
    public String getDisplayName() {
        return "Going Big";
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.putDouble(TARGET_KEY, this.scaleTo);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.scaleTo = nbt.getDouble(TARGET_KEY, 1);
    }
}
