package org.mod.industrialtech_ae.block;

import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BulkFluidTankBlock extends BaseBlock<BulkFluidTankBlockEntity> {
    public BulkFluidTankBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BulkFluidTankBlockEntity(pos, state);
    }
}
