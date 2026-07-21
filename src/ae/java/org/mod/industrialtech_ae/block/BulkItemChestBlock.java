package org.mod.industrialtech_ae.block;

import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BulkItemChestBlock extends BaseBlock<BulkItemChestBlockEntity> {
    public BulkItemChestBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BulkItemChestBlockEntity(pos, state);
    }
}