package org.mod.industrialtech_ae.block;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import org.jetbrains.annotations.NotNull;
import org.mod.industrialtech_ae.block.entities.IAeCraftingBlockEntity;
import org.mod.industrialtech_ae.me.IAeCraftingUnitType;

public class IAeCraftingUnitBlock extends AbstractCraftingUnitBlock<IAeCraftingBlockEntity> implements IAeICraftingBlock {
    public IAeCraftingUnitBlock(Properties props, IAeCraftingUnitType type) {
        super(props, type);
        this.IAeCraftingType = type;
    }

    public final IAeCraftingUnitType IAeCraftingType;

    @Override
    public @NotNull IAeCraftingUnitType getIAeCraftingUnitType() {
        return this.IAeCraftingType;
    }
}
