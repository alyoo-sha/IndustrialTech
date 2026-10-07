package org.mod.industrialtech.client.render;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.client.ModClientForgeEvents;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Industrialtech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlurConfig {

    private static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    public static int colorFirst, colorSecond;
    public static Set<String> guiExlusions = new HashSet<>();

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void registerConfig() {
        try {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModBlurConfig.clientSpec);
            // Не вызываем updateColors() сразу - конфиг еще не загружен
            // updateColors() будет вызван в onReload() когда конфиг загружен
        } catch (Exception e) {
            Industrialtech.LOGGER.error("Failed to register ModBlurConfig", e);
        }
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent configEvent) {
        try {
            // Загружаем цвета только после загрузки конфига
            if (configEvent.getConfig().getSpec() == clientSpec) {
                updateColors();
            }
        } catch (Exception e) {
            Industrialtech.LOGGER.error("Failed to update blur config colors", e);
        }
    }

    public static void saveConfig() {
        try {
            clientSpec.save();
        } catch (Exception e) {
            Industrialtech.LOGGER.error("Failed to save blur config", e);
        }
    }

    private static void updateColors() {
        try {
            int colorFirst = Integer.parseUnsignedInt(CLIENT.colorFirstRaw.get(), 16);
            ModBlurConfig.colorFirst = convertColor(colorFirst);
            int colorSecond = Integer.parseUnsignedInt(CLIENT.colorSecondRaw.get(), 16);
            ModBlurConfig.colorSecond = convertColor(colorSecond);
            guiExlusions.clear();;
            guiExlusions.addAll(CLIENT.guiExclusions.get());
            Minecraft.getInstance().execute(() -> ModClientForgeEvents.updateUniform("Radius", CLIENT.radius.get()));
        } catch (Exception e) {
            Industrialtech.LOGGER.error("Failed to update blur colors", e);
        }
    }

    private static int convertColor(int rgba) {
        return  (((rgba) & 0xFF) << 24) |
                (((rgba >> 24) & 0xFF) << 16) |
                (((rgba >> 16) & 0xFF) << 8)  |
                (((rgba >> 8) & 0xFF) << 0);
    }

    public static class Client {

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> guiExclusions;
        public final ForgeConfigSpec.IntValue fadeTime;
        public final ForgeConfigSpec.IntValue radius;
        public final ForgeConfigSpec.ConfigValue<String> colorFirstRaw;
        public final ForgeConfigSpec.ConfigValue<String> colorSecondRaw;
        public final ForgeConfigSpec.BooleanValue blurEnabled;

        private Client(ForgeConfigSpec.Builder builder) {
            this.guiExclusions = builder.comment("Список классов, которые должны быть исключены из шейдера размытия.")
                    .defineList("guiExclusions", Lists.newArrayList(ChatScreen.class.getName(), InBedChatScreen.class.getName()), o -> {
                        try {
                            //noinspection ConstantConditions
                            return o instanceof String && Class.forName((String) o) != null;
                        } catch (ClassNotFoundException e) {
                            return false;
                        }
                    });

            this.fadeTime = builder.comment("Время, необходимое для того, чтобы размытие исчезло, в мс.").defineInRange("fadeTime", 100, 0, 10 * 1000);
            this.radius = builder.comment("Радиус эффекта размытия. Определяет, насколько \"сильным\" будет размытие.").defineInRange("radius", 12, 1, 100);
            this.colorFirstRaw = builder.comment("Верхний цвет фонового градиента. Приведен в шестнадцатеричном формате RGBA.").define("gradientStartColor", "0000003f", this::isHexValue);
            this.colorSecondRaw = builder.comment("Нижний цвет фонового градиента. Приведен в шестнадцатеричном формате RGBA.").define("gradientEndColor", "0000003f", this::isHexValue);
            this.blurEnabled = builder.comment("Включить или отключить размытие экрана.").define("blurEnabled", true);
        }

        private boolean isHexValue(Object o) {
            if (o instanceof String) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Integer.parseUnsignedInt((String) o, 16);
                    return true;
                } catch (NumberFormatException ignored) {}
            }
            return false;
        }
    }
}
