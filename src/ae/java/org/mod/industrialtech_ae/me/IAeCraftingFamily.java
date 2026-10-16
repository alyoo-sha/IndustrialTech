package org.mod.industrialtech_ae.me;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.core.init.IAeModBlock;

public enum IAeCraftingFamily {
    COMMON,
    MEGA,
    GIGA;

    public Block getUnitBaseBlock() {
        RegistryObject<? extends Block> deferredBlock = switch (this) {
            case COMMON -> IAeModBlock.COMMON_CRAFTING_UNIT_BLOCK;
            case MEGA -> IAeModBlock.MEGA_CRAFTING_UNIT_BLOCK;
            case GIGA -> IAeModBlock.GIGA_CRAFTING_UNIT_BLOCK;
        };
        return deferredBlock.get();
    }

    public Block getMonitorBlock()
    {
        RegistryObject<? extends Block> deferredBlock = switch (this)
        {
            case COMMON -> IAeModBlock.COMMON_CRAFTING_MONITOR_BLOCK;
            case MEGA -> IAeModBlock.MEGA_CRAFTING_UNIT_BLOCK;
            case GIGA -> IAeModBlock.GIGA_CRAFTING_UNIT_BLOCK;
        };
        return deferredBlock.get();
    }
}
