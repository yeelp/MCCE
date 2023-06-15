package yeelp.mcce.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public interface ClientRenderCallbacks {
	
	@FunctionalInterface
	public interface RenderHealthCallback {
		enum Phase {
			BEFORE,
			AFTER;
			
			private final Event<RenderHealthCallback> event = EventFactory.createArrayBacked(RenderHealthCallback.class, (listeners) -> (stack, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking) -> {
				for(RenderHealthCallback rhc : listeners) {
					rhc.onRender(stack, player, x, y, lines, regenHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
				}
			});
			
			public void register(RenderHealthCallback callback) {
				this.event.register(callback);
			}
			
			public RenderHealthCallback invoker() {
				return this.event.invoker();
			}
		}
		
		void onRender(DrawContext context, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking);
	}
	
	@FunctionalInterface
	public interface AfterShaderSetCallback {
		
		Event<AfterShaderSetCallback> EVENT = EventFactory.createArrayBacked(AfterShaderSetCallback.class, (listeners) -> (matrix, x0, x1, y0, y1, z, u0, u1, v0, v1) -> {
			for(AfterShaderSetCallback assc : listeners) {
				assc.afterShaderSet(matrix, x0, x1, y0, y1, z, u0, u1, v0, v1);
			}
		});
		
		void afterShaderSet(Identifier texture, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1);
	}
}
