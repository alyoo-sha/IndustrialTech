package org.mod.industrialtech.client;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.lwjgl.glfw.GLFW;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.client.render.ModBlurConfig;
import org.mod.industrialtech.client.screen.ModBlurSettingsScreen;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Industrialtech.MOD_ID)
public class ModClientForgeEvents {
    /** uses `minecraft` as namespace because optifine breaks the shader loading */
    private static final ResourceLocation fade_in_blur = new ResourceLocation("shaders/post/fade_in_blur.json");
    private static float prevProgress = -1;
    private static long start;
    private static Field _listShaders;
    private static String lastShader;


    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT && event.getAction() == 1) {
            Minecraft.getInstance().setScreen(new ModBlurSettingsScreen());
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        // 🔥 ВСЕГДА обновляем размутие когда экран открыт
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().screen != null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.gameRenderer.currentEffect() != null) {
                try {
                    float progress = getProgress();
                    if (progress != prevProgress) {
                        prevProgress = progress;
                        updateUniform("Progress", progress);
                    }
                } catch (IllegalStateException e) {
                    // Конфиг еще не загружен, пропускаем
                }
            }
        }
    }

    public static void updateUniform(String name, float value) {
        if (_listShaders == null) return;
        PostChain sg = Minecraft.getInstance().gameRenderer.currentEffect();
        if(sg != null) {
            try {
                @SuppressWarnings("unchecked")
                List<PostPass> shaders = (List<PostPass>) _listShaders.get(sg);
                for (PostPass s : shaders) {
                    Uniform su = s.getEffect().getUniform(name);
                    if (su != null) {
                        su.set(value);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SubscribeEvent
    public static void onGuiChange(ScreenEvent.Init.Post event) throws SecurityException {
        try {
            String screenName = event.getScreen().getClass().getName();
            boolean isExcluded = ModBlurConfig.guiExlusions.contains(screenName);
            boolean blurDisabled = !ModBlurConfig.CLIENT.blurEnabled.get();

            // 🔥 НЕ ИСКЛЮЧАЕМ EMI, РАЗМЫТИЕ ДОЛЖНО РАБОТАТЬ И ТУДА
            // Удалили проверку на EMI, чтобы размутие применялось ко всем экранам

            updateShader(isExcluded || blurDisabled);
        } catch (IllegalStateException e) {
            // Конфиг еще не загружен, пропускаем
        }
    }


    public static void onConfigChange(boolean excluded) {
        try {
            updateShader(excluded);
        } catch (IllegalStateException e) {
            // Конфиг еще не загружен, пропускаем
        }
    }

    public static void updateShader(boolean excluded) {
        try {
            if (_listShaders == null) {
                _listShaders = ObfuscationReflectionHelper.findField(PostChain.class, "f_110009_");
            }

            Minecraft mc = Minecraft.getInstance();

            if (mc.level != null) {
                GameRenderer er = mc.gameRenderer;
                PostChain postChain = er.currentEffect();

                if (!excluded) {
                    boolean needToLoad = false;

                    if (postChain == null) {
                        needToLoad = true;
                    } else if (!fade_in_blur.toString().equals(postChain.getName())) {
                        needToLoad = true;
                        lastShader = postChain.getName();
                    }

                    if (needToLoad) {
                        er.loadEffect(fade_in_blur);
                        try {
                            updateUniform("Radius", ModBlurConfig.CLIENT.radius.get());
                        } catch (IllegalStateException e) {
                            // Конфиг не готов, используем значение по умолчанию
                            updateUniform("Radius", 0.5f);
                        }
                        start = System.currentTimeMillis();
                    }

                    PostChain currentChain = er.currentEffect();
                    if (currentChain != null && fade_in_blur.toString().equals(currentChain.getName())) {
                        if (start != -1) {
                            updateUniform("Progress", getProgress());
                        }
                    }

                } else if (postChain != null && fade_in_blur.toString().equals(postChain.getName())) {
                    if (lastShader != null) {
                        try {
                            if (lastShader.contains(":")) {
                                String[] parts = lastShader.split(":", 2);
                                er.loadEffect(new ResourceLocation(parts[0], parts[1]));
                            } else {
                                er.loadEffect(new ResourceLocation(lastShader));
                            }
                        } catch (Exception ignored) {
                            er.shutdownEffect();
                        }
                        lastShader = null;
                    } else {
                        er.shutdownEffect();
                    }
                    start = -1;
                    prevProgress = -1;
                }
            } else {
                start = -1;
                prevProgress = -1;
            }
        } catch (IllegalStateException e) {
            // Конфиг еще не загружен, пропускаем обновление
        }
    }
    // ...existing code...


    private static float getProgress() {
        try {
            return Math.min((System.currentTimeMillis() - start) / (float) ModBlurConfig.CLIENT.fadeTime.get(), 1);
        } catch (IllegalStateException e) {
            // Конфиг еще не загружен, используем значение по умолчанию
            return Math.min((System.currentTimeMillis() - start) / 200f, 1);
        }
    }

    public static int getBackgroundColor(boolean second) {
        int color = second ? ModBlurConfig.colorSecond : ModBlurConfig.colorFirst;
        int a = color >> 24;
        int r = (color >> 16) & 0xFF;
        int b = (color >> 8) & 0xFF;
        int g = color & 0xFF;
        float prog = getProgress();
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
