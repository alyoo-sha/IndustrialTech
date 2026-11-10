package org.mod.industrialtech.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.mod.industrialtech.client.IClientForgeEvents;
import org.mod.industrialtech.client.render.IBlurConfig;
import org.mod.nova_core.ui.api.ButtonWidget;
import org.mod.nova_core.ui.api.Checkbox;
import org.mod.nova_core.ui.api.HStack;
import org.mod.nova_core.ui.api.Panel;
import org.mod.nova_core.ui.api.TextWidget;
import org.mod.nova_core.ui.api.VStack;
import org.mod.nova_core.ui.component.Widget;
import org.mod.nova_core.ui.context.UiContext;
import org.mod.nova_core.ui.graphics.UiGraphics;
import org.mod.nova_core.ui.graphics.UiText;
import org.mod.nova_core.ui.input.MouseButton;
import org.mod.nova_core.ui.input.PointerEvent;
import org.mod.nova_core.ui.layout.Alignment;
import org.mod.nova_core.ui.layout.Insets;
import org.mod.nova_core.ui.layout.Justification;
import org.mod.nova_core.ui.layout.SizeMode;
import org.mod.nova_core.ui.minecraft.NovaTexts;
import org.mod.nova_core.ui.minecraft.UIScreen;
import org.mod.nova_core.ui.state.Computed;
import org.mod.nova_core.ui.state.Signal;
import org.mod.nova_core.ui.state.Subscription;
import org.mod.nova_core.ui.style.BackdropStyle;
import org.mod.nova_core.ui.style.ButtonStyle;
import org.mod.nova_core.ui.style.Gradient;
import org.mod.nova_core.ui.style.Styles;
import org.mod.nova_core.ui.theme.Theme;
import org.mod.nova_core.ui.theme.ThemeManager;

import java.util.function.IntConsumer;

/** Экран настроек размытия на Nova Core (тема Glass). Конструктор без аргументов прежний. */
public class IBlurSettingsScreen extends UIScreen {

    private static final int PANEL_W = 232;
    private static final int PANEL_H = 160;
    private static final int PAD = 14;

    private static final int MIN_RADIUS = 1;
    /** Больше 30 уже заметно нагружает видеокарту (4 прохода по 2R+1 выборок). */
    private static final int MAX_RADIUS = 30;

    /** Прозрачность оттенка сверху градиента (0x00..0xFF). */
    private static final int TINT_TOP_ALPHA = 0x70;
    private static final String BOTTOM_COLOR = "0000003f";

    /** RGB пресетов: синий, красный, фиолетовый, зелёный, жёлтый, оранжевый, розовый, голубой. */
    private static final int[] PRESETS = {
            0x0190FF, 0xFF0000, 0xAA00FF, 0x00FF00, 0xFFFF00, 0xFFA500, 0xFF69B4, 0x00FFFF
    };

    // ------------------------------------------------------------------ state
    // Состояние живёт в экране: build() вызывается заново при ресайзе, а значения должны сохраняться

    private final Signal<Boolean> blurOn = Signal.of(IBlurConfig.CLIENT.blurEnabled.get());
    private final Signal<Integer> radius = Signal.of(IBlurConfig.CLIENT.radius.get());
    /** RGB выбранного пресета или -1. */
    private final Signal<Integer> selectedRgb = Signal.of(readSelectedRgb());
    private final Computed<UiText> radiusText = new Computed<>(
            () -> NovaTexts.tr("label.industrialtech.radius", radius.get()));

    private final Subscription blurSub;
    private Theme savedTheme = null;

    public IBlurSettingsScreen() {
        super(Component.translatable("label.industrialtech.settings"));
        this.blurSub = blurOn.subscribe(enabled -> defer(() -> onBlurToggled(enabled)));
    }

    // ------------------------------------------------------------------ lifecycle

    @Override
    protected void init() {
        // Фон темы заменяем прозрачным, пока экран открыт: иначе он закрыл бы оттенок из MixinScreen
        if (savedTheme == null) {
            savedTheme = ThemeManager.current();
            ThemeManager.set(savedTheme.with(Styles.BACKDROP, new BackdropStyle(Gradient.solid(0x00000000))));
        }
        super.init();
    }

    /**
     * Ванильный фон Screen.renderBackground(GuiGraphics): именно в нём MixinScreen подменяет цвета
     * градиента на цвета из конфига. Это живой предпросмотр выбранного цвета.
     */
    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public void removed() {
        blurSub.cancel();
        radiusText.cancel();
        if (savedTheme != null) {
            ThemeManager.set(savedTheme);
            savedTheme = null;
        }
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ------------------------------------------------------------------ layout

    @Override
    protected void build(Widget root) {
        Panel panel = new Panel((root.getWidth() - PANEL_W) / 2, (root.getHeight() - PANEL_H) / 2, PANEL_W, PANEL_H);

        VStack stack = new VStack(0, 0, 0, 0);
        stack.setSizeMode(SizeMode.FILL, SizeMode.FILL);
        stack.setPadding(Insets.all(PAD)).setGap(6).setAlignment(Alignment.STRETCH);

        stack.add(new TextWidget(0, 0, 0, 12, NovaTexts.bold(NovaTexts.tr("label.industrialtech.settings")))
                .setAlignment(Alignment.CENTER));
        stack.add(centered(new Checkbox(0, 0, NovaTexts.tr("label.industrialtech.blur_toggle"), blurOn)));
        stack.add(radiusRow());
        stack.add(new TextWidget(0, 0, 0, 10, NovaTexts.tr("label.industrialtech.dark_color")).setColor(0xFFC8D2E6));
        stack.add(centered(swatchRow()));

        Widget spacer = new Widget(0, 0, 0, 0);
        spacer.setWeight(1f);
        stack.add(spacer);

        stack.add(bottomRow());
        panel.add(stack);
        root.add(panel);
    }

    /** Обёртка для центрирования по горизонтали. */
    private static Widget centered(Widget child) {
        HStack row = new HStack(0, 0, 0, 0);
        row.setSizeMode(SizeMode.FILL, SizeMode.WRAP);
        row.setJustification(Justification.CENTER).setAlignment(Alignment.CENTER);
        row.add(child);
        return row;
    }

    /** [ - ]  Радиус: N  [ + ] */
    private Widget radiusRow() {
        HStack row = new HStack(0, 0, 0, 0);
        row.setSizeMode(SizeMode.FILL, SizeMode.WRAP);
        row.setJustification(Justification.SPACE_BETWEEN).setAlignment(Alignment.CENTER);

        TextWidget label = new TextWidget(0, 0, UiText.literal(""));
        label.bindText(radiusText);

        row.add(new ButtonWidget(0, 0, 40, 20, UiText.literal("-"), () -> defer(() -> changeRadius(-1))));
        row.add(label);
        row.add(new ButtonWidget(0, 0, 40, 20, UiText.literal("+"), () -> defer(() -> changeRadius(1))));
        return row;
    }

    private Widget swatchRow() {
        HStack row = new HStack(0, 0, 0, 0);
        row.setSizeMode(SizeMode.WRAP, SizeMode.WRAP);
        row.setGap(4);
        for (int rgb : PRESETS) {
            row.add(new SwatchWidget(rgb, selectedRgb, picked -> defer(() -> applyPreset(picked))));
        }
        return row;
    }

    private Widget bottomRow() {
        HStack row = new HStack(0, 0, 0, 0);
        row.setSizeMode(SizeMode.FILL, SizeMode.WRAP);
        row.setGap(8);

        ButtonWidget reset = new ButtonWidget(0, 0, 0, 20,
                NovaTexts.tr("label.industrialtech.clear_color"), () -> defer(this::resetColors));
        reset.setWeight(1f);

        ButtonWidget done = new ButtonWidget(0, 0, 0, 20,
                NovaTexts.tr("label.industrialtech.success"), () -> defer(this::onClose));
        done.setWeight(1f);
        done.setStyle(primaryStyle());

        row.add(reset);
        row.add(done);
        return row;
    }

    /** Зелёная акцентная кнопка: остальное берётся из текущей темы. */
    private static ButtonStyle primaryStyle() {
        ButtonStyle b = ThemeManager.current().get(Styles.BUTTON);
        return new ButtonStyle(0xCC2F8F72, 0xEE38A584, 0xCC236B56, b.disabled(),
                b.border(), b.focusedBorder(), b.text(), b.disabledText(),
                b.paddingX(), b.height(), b.textShadow(), b.radius());
    }

    // ------------------------------------------------------------------ actions

    /** Выполнить после завершения обработки клика: действия могут пересоздать или закрыть экран. */
    private static void defer(Runnable action) {
        Minecraft.getInstance().tell(action);
    }

    private void onBlurToggled(boolean enabled) {
        boolean wasEnabled = IBlurConfig.CLIENT.blurEnabled.get();
        if (enabled == wasEnabled) {
            return;
        }
        IBlurConfig.CLIENT.blurEnabled.set(enabled);
        IBlurConfig.saveConfig();
        // excluded = true выключает шейдер, то есть новое состояние "выключено" == старое "включено"
        IClientForgeEvents.onConfigChange(wasEnabled);
    }

    private void changeRadius(int delta) {
        int value = Mth.clamp(IBlurConfig.CLIENT.radius.get() + delta, MIN_RADIUS, MAX_RADIUS);
        IBlurConfig.CLIENT.radius.set(value);
        IClientForgeEvents.updateUniform("Radius", value);
        IBlurConfig.saveConfig();
        radius.set(value);
    }

    private void applyPreset(int rgb) {
        String hex = String.format("%06X", rgb);
        IBlurConfig.applyColors(hex + String.format("%02X", TINT_TOP_ALPHA), BOTTOM_COLOR);
        selectedRgb.set(rgb);
    }

    private void resetColors() {
        IBlurConfig.applyColors(BOTTOM_COLOR, BOTTOM_COLOR);
        selectedRgb.set(-1);
    }

    /** RGB первого цвета из конфига (первые 6 hex-символов строки RRGGBBAA) или -1. */
    private static int readSelectedRgb() {
        try {
            return Integer.parseInt(IBlurConfig.CLIENT.colorFirstRaw.get().substring(0, 6), 16);
        } catch (RuntimeException e) {
            return -1;
        }
    }

    // ------------------------------------------------------------------ swatch

    /** Квадрат с цветом пресета: у выбранного рамка белая и толще. При наведении ничего не меняется. */
    private static final class SwatchWidget extends Widget {
        private static final int SIZE = 20;

        private final int rgb;
        private final Signal<Integer> selected;
        private final IntConsumer onPick;
        private boolean pressedState = false;

        private SwatchWidget(int rgb, Signal<Integer> selected, IntConsumer onPick) {
            super(0, 0, SIZE, SIZE);
            this.rgb = rgb;
            this.selected = selected;
            this.onPick = onPick;
        }

        @Override
        protected void renderSelf(UiGraphics g, int mouseX, int mouseY, float partialTick) {
            boolean isSelected = selected.peek() == rgb;
            int ax = absX();
            int ay = absY();
            int w = getWidth();
            int h = getHeight();
            int inset = isSelected ? 2 : 1;
            g.fillRounded(ax, ay, ax + w, ay + h, 4, isSelected ? 0xFFFFFFFF : 0x66FFFFFF);
            g.fillRounded(ax + inset, ay + inset, ax + w - inset, ay + h - inset, 3, 0xFF000000 | rgb);
        }

        @Override
        protected void onPointerDown(PointerEvent e) {
            if (e.getButton() != MouseButton.LEFT) return;
            pressedState = true;
            e.stopPropagation();
        }

        @Override
        protected void onPointerUp(PointerEvent e) {
            if (e.getButton() != MouseButton.LEFT || !pressedState) return;
            pressedState = false;
            if (contains(e.getX(), e.getY())) {
                UiContext.platform().playClickSound();
                onPick.accept(rgb);
            }
            e.stopPropagation();
        }

        @Override
        protected void onPointerCancel(PointerEvent e) {
            pressedState = false;
        }
    }
}