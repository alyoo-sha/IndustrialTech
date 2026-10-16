package org.mod.industrialtech_ae.common.block;

import appeng.block.crafting.CraftingMonitorBlock;
import org.jetbrains.annotations.NotNull;
import org.mod.industrialtech_ae.me.IAeCraftingUnitType;

public class IAeCraftingMonitorBlock extends CraftingMonitorBlock implements IAeICraftingBlock {

    public final IAeCraftingUnitType IAeCraftingType;

    public IAeCraftingMonitorBlock(IAeCraftingUnitType type) {
        super(type);
        this.IAeCraftingType = type;
    }

    @Override
    public @NotNull IAeCraftingUnitType getIAeCraftingUnitType() {
        return this.IAeCraftingType;
    }
}
