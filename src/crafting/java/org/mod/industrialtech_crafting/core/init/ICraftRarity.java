package org.mod.industrialtech_crafting.core.init;

import net.minecraft.world.item.Rarity;

public enum ICraftRarity {
    CUSTOM_COMMON(0xC7CDD6),
    CUSTOM_UNCOMMON(0x38E26D),
    CUSTOM_RARE(0x4DA6FF),
    CUSTOM_EPIC(0xB04CFF),
    CUSTOM_LEGENDARY(0xFF6600),
    CUSTOM_MYTHIC(0xFF0000);
//17DDF6
//    E31A62
    public final Rarity rarity;

    ICraftRarity(int color) {
        this.rarity = Rarity.create(name(), style -> style.withColor(color));
    }
}
