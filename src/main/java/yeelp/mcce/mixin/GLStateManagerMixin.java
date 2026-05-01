package yeelp.mcce.mixin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import yeelp.mcce.model.chaoseffects.PaintEffect;

import java.util.Map;
import java.util.UUID;

@Mixin(GlStateManager.class)
@Environment(EnvType.CLIENT)
public abstract class GLStateManagerMixin implements AutoCloseable {

    @Unique
    private static final int CLEAR_INTERVAL = 800;
    @Unique
    private static final Map<UUID, Integer> TRACKED_PLAYERS = Maps.newHashMap();

    @ModifyVariable(method = "_clear(I)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static int clear(int mask) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) {
            return mask;
        }
        if(!PaintEffect.isClientTracked(player)) {
            TRACKED_PLAYERS.remove(player.getUuid());
            return mask;
        }
        if(TRACKED_PLAYERS.merge(player.getUuid(), 1, Integer::sum) % CLEAR_INTERVAL == 0) {
            return mask;
        }
        return mask & ~(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
