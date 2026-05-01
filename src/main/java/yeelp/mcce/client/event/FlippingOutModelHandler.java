package yeelp.mcce.client.event;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.block.entity.model.CopperGolemStatueModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import yeelp.mcce.client.event.ClientRenderCallbacks.BeforeModelRenderCallback;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.model.chaoseffects.FlippingOutEffect;

import java.util.Map;
import java.util.UUID;

public final class FlippingOutModelHandler implements BeforeModelRenderCallback, PlayerTickCallback {

    private static final Map<UUID, Float> ROTATION_AMOUNT = Maps.newHashMap();
    private static final float INC_AMOUNT = 0.05f;

    public static void incrementRotationAmount(UUID uuid) {
        ROTATION_AMOUNT.merge(uuid, INC_AMOUNT, Float::sum);
    }

    @Override
    public void beforeRender(MatrixStack stack, Model<?> model) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null && FlippingOutEffect.isClientTracked(player) && !(model instanceof Model.SinglePartModel) && !(model instanceof CopperGolemStatueModel)) {
            ModelTransform current = model.getRootPart().getTransform();
            model.getRootPart().setTransform(new ModelTransform(current.x(), current.y(), current.z(), current.pitch() + ROTATION_AMOUNT.getOrDefault(player.getUuid(), 0.0f), current.yaw(), current.roll(), current.xScale(), current.yScale(), current.zScale()));
        }
    }

    @Override
    public void tick(PlayerEntity player) {
        UUID uuid = player.getUuid();
        if(FlippingOutEffect.isClientTracked(player)) {
            incrementRotationAmount(uuid);
        }
        else {
            ROTATION_AMOUNT.remove(uuid);
        }
    }
}
