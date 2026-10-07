package org.mod.industrialtech.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.client.ModClientForgeEvents;
import org.mod.industrialtech.client.render.ModBlurConfig;

public class ModBlurSettingsScreen extends Screen {

    private static final ResourceLocation BG =
            new ResourceLocation(Industrialtech.MOD_ID, "textures/gui/elevator_gui.png");

    // 🎨 ОДНА ОБЩАЯ ТЕКСТУРА ДЛЯ ВСЕХ КНОПОК ЦВЕТОВ (атлас)
    private static final ResourceLocation BTN_ATLAS = new ResourceLocation(Industrialtech.MOD_ID, "textures/gui/buttons/buttons_atlas.png");

    // 📐 Размеры и координаты кнопок в атласе
    // Атлас: 256×256 пикселей
    // Верхняя половина (0-80): обычные кнопки
    // Нижняя половина (80-160): затемненные версии для hover
    private static final int BTN_TEX_WIDTH = 256;   // Ширина всей текстуры
    private static final int BTN_TEX_HEIGHT = 256;  // Высота всей текстуры

    // Координаты каждой кнопки (U, V) в атласе - ОБЫЧНОЕ СОСТОЯНИЕ
    // Ряд 1 (Y = 0)
    private static final int BLUE_U = 0,    BLUE_V = 0;       // слева
    private static final int RED_U = 90,    RED_V = 0;        // справа

    // Ряд 2 (Y = 20)
    private static final int PURPLE_U = 0,  PURPLE_V = 20;    // слева
    private static final int GREEN_U = 90,  GREEN_V = 20;     // справа

    // Ряд 3 (Y = 40)
    private static final int YELLOW_U = 0,  YELLOW_V = 40;    // слева
    private static final int ORANGE_U = 90, ORANGE_V = 40;    // справа

    // Ряд 4 (Y = 60)
    private static final int PINK_U = 0,    PINK_V = 60;      // слева
    private static final int CYAN_U = 90,   CYAN_V = 60;      // справа

    // Координаты затемненных версий (для hover) - НИЖНЯЯ ПОЛОВИНА
    // Ряд 1 (Y = 80)
    private static final int BLUE_DARK_U = 0,    BLUE_DARK_V = 80;
    private static final int RED_DARK_U = 90,    RED_DARK_V = 80;

    // Ряд 2 (Y = 100)
    private static final int PURPLE_DARK_U = 0,  PURPLE_DARK_V = 100;
    private static final int GREEN_DARK_U = 90,  GREEN_DARK_V = 100;

    // Ряд 3 (Y = 120)
    private static final int YELLOW_DARK_U = 0,  YELLOW_DARK_V = 120;
    private static final int ORANGE_DARK_U = 90, ORANGE_DARK_V = 120;

    // Ряд 4 (Y = 140)
    private static final int PINK_DARK_U = 0,    PINK_DARK_V = 140;
    private static final int CYAN_DARK_U = 90,   CYAN_DARK_V = 140;

    public ModBlurSettingsScreen() {
        super(Component.literal("Blur Settings"));
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int y = height / 2 - 60;

        // 🔥 КНОПКА: СИЛА BLUR
        addRenderableWidget(Button.builder(
                Component.literal("Radius: " + ModBlurConfig.CLIENT.radius.get()),
                btn -> {
                    int val = ModBlurConfig.CLIENT.radius.get() + 1;
                    if (val > 25) val = 1;

                    ModBlurConfig.CLIENT.radius.set(val);
                    ModClientForgeEvents.updateUniform("Radius", val);
                    ModBlurConfig.saveConfig();

                    btn.setMessage(Component.literal("Radius: " + val));
                }
        ).bounds(centerX - 100, y, 200, 20).build());

        y += 24;

        // 🎨 ТЕКСТУРИРОВАННЫЕ КНОПКИ ЦВЕТОВ (из одного атласа)
        addRenderableWidget(new TexturedButton(
                centerX - 100, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, BLUE_U, BLUE_V, BLUE_DARK_U, BLUE_DARK_V,
                btn -> applyColorPreset("0190FFFF", "0000003f")
        ));

        addRenderableWidget(new TexturedButton(
                centerX + 10, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, RED_U, RED_V, RED_DARK_U, RED_DARK_V,
                btn -> applyColorPreset("FF0000FF", "0000003f")
        ));

        y += 24;

        addRenderableWidget(new TexturedButton(
                centerX - 100, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, PURPLE_U, PURPLE_V, PURPLE_DARK_U, PURPLE_DARK_V,
                btn -> applyColorPreset("AA00FFFF", "0000003f")
        ));

        addRenderableWidget(new TexturedButton(
                centerX + 10, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, GREEN_U, GREEN_V, GREEN_DARK_U, GREEN_DARK_V,
                btn -> applyColorPreset("00FF00FF", "0000003f")
        ));

        y += 24;

        addRenderableWidget(new TexturedButton(
                centerX - 100, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, YELLOW_U, YELLOW_V, YELLOW_DARK_U, YELLOW_DARK_V,
                btn -> applyColorPreset("FFFF00FF", "0000003f")
        ));

        addRenderableWidget(new TexturedButton(
                centerX + 10, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, ORANGE_U, ORANGE_V, ORANGE_DARK_U, ORANGE_DARK_V,
                btn -> applyColorPreset("FFA500FF", "0000003f")
        ));

        y += 24;

        addRenderableWidget(new TexturedButton(
                centerX - 100, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, PINK_U, PINK_V, PINK_DARK_U, PINK_DARK_V,
                btn -> applyColorPreset("FF69B4FF", "0000003f")
        ));

        addRenderableWidget(new TexturedButton(
                centerX + 10, y, 90, 20,
                BTN_ATLAS, BTN_TEX_WIDTH, BTN_TEX_HEIGHT, CYAN_U, CYAN_V, CYAN_DARK_U, CYAN_DARK_V,
                btn -> applyColorPreset("00FFFFFF", "0000003f")
        ));
    }

    private void applyColorPreset(String first, String second) {
        ModBlurConfig.CLIENT.colorFirstRaw.set(first);
        ModBlurConfig.CLIENT.colorSecondRaw.set(second);

        ModBlurConfig.colorFirst = Integer.parseUnsignedInt(first, 16);
        ModBlurConfig.colorSecond = Integer.parseUnsignedInt(second, 16);

        ModBlurConfig.saveConfig();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {

        // 🔥 ФОН КАРТИНКОЙ
        gui.blit(
                BG,
                45, 70,
                0, 0,
                width, height,
                width, height
        );


        super.render(gui, mouseX, mouseY, partialTick);
    }
}
