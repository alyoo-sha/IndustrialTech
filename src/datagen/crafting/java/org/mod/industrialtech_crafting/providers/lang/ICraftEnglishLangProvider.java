package org.mod.industrialtech_crafting.providers.lang;

import net.minecraft.data.PackOutput;
import org.mod.industrialtech_crafting.Industrialtech_crafting;
import org.mod.industrialtech_crafting.init.ICraftModBlock;

public class ICraftEnglishLangProvider extends ICraftLangProvider {
    public ICraftEnglishLangProvider(PackOutput output) {
        super(output, Industrialtech_crafting.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ICraftModBlock.LUMINESSENCE_BLOCK.get(), "Luminessence Block");
        addBlock(ICraftModBlock.BLACK_IRON_BLOCK.get(), "Black Iron Block");
        addBlock(ICraftModBlock.REDSTONE_INGOT_BLOCK.get(), "Redstone Ingot Block");
        addBlock(ICraftModBlock.ENHANCED_REDSTONE_INGOT_BLOCK.get(), "Enhanced Redstone Ingot Block");
        addBlock(ICraftModBlock.ENDER_INGOT_BLOCK.get(), "Ender Ingot Block");
        addBlock(ICraftModBlock.ENHANCED_ENDER_INGOT_BLOCK.get(), "Enhanced Ender Ingot Block");
        addBlock(ICraftModBlock.CRYSTALTINE_BLOCK.get(), "Crystaltine Block");
        addBlock(ICraftModBlock.NETHER_STAR_BLOCK.get(), "Nether Star Block");
        addBlock(ICraftModBlock.FLUX_STAR_BLOCK.get(), "Flux Star Block");
        addBlock(ICraftModBlock.ENDER_STAR_BLOCK.get(), "Ender Star Block");
    }
}
