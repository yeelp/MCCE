package yeelp.mcce.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("MagicNumber")
public final class GuiWarningScreen extends AbstractChaosTextScreen {

    private final Screen base;
    private static final Text WARNING = Text.of("WARNING!");
    private static final String[] TEXT = new String[] {
            WARNING.getString(),
            "Chaos Edition may unintentionally cause flashing colours to appear on screen.",
            "This is a result of various effects creating these flashing colours in various ways.",
            "If you are someone who is sensitive to flashing colours or lights, please keep this in mind while playing."
    };

    public GuiWarningScreen(Screen base) {
        super(WARNING);
        this.base = base;
    }

    @Override
    protected Stream<ButtonWidget> getButtons() {
        return Stream.of(new ButtonWidget.Builder(Text.of("Okay"), button -> {
            MinecraftClient client = Objects.requireNonNull(GuiWarningScreen.this.client);
            client.setScreen(GuiWarningScreen.this.base);
            GuiWarningScreen.this.close();
        }).width(this.width/2).position(this.width/4, this.height/2 + 50).build());
    }

    @Override
    protected Stream<List<Text>> getText() {
        return Arrays.stream(TEXT).map((s) -> Text.of(s).getWithStyle(Style.EMPTY.withColor(s.equals(WARNING.getString()) ? Formatting.RED : Formatting.WHITE)));
    }
}
