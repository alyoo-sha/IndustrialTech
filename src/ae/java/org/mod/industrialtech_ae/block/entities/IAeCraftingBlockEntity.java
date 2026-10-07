package org.mod.industrialtech_ae.block.entities;

import appeng.blockentity.crafting.CraftingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mod.industrialtech_ae.me.IAcceleratorThreadsProvider;

public class IAeCraftingBlockEntity extends CraftingBlockEntity implements IAcceleratorThreadsProvider {
    public IAeCraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Override
    public int getAcceleratorThreads() {
        return 0;
    }

    @Override
    public int getIAeCustomAcceleratorThreads() {
        return getUnitBlock().type.getAcceleratorThreads();
    }
}
