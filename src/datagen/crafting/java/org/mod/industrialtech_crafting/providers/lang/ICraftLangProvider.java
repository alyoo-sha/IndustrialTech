package org.mod.industrialtech_crafting.providers.lang;


import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public abstract class ICraftLangProvider extends LanguageProvider {
    protected final String modId;
    public ICraftLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.modId = modid;
    }

    // Метод для добавления предметов
    protected void addItem(String name, String translate) {
        add("item." + modId + "." + name, translate);
    }

    // Метод для добавления блоков
    protected void addBlock(String name, String translate) {
        add("block." + modId + "." + name, translate);
    }

    // Метод для добавления блоков
    protected void addBlock(Block name, String translate) {
        // Если у предмета есть GUI или другие особенности – можно добавить логику
        // Но обычно предметы не имеют контейнерных ключей, поэтому просто вызываем add с descriptionId
        add(name.getDescriptionId(), translate);
    }

    // Метод для добавления подсказок к предметам
    protected void addTooltip(ItemLike item, String tooltip) {
        add(item.asItem().getDescriptionId() + ".tooltip", tooltip);
    }

    // Метод для добавления подсказок с дополнительными параметрами
    protected void addTooltipWithKey(String itemKey, String tooltipKey, String tooltip) {
        add(itemKey + ".tooltip." + tooltipKey, tooltip);
    }

    // Вспомогательный метод для получения пути предмета
    protected String getItemPath(ItemLike item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).getPath();
    }
}
