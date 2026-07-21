package org.mod.industrialtech_ae.item;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.items.AEBaseItem;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import org.mod.industrialtech_ae.ae2.AeKeyType;
import org.mod.industrialtech_ae.ae2.BaseCellInventory;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.util.AmountFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BulkItemCellItem extends AEBaseItem implements ICellWorkbenchItem {
    public static final org.mod.industrialtech_ae.item.BulkItemCellItem.Handler HANDLER = new org.mod.industrialtech_ae.item.BulkItemCellItem.Handler();

    public BulkItemCellItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEItemKey.filter(), is, 1);
    }

    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return FuzzyMode.IGNORE_ALL;
    }

    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        BaseCellInventory<AEItemKey> inv = HANDLER.getCellInventory(stack, (ISaveProvider)null);
        if (inv == null) {
            return Optional.empty();
        } else {
            List<GenericStack> content = new ArrayList();
            AEItemKey storedKey = (AEItemKey)inv.getStoredKey();
            AEItemKey filterKey = (AEItemKey)inv.getFilterKey();
            long amount = inv.getStoredAmount();
            if (storedKey != null && amount > 0L) {
                content.add(new GenericStack(storedKey, amount));
            } else if (filterKey != null) {
                content.add(new GenericStack(filterKey, 0L));
            }

            return Optional.of(new StorageCellTooltipComponent(List.of(), content, false, true));
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        BaseCellInventory<AEItemKey> inv = HANDLER.getCellInventory(stack, (ISaveProvider)null);
        if (inv != null) {
            AEItemKey filter = (AEItemKey)inv.getFilterKey();
            AEItemKey stored = (AEItemKey)inv.getStoredKey();
            long amount = inv.getStoredAmount();
            if (filter == null) {
                tooltip.add(Component.translatable("gui.industrialtech_ae.unpartitioned").withStyle(ChatFormatting.RED));
            } else {
                tooltip.add(Component.translatable("gui.industrialtech_ae.contains").withStyle(ChatFormatting.GRAY).append(": ").append(filter.getDisplayName().copy().withStyle(ChatFormatting.GREEN)));
            }

            if (stored != null && amount > 0L) {
                tooltip.add(Component.translatable("gui.industrialtech_ae.amount").withStyle(ChatFormatting.GRAY).append(": ").append(AmountFormatter.formatItem(amount, true, ChatFormatting.AQUA)));
            } else {
                tooltip.add(Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.GRAY));
            }

        }
    }

    public static class Handler implements ICellHandler {
        public Handler() {
        }

        public boolean isCell(ItemStack stack) {
            return stack.getItem() instanceof org.mod.industrialtech_ae.item.BulkItemCellItem;
        }

        public BaseCellInventory<AEItemKey> getCellInventory(ItemStack stack, ISaveProvider host) {
            if (!this.isCell(stack)) {
                return null;
            } else {
                GenericAeKeyStorage<AEItemKey> storage = new GenericAeKeyStorage(AeKeyType.ITEM, (AEKey)null, (Runnable)null);
                return new BaseCellInventory(stack, host, storage);
            }
        }
    }
}
