package yeelp.mcce.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yeelp.mcce.client.event.KeyPressCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;

@Mixin(Keyboard.class)
public class KeyboardMixin {

	@Inject(at = @At("HEAD"), method = "onKey(JILnet/minecraft/client/input/KeyInput;)V", cancellable = true)
	private void onKey(long window, int keyaction, KeyInput input, CallbackInfo info) {
		CallbackResult result = KeyPressCallback.EVENT.invoker().onKeyPressBefore((Keyboard) (Object) this, window, keyaction, input);
		if(result.getCancelState() == CancelState.CANCEL) {
			info.cancel();
		}
	}
}
