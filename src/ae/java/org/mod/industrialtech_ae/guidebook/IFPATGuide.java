package org.mod.industrialtech_ae.guidebook;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.slf4j.Logger;

public class IFPATGuide {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation ID = Industrialtech_ae.makeId("book");

    public static void init() {
        ModList modList = ModList.get();
        if (modList == null || modList.isLoaded("guideme")) {
            try {
                Class<?> guideClass = Class.forName("guideme.Guide");
                Object guideBuilder = guideClass.getMethod("builder", ResourceLocation.class).invoke(null, ID);
                Class<?> guideBuilderClass = guideBuilder.getClass();
                guideBuilderClass.getMethod("folder", String.class).invoke(guideBuilder, "ifpat_guidebook");
                guideBuilderClass.getMethod("build").invoke(guideBuilder);
                LOGGER.info("Registered GuideME guide {}", ID);
            } catch (ReflectiveOperationException e) {
                LOGGER.warn("Failed to initialize GuideME integration.", e);
            }

        }
    }

    public static ItemStack createGuideItem() {
        ModList modList = ModList.get();
        if (modList == null || modList.isLoaded("guideme")) {
            try {
                Class<?> guidesClass = Class.forName("guideme.Guides");
                Object guideItem = guidesClass.getMethod("createGuideItem", ResourceLocation.class).invoke(null, ID);
                if (guideItem instanceof ItemStack stack) {
                    return stack;
                }
            } catch (ReflectiveOperationException e) {
                LOGGER.warn("Failed to create GuideME item for {}.", ID, e);
            }

        }
        return ItemStack.EMPTY;
    }

    private IFPATGuide() {
    }
}
