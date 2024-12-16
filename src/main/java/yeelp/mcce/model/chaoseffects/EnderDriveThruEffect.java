package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import yeelp.mcce.network.NetworkingConstants.SoundPacketConstants;
import yeelp.mcce.network.SoundPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EnderDriveThruEffect extends AbstractInstantChaosEffect {

    private static final List<Text> NAMES = Arrays.stream(new String[] {"Ender Drive Thru", "Did You Want Your Ender Chest?", "Ender on Demand", "Efficient Ender Entrypoint", "Return to Ender", "Ender Storage.co"}).map(Text::of).collect(Collectors.toList());
    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return true;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        if(player.getEnderChestInventory() != null && player.getWorld() instanceof ServerWorld) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, inv, plyer) -> GenericContainerScreenHandler.createGeneric9x3(i, inv, plyer.getEnderChestInventory()), ChaosLib.getRandomElementFrom(NAMES, this.getRNG())));
            PlayerUtils.getServerPlayer(player).ifPresent(new SoundPayload(SoundPacketConstants.ENDER_DRIVE_THRU_ACTIVATE, 1.0f, 1.0f)::send);
        }
    }

    @Override
    public String getName() {
        return "enderdrivethru";
    }
}
