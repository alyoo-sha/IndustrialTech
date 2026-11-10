package org.mod.industrialtech.client.screen;

import org.mod.nova_core.ui.component.Widget;
import org.mod.nova_core.ui.context.UiContext;
import org.mod.nova_core.ui.context.UiPlatform;
import org.mod.nova_core.ui.graphics.UiGraphics;
import org.mod.nova_core.ui.graphics.UiText;
import org.mod.nova_core.ui.overlay.TooltipBubble;
import org.mod.nova_core.ui.overlay.Tooltips;
import org.mod.nova_core.ui.style.SliderStyle;
import org.mod.nova_core.ui.style.Styles;
import org.mod.nova_core.ui.theme.StyleRef;

import java.util.function.LongSupplier;
import java.util.function.Supplier;

/** Красная полоса энергии с промежутками; значения опрашиваются каждый кадр. */
public class EnergyBar extends Widget {
    private static final int SEG_W = 4;
    private static final int SEG_GAP = 1;
    private static final int PAD = 2;
    private static final int FILLED = 0xFFE0453F;
    private static final int EMPTY = 0x40E0453F;

    private final StyleRef<SliderStyle> style = new StyleRef<>(Styles.SLIDER);
    private final LongSupplier stored;
    private final LongSupplier capacity;
    private final Supplier<String> label;

    public EnergyBar(int x, int y, int width, int height, LongSupplier stored, LongSupplier capacity,
                     Supplier<String> label, Supplier<String> tooltip) {
        super(x, y, width, height);
        this.stored = stored;
        this.capacity = capacity;
        this.label = label;
        Tooltips.attach(this, () -> new TooltipBubble(UiText.literal(tooltip.get())));
    }

    /** 2560000000 -> "2 560 000 000". */
    public static String format(long value) {
        String s = Long.toString(value);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (s.length() - i) % 3 == 0) b.append(' ');
            b.append(s.charAt(i));
        }
        return b.toString();
    }

    @Override
    protected void renderSelf(UiGraphics g, int mouseX, int mouseY, float partialTick) {
        SliderStyle s = style.get();
        int ax = absX();
        int ay = absY();
        int w = getWidth();
        int h = getHeight();

        g.fillRounded(ax, ay, ax + w, ay + h, s.radius() + 1, s.track());

        int count = Math.max(1, (w - PAD * 2 + SEG_GAP) / (SEG_W + SEG_GAP));
        long cap = capacity.getAsLong();
        double ratio = cap > 0 ? Math.min(1.0, (double) stored.getAsLong() / cap) : 0.0;
        int filled = (int) Math.ceil(count * ratio);

        int used = count * (SEG_W + SEG_GAP) - SEG_GAP;
        int x0 = ax + (w - used) / 2;
        for (int n = 0; n < count; n++) {
            int sx = x0 + n * (SEG_W + SEG_GAP);
            g.fillRounded(sx, ay + PAD, sx + SEG_W, ay + h - PAD, 1, n < filled ? FILLED : EMPTY);
        }

        UiPlatform p = UiContext.platform();
        UiText text = UiText.literal(label.get());
        g.drawText(text, ax + (w - p.textWidth(text)) / 2, ay + (h - p.lineHeight()) / 2, 0xFFFFFFFF, true);
    }
}