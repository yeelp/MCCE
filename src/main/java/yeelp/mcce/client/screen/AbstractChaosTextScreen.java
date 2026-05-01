package yeelp.mcce.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractChaosTextScreen extends Screen {
    
    private static final int TEXT_SPACING = 10;
    protected AbstractChaosTextScreen(Text title) {
        super(title);
    }

    @Override
    protected final void init() {
        super.init();
        this.getButtons().forEach(this::addDrawableChild);
    }
    
    protected abstract Stream<ButtonWidget> getButtons();
    
    protected abstract Stream<List<Text>> getText();
    
    @Override
    public final void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        int x = this.width/2;
        int y = this.height/6 + 10;
        OfInt ys = IntStream.iterate(y, (i) -> i + TEXT_SPACING).iterator();
        //noinspection MagicNumber
        this.getText().forEach((l) -> l.forEach((text) -> context.drawCenteredTextWithShadow(this.getTextRenderer(), text, x, ys.nextInt(), 0xffffffff)));
    }
}
