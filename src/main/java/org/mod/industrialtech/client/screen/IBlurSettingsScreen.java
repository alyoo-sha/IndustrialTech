package org.mod.industrialtech.client.screen;

import com.daqem.uilib.client.gui.AbstractScreen;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.ButtonComponent;
import com.daqem.uilib.client.gui.component.SolidColorComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.mod.industrialtech.client.IClientForgeEvents;
import org.mod.industrialtech.client.render.IBlurConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Экран настроек размытия на UILib (ветка 1.20.1).
 * Заменяет прежний ModBlurSettingsScreen: тот же конструктор без аргументов,
 * так что вызов в ModClientForgeEvents.onKey менять не нужно.
 */
public class IBlurSettingsScreen extends AbstractScreen {

    private static final int PANEL_W = 232;
    private static final int PANEL_H = 160;
    private static final int PAD = 16;

    private static final int MIN_RADIUS = 1;
    /** Больше 30 уже заметно нагружает видеокарту (4 прохода по 2R+1 выборок). */
    private static final int MAX_RADIUS = 30;

    /** Прозрачность оттенка сверху и снизу градиента (0x00..0xFF). */
    private static final int TINT_TOP_ALPHA = 0x70;
    private static final String  BOTTOM_COLOR = "0000003f";

    /** RGB пресетов: синий, красный, фиолетовый, зелёный, жёлтый, оранжевый, розовый, голубой. */
    private static final int[] PRESETS = {
            0x0190FF, 0xFF0000, 0xAA00FF, 0x00FF00, 0xFFFF00, 0xFFA500, 0xFF69B4, 0x00FFFF
    };

    private final List<SwatchComponent> swatches = new ArrayList<>();
    private TextComponent radiusLabel;
    private TextComponent toggleLabel;
    private int radiusCenterX;

    public IBlurSettingsScreen() {
        super(Component.literal("Настройки размытия"));
    }

    // ------------------------------------------------------------------ UILib hooks

    @Override
    public void startScreen() {
        this.setPauseScreen(false);
        // Фон UILib по умолчанию (тёмный градиент) убираем: иначе он перекрывает и блюр, и тонировку.
        this.setBackground(null);
        buildUi();
    }

    @Override
    public void onResizeScreenRepositionComponents(int width, int height) {
        // Компоненты UILib хранят абсолютные координаты, поэтому при ресайзе проще пересобрать.
        this.getComponents().clear();
        this.swatches.clear();
        buildUi();
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }

    /**
     * Рисуем ванильный фон Screen.renderBackground(GuiGraphics): именно в нём MixinScreen подменяет
     * цвета градиента на цвета из конфига. Это и есть живой предпросмотр выбранного цвета.
     * (AbstractScreen из UILib этот метод не вызывает, поэтому без переопределения тонировки не видно.)
     */
    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
    }

    // ------------------------------------------------------------------ layout

    private void buildUi() {
        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;
        int centerX = this.width / 2;
        int innerW = PANEL_W - PAD * 2;

        // Панель
        addComponent(new SolidColorComponent(px, py, PANEL_W, PANEL_H, 0xE0141414));
        frame(px, py, PANEL_W, PANEL_H, 0xFF5A5A5A);

        // Заголовок
        addLabel("Настройки размытия", centerX, py + 8, 0xFFFFFFFF);

        // Вкл/выкл
        toggleLabel = addButton(px + PAD, py + 24, innerW, 20, "", this::toggleBlur);
        setLabelText(toggleLabel, blurEnabledText(), innerW / 2);

        // Радиус:  [ − ]  Радиус: N  [ + ]
        addButton(px + PAD, py + 50, 60, 20, "-", () -> changeRadius(-1));
        addButton(px + PAD + innerW - 60, py + 50, 60, 20, "+", () -> changeRadius(1));
        radiusCenterX = centerX;
        radiusLabel = addLabel("", radiusCenterX, py + 56, 0xFFFFFFFF);
        setLabelText(radiusLabel, radiusText(), radiusCenterX);

        // Цвета
        addLabel("Цвет затемнения", centerX, py + 78, 0xFFAAAAAA);
        int size = 20;
        int gap = 4;
        int rowW = PRESETS.length * size + (PRESETS.length - 1) * gap;
        int sx = px + (PANEL_W - rowW) / 2;
        int selected = readSelectedRgb();
        for (int i = 0; i < PRESETS.length; i++) {
            SwatchComponent swatch = new SwatchComponent(sx + i * (size + gap), py + 92, size, PRESETS[i]);
            swatch.setSelected(PRESETS[i] == selected);
            swatch.setOnClickEvent((clicked, screen, mouseX, mouseY, button) -> {
                if (button == 0) applyPreset(clicked.getRgb());
            });
            swatches.add(swatch);
            addComponent(swatch);
        }

        // Низ
        int bw = (innerW - 8) / 2;
        addButton(px + PAD, py + 124, bw, 20, "Сброс цвета", this::resetColors);
        addButton(px + PAD + bw + 8, py + 124, bw, 20, "Готово", this::onClose);
    }

    // ------------------------------------------------------------------ actions

    private void toggleBlur() {
        boolean wasEnabled = IBlurConfig.CLIENT.blurEnabled.get();
        IBlurConfig.CLIENT.blurEnabled.set(!wasEnabled);
        IBlurConfig.saveConfig();
        // excluded = true выключает шейдер, то есть новое состояние "выключено" == старое "включено"
        IClientForgeEvents.onConfigChange(wasEnabled);
        setLabelText(toggleLabel, blurEnabledText(), (PANEL_W - PAD * 2) / 2);
    }

    private void changeRadius(int delta) {
        int value = Mth.clamp(IBlurConfig.CLIENT.radius.get() + delta, MIN_RADIUS, MAX_RADIUS);
        IBlurConfig.CLIENT.radius.set(value);
        IClientForgeEvents.updateUniform("Radius", value);
        IBlurConfig.saveConfig();
        setLabelText(radiusLabel, radiusText(), radiusCenterX);
    }

    private void applyPreset(int rgb) {
        String hex = String.format("%06X", rgb);
        IBlurConfig.applyColors(
                hex + String.format("%02X", TINT_TOP_ALPHA), BOTTOM_COLOR);
        refreshSwatches(rgb);
    }

    private void resetColors() {
        IBlurConfig.applyColors(BOTTOM_COLOR, BOTTOM_COLOR);
        refreshSwatches(-1);
    }

    private void refreshSwatches(int selectedRgb) {
        for (SwatchComponent swatch : swatches) {
            swatch.setSelected(swatch.getRgb() == selectedRgb);
        }
    }

    /** RGB первого цвета из конфига (первые 6 hex-символов строки RRGGBBAA) или -1. */
    private int readSelectedRgb() {
        try {
            return Integer.parseInt(IBlurConfig.CLIENT.colorFirstRaw.get().substring(0, 6), 16);
        } catch (RuntimeException e) {
            return -1;
        }
    }

    private String radiusText() {
        return "Радиус: " + IBlurConfig.CLIENT.radius.get();
    }

    private String blurEnabledText() {
        return "Размытие: " + (IBlurConfig.CLIENT.blurEnabled.get() ? "вкл" : "выкл");
    }

    // ------------------------------------------------------------------ helpers

    /** Подпись без добавления на экран (для вложения в кнопку). */
    private TextComponent makeLabel(String text, int centerX, int y, int color) {
        Text uiText = new Text(this.font, Component.literal(text));
        uiText.setTextColor(color);
        uiText.setShadow(true);
        return new TextComponent(centerX - this.font.width(text) / 2, y, uiText);
    }

    private TextComponent addLabel(String text, int centerX, int y, int color) {
        TextComponent label = makeLabel(text, centerX, y, color);
        addComponent(label);
        return label;
    }

    /** Меняет текст подписи и пересчитывает её x, чтобы она осталась по центру centerX. */
    private void setLabelText(TextComponent label, String text, int centerX) {
        label.getText().setText(Component.literal(text));
        label.setX(centerX - this.font.width(text) / 2);
    }

    /**
     * Кнопка UILib с ванильной текстурой и hover-состоянием. Подпись кладём ДОЧЕРНИМ компонентом:
     * hover-состояние в UILib рисует только детей, а не text самой кнопки.
     * Координаты задаём сразу в конструкторе (hover-копия запоминает их в момент создания).
     */
    private TextComponent addButton(int x, int y, int w, int h, String text, Runnable action) {
        ButtonComponent button = new ButtonComponent(Textures.MINECRAFT_BUTTON, x, y, w, h);
        button.getHoverState().setTexture(Textures.MINECRAFT_BUTTON_HOVERED);
        // Без непустого hover-обработчика UILib не рисует hover-состояние
        button.setOnHoverEvent((hovered, screen, mouseX, mouseY, delta) -> {});
        button.setOnClickEvent((clicked, screen, mouseX, mouseY, mouseButton) -> {
            if (mouseButton == 0) action.run();
        });

        TextComponent label = makeLabel(text, w / 2, (h - this.font.lineHeight) / 2, 0xFFFFFFFF);
        button.addChild(label);
        addComponent(button);
        return label;
    }

    private void frame(int x, int y, int w, int h, int color) {
        addComponent(new SolidColorComponent(x, y, w, 1, color));
        addComponent(new SolidColorComponent(x, y + h - 1, w, 1, color));
        addComponent(new SolidColorComponent(x, y, 1, h, color));
        addComponent(new SolidColorComponent(x + w - 1, y, 1, h, color));
    }

    // ------------------------------------------------------------------ swatch

    /** Квадрат с цветом пресета: рамка светлеет при наведении, у выбранного она толще. */
    private static final class SwatchComponent extends AbstractComponent<SwatchComponent> {
        private final int rgb;
        private boolean selected;

        private SwatchComponent(int x, int y, int size, int rgb) {
            super(null, x, y, size, size);
            this.rgb = rgb;
        }

        int getRgb() {
            return rgb;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            boolean highlighted = selected || isTotalHovered(mouseX, mouseY);
            int inset = selected ? 2 : 1;
            graphics.fill(0, 0, getWidth(), getHeight(), highlighted ? 0xFFFFFFFF : 0xFF555555);
            graphics.fill(inset, inset, getWidth() - inset, getHeight() - inset, 0xFF000000 | rgb);
        }
    }
}
