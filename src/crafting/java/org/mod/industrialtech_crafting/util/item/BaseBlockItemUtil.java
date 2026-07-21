package org.mod.industrialtech_crafting.util.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class BaseBlockItemUtil extends BlockItem {
    public BaseBlockItemUtil(Block pBlock) {
        super(pBlock, new Properties());
    }

    public BaseBlockItemUtil(Block block, Function<Properties, Properties> properties) {
        super(block, (Properties)properties.apply(new Properties()));
    }
}
