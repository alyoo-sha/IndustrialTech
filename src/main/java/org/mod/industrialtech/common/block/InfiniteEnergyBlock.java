package org.mod.industrialtech.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.mod.industrialtech.common.entity.BlockEntityInfiniteEnergy;
import org.mod.industrialtech.core.init.IModBlockEntity;
import org.mod.industrialtech.core.init.MatterRegistry;

import java.util.List;

/** Креативный источник энергии. GUI нет: клик по блоку ничего не делает. */
public class InfiniteEnergyBlock extends BaseEntityBlock {

    public InfiniteEnergyBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(2.0F, 3_600_000.0F).sound(SoundType.METAL));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityInfiniteEnergy(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> given) {
        if (level.isClientSide || given != IModBlockEntity.INFINITE_ENERGY_BE.get()) {
            return null;
        }
        return (lvl, pos, st, be) -> {
            if (be instanceof BlockEntityInfiniteEnergy source) {
                source.serverTick();
            }
        };
    }

    /** Дроп без loot table: блок выпадает сам. */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of(new ItemStack(this));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof MenuProvider provider && player instanceof ServerPlayer sp) {
            NetworkHooks.openScreen(sp, provider, pos);
        }
        return InteractionResult.CONSUME;
    }
}