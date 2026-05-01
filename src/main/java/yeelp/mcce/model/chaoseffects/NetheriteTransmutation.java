package yeelp.mcce.model.chaoseffects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;
import yeelp.mcce.event.OnBlockPlaceCallback;

public final class NetheriteTransmutation extends SimpleTimedChaosEffect implements OnBlockPlaceCallback {
    private static final int DURATION_MIN = 3600, DURATION_MAX = 4000;
    private static final BlockState NETHERITE_STATE = Blocks.NETHERITE_BLOCK.getDefaultState();

    public NetheriteTransmutation() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected boolean canStack() {
        return true;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MIDAS_TOUCH);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        //nothing!
    }

    @Override
    public String getName() {
        return "netheritetransmutation";
    }

    @Override
    public String getDisplayName() {
        return "Netherite Transmutation";
    }

    @Override
    public void registerCallbacks() {
        OnBlockPlaceCallback.EVENT.register(this);
    }

    @Override
    public CallbackResult onBlockPlace(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(placer instanceof ServerPlayerEntity player && MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.NETHERITE_TRANSMUTATION)) {
            world.setBlockState(pos, NETHERITE_STATE);
            return new CallbackResult(CancelState.CANCEL);
        }
        return new CallbackResult();
    }
}
