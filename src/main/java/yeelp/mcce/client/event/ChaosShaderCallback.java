package yeelp.mcce.client.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import yeelp.mcce.client.event.ClientRenderCallbacks.SetShaderCallback;

import java.util.function.Predicate;

public final class ChaosShaderCallback implements SetShaderCallback {

    private final Predicate<PlayerEntity> check;
    private final Identifier id;

    public ChaosShaderCallback(Identifier id, Predicate<PlayerEntity> check) {
        this.check = check;
        this.id = id;
    }

    @Override
    public Identifier setShaderIdentifier(Identifier curr) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null || !this.check.test(player)) {
            return curr;
        }
        return this.id;
    }
}
