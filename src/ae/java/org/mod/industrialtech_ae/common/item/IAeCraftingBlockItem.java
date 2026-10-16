package org.mod.industrialtech_ae.common.item;

import appeng.block.crafting.CraftingBlockItem;
import appeng.core.definitions.AEParts;
import appeng.util.InteractionUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.mod.industrialtech_ae.common.block.IAeCraftingMonitorBlock;
import org.mod.industrialtech_ae.common.block.IAeICraftingBlock;
import org.mod.industrialtech_ae.core.init.IAeModItem;
import org.mod.industrialtech_ae.me.IAeCraftingFamily;
import org.mod.industrialtech_ae.me.IAeCraftingStorageType;

import java.util.function.Supplier;

public class IAeCraftingBlockItem extends CraftingBlockItem {
    public final IAeCraftingFamily family;

    public IAeCraftingBlockItem(Block id, Properties props, IAeCraftingFamily family) {
        super(id, props, getDisassemblyExtraItem(id));
        this.family = family;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(InteractionUtil.isInAlternateUseMode(player)) {
            // 对空单元不执行操作
            if (getBlock() instanceof IAeICraftingBlock IAeCraftingBlock && IAeCraftingBlock.getIAeCraftingUnitType().storageType == IAeCraftingStorageType.UNIT)
                return InteractionResultHolder.pass(player.getItemInHand(hand));

            ItemStack stack = player.getItemInHand(hand);

            ItemStack removedUpgrade;
            if (getBlock() instanceof IAeCraftingMonitorBlock)
            {
                removedUpgrade = AEParts.STORAGE_MONITOR.stack();
            }
            else
            {
                removedUpgrade = new ItemStack(getDisassemblyExtraItem(getBlock()).get(), 1);
            }
            if (removedUpgrade.isEmpty()) return super.use(level, player, hand);

            int itemCount = stack.getCount();
            player.setItemInHand(hand, ItemStack.EMPTY);

            Inventory inv = player.getInventory();
            inv.placeItemBackInInventory(removedUpgrade.copyWithCount(removedUpgrade.getCount() * itemCount));

            ItemLike unitBlock = this.family.getUnitBaseBlock();
            inv.placeItemBackInInventory(new ItemStack(unitBlock, itemCount));

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    public static Supplier<ItemLike> getDisassemblyExtraItem(Block disassemblyBlock) {
        if (disassemblyBlock instanceof IAeICraftingBlock aeICraftingBlock) {
            return () -> switch (aeICraftingBlock.getIAeCraftingUnitType()) {
                case IAE_COMMON_MONITOR -> AEParts.STORAGE_MONITOR;
                case IAE_COMMON_STORAGE_1K -> IAeModItem.COMMON_CELL_COMPONENT_1K.get();
                case IAE_COMMON_STORAGE_4K -> IAeModItem.COMMON_CELL_COMPONENT_4K.get();
                case IAE_COMMON_STORAGE_16K -> IAeModItem.COMMON_CELL_COMPONENT_16K.get();
                case IAE_COMMON_STORAGE_64K -> IAeModItem.COMMON_CELL_COMPONENT_64K.get();
                case IAE_COMMON_STORAGE_256K -> IAeModItem.COMMON_CELL_COMPONENT_256K.get();
                case IAE_COMMON_STORAGE_1M -> IAeModItem.COMMON_CELL_COMPONENT_1M.get();
                case IAE_COMMON_STORAGE_4M -> IAeModItem.COMMON_CELL_COMPONENT_4M.get();
                case IAE_COMMON_STORAGE_16M -> IAeModItem.COMMON_CELL_COMPONENT_16M.get();
                case IAE_COMMON_STORAGE_64M -> IAeModItem.COMMON_CELL_COMPONENT_64M.get();
                case IAE_COMMON_STORAGE_256M -> IAeModItem.COMMON_CELL_COMPONENT_256M.get();

                default -> Items.AIR;
            };
        }
        return() -> Items.AIR;
    }
}
