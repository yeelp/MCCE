package yeelp.mcce.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yeelp.mcce.model.chaoseffects.GammegaEffect;

@Mixin(SimpleOption.class)
@Environment(EnvType.CLIENT)
public class MixinSimpleOption {

    @Final
    @Shadow
    Text text;

    @Unique
    private static final double GAMMEGA_TARGET = 64;
    @Unique
    private static final String GAMMEGA_OPTION_KEY = "options.gamma";

    @Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
    public void changeValue(CallbackInfoReturnable<Object> cir) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null || !GammegaEffect.isClientTracked(MinecraftClient.getInstance().player)) {
            return;
        }
        if(this.text.getContent() instanceof TranslatableTextContent content && content.getKey().equals(GAMMEGA_OPTION_KEY)) {
            cir.setReturnValue(GAMMEGA_TARGET);
        }
    }
}
