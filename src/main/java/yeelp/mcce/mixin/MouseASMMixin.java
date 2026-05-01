package yeelp.mcce.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseASMMixin {

    @Invoker("onMouseButton")
    void mcce$onMouseButton(long window, MouseInput input, int action);
}
