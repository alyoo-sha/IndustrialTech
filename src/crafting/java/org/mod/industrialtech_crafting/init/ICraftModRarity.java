package org.mod.industrialtech_crafting.init;

import net.minecraft.world.item.Rarity;

public enum ICraftModRarity {
    COMMON(0xC7CDD6),
    UNCOMMON(0x38E26D),
    RARE(0x4DA6FF),
    EPIC(0xB04CFF),
    LEGENDARY(0xFF6600),
    MYTHIC(0xFF0000);
//17DDF6
//    E31A62
    public final Rarity rarity;

    ICraftModRarity(int color) {
        this.rarity = Rarity.create(name(), style -> style.withColor(color));
    }
}
