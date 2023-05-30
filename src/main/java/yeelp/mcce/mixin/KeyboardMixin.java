package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import yeelp.mcce.client.event.KeyPressCallback;
import yeelp.mcce.event.CallbackResult;
import yeelp.mcce.event.CallbackResult.CancelState;

@Mixin(Keyboard.class)
public class KeyboardMixin {

	@Inject(at = @At("HEAD"), method = "onKey(JIIII)V", cancellable = true)
	private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
		CallbackResult result = KeyPressCallback.EVENT.invoker().onKeyPressBefore((Keyboard) (Object) this, window, key, scancode, action, modifiers);
		if(result.getCancelState() == CancelState.CANCEL) {
			info.cancel();
		}
	}
}
