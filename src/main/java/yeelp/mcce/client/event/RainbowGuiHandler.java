package yeelp.mcce.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import yeelp.mcce.client.event.ClientRenderCallbacks.ChangeTextureColour;
import yeelp.mcce.client.event.ClientRenderCallbacks.RenderHealthCallback;
import yeelp.mcce.util.Tracker;

import java.util.function.Function;

public class RainbowGuiHandler implements RenderHealthCallback {

	private final boolean reset;
	private static boolean drawingHearts = false;
	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	

	public RainbowGuiHandler(boolean reset) {
		this.reset = reset;
	}

	@Override
	public void onRender(DrawContext context, PlayerEntity player, int x, int y, int lines, int regenHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if(!AFFECTED_PLAYERS.tracked(player)) {
			return;
		}
		drawingHearts = !this.reset;
	}

	public static void addPlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player);
	}

	public static void removePlayer(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player);
		drawingHearts = false;
	}

	public static final class RainbowShaderHandler implements ChangeTextureColour {

		private float h = 0;

		@SuppressWarnings("MagicNumber")
		@Override
		public int changeColor(Function<Identifier, RenderLayer> renderLayers, Identifier texture, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
			if (RainbowGuiHandler.drawingHearts) {
				this.h += 0.5f;
				RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
				short[] rgb = HSLToRGB((this.h) % 360);
				RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], 0.5f);
				RainbowGuiHandler.drawingHearts = false;
				return ColorHelper.getArgb(rgb[0] << 16,rgb[1]  << 8, rgb[2]);
			}
			return color;
		}

		@SuppressWarnings("MagicNumber")
        private static short[] HSLToRGB(float h) {
			float c = (1 - Math.abs(2 * (float) 0.5 - 1));
			float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
			float m = (float) 0.5 - c / 2;
			float rPrime, gPrime, bPrime;
			switch((int) h / 60) {
				case 0:
					rPrime = c;
					gPrime = x;
					bPrime = 0;
					break;
				case 1:
					rPrime = x;
					gPrime = c;
					bPrime = 0;
					break;
				case 2:
					rPrime = 0;
					gPrime = c;
					bPrime = x;
					break;
				case 3:
					rPrime = 0;
					gPrime = x;
					bPrime = c;
					break;
				case 4:
					rPrime = x;
					gPrime = 0;
					bPrime = c;
					break;
				case 5:
					rPrime = c;
					gPrime = 0;
					bPrime = x;
					break;
				default:
					rPrime = 0;
					gPrime = 0;
					bPrime = 0;
					break;
			}
			return new short[] {
                    (short) ((rPrime + m) * 255),
                    (short) ((gPrime + m) * 255),
                    (short) ((bPrime + m) * 255)};
		}
	}
}
