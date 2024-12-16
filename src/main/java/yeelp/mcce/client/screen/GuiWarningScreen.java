package yeelp.mcce.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Objects;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;

@SuppressWarnings("MagicNumber")
public final class GuiWarningScreen extends Screen {

    private final Screen base;
    private static final Text WARNING = Text.of("WARNING!");
    private static final String[] TEXT = new String[] {
            WARNING.getString(),
            "Chaos Edition may unintentionally cause flashing colours to appear on screen.",
            "This is a result of various effects creating these flashing colours in various ways.",
            "If you are someone who is sensitive to flashing colours, please keep this in mind while playing."
    };
    private static final int TEXT_SPACING = 10;

    public GuiWarningScreen(Screen base) {
        super(WARNING);
        this.base = base;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget.Builder(Text.of("Okay"), button -> {
            MinecraftClient client = Objects.requireNonNull(GuiWarningScreen.this.client);
            client.setScreen(GuiWarningScreen.this.base);
            GuiWarningScreen.this.close();
        }).width(this.width/2).position(this.width/4, this.height/2 + 50).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = this.width/2;
        int y = this.height/6 + 10;
        OfInt ys = IntStream.iterate(y, (i) -> i + TEXT_SPACING).iterator();
        Arrays.stream(TEXT).map((s) -> Text.of(s).getWithStyle(Style.EMPTY.withColor(s.equals(WARNING.getString()) ? Formatting.RED : Formatting.WHITE))).forEach((list) -> list.forEach((text) -> context.drawCenteredTextWithShadow(this.getTextRenderer(), text, x, ys.nextInt(), 0xffffff)));
    }
}
