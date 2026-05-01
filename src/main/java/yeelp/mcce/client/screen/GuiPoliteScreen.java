package yeelp.mcce.client.screen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;
import yeelp.mcce.network.PolitePayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.ColourUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class GuiPoliteScreen extends AbstractChaosTextScreen {

    private enum ButtonAction {
        YES("Yes") {
            @Override
            void sendPayload(String effect) {
                ClientPlayNetworking.send(new PolitePayload(effect));
            }
        },
        NO("No") {
            @Override
            void sendPayload(String effect) {
                //nothing
            }
        };

        private final Text text;

        ButtonAction(String text) {
            this.text = Text.of(text);
        }

        Text getText() {
            return this.text;
        }

        abstract void sendPayload(String effect);

        void actionPerformed(GuiPoliteScreen screen, String effect) {
            this.sendPayload(effect);
            screen.close();
        }
    }

    private static final class ColourStyle {
        @Nullable
        private final Formatting colour;
        private float h;
        private static final float HUE_SHIFT_RATE = 0.25f;

        ColourStyle(@Nullable Formatting colour) {
            if((this.colour = colour) == null) {
                int c = ChaosLib.getRandomColour();
                this.h = ColourUtil.RGBtoHSL((byte) ColorHelper.getRed(c), (byte) ColorHelper.getGreen(c), (byte) ColorHelper.getBlue(c))[0];
            }
        }

        Style applyColour() {
            if(this.colour != null) {
                return Style.EMPTY.withColor(this.colour);
            }
            //noinspection MagicNumber
            this.h = (this.h + HUE_SHIFT_RATE) % 360;
            short[] rgb = ColourUtil.HSLtoRGB(this.h);
            return Style.EMPTY.withColor(ColorHelper.getArgb(rgb[0], rgb[1], rgb[2]));
        }
    }

    private final String effect;
    private final String display;
    private final ColourStyle style;
    public GuiPoliteScreen(String effect) {
        super(Text.of("Apply Effect?"));
        this.effect = effect;
        this.display = ChaosEffectRegistry.getEntry(this.effect).getDisplayName();
        this.style = new ColourStyle(ChaosLib.getStaticRandomInstance().nextBoolean() ? null : ChaosLib.getRandomElementFrom(Arrays.stream(Formatting.values()).filter(Formatting::isColor).collect(Collectors.toList()), ChaosLib.getStaticRandomInstance()));
    }

    @Override
    protected Stream<ButtonWidget> getButtons() {
        AtomicInteger i = new AtomicInteger(1);
        List<ButtonAction> options = Lists.newArrayList(ButtonAction.values());
        Collections.shuffle(options, ChaosLib.getStaticRandomInstance());
        return options.stream().map((action) -> new Builder(action.getText(), button -> action.actionPerformed(this, this.effect)).width(this.width/4).position(i.get()*this.width/4 + (2*i.getAndIncrement() - 3)*10, 5*this.height/6).build());
    }

    @Override
    protected Stream<List<Text>> getText() {
        MutableText text = MutableText.of(new Literal("Apply Effect: "));
        Text.of(this.display).getWithStyle(this.style.applyColour()).forEach(text::append);
        text.append(Text.of("?"));
        return Stream.of(List.of(text));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
