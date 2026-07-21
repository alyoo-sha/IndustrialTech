package org.mod.industrialtech_ae.item;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.items.AEBaseItem;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mod.industrialtech_ae.ae2.AeKeyType;
import org.mod.industrialtech_ae.ae2.BaseCellInventory;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.util.AmountFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BulkFluidCellItem extends AEBaseItem implements ICellWorkbenchItem {
    public static final org.mod.industrialtech_ae.item.BulkFluidCellItem.Handler HANDLER = new org.mod.industrialtech_ae.item.BulkFluidCellItem.Handler();

    public BulkFluidCellItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    public ConfigInventory getConfigInventory(ItemStack is) {
        return CellConfig.create(AEFluidKey.filter(), is, 1);
    }

    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return FuzzyMode.IGNORE_ALL;
    }

    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        BaseCellInventory<AEFluidKey> inv = HANDLER.getCellInventory(stack, (ISaveProvider)null);
        if (inv == null) {
            return Optional.empty();
        } else {
            List<GenericStack> content = new ArrayList();
            AEFluidKey storedKey = (AEFluidKey)inv.getStoredKey();
            AEFluidKey filterKey = (AEFluidKey)inv.getFilterKey();
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
        BaseCellInventory<AEFluidKey> inv = HANDLER.getCellInventory(stack, (ISaveProvider)null);
        if (inv != null) {
            AEFluidKey filter = (AEFluidKey)inv.getFilterKey();
            AEFluidKey stored = (AEFluidKey)inv.getStoredKey();
            long amount = inv.getStoredAmount();
            if (filter == null) {
                tooltip.add(Component.translatable("gui.industrialtech_ae.unpartitioned").withStyle(ChatFormatting.RED));
            } else {
                tooltip.add(Component.translatable("gui.industrialtech_ae.contains").withStyle(ChatFormatting.GRAY).append(": ").append(filter.getDisplayName().copy().withStyle(ChatFormatting.GOLD)));
            }

            if (stored != null && amount > 0L) {
                tooltip.add(Component.translatable("gui.industrialtech_ae.amount").withStyle(ChatFormatting.GRAY).append(": ").append(AmountFormatter.formatFluid(amount, true, ChatFormatting.LIGHT_PURPLE)));
            } else {
                tooltip.add(Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.GRAY));
            }

        }
    }

    public static class Handler implements ICellHandler {
        public Handler() {
        }

        public boolean isCell(ItemStack stack) {
            return stack.getItem() instanceof org.mod.industrialtech_ae.item.BulkFluidCellItem;
        }

        public BaseCellInventory<AEFluidKey> getCellInventory(ItemStack stack, ISaveProvider host) {
            if (!this.isCell(stack)) {
                return null;
            } else {
                GenericAeKeyStorage<AEFluidKey> storage = new GenericAeKeyStorage(AeKeyType.FLUID, (AEKey)null, (Runnable)null);
                return new BaseCellInventory(stack, host, storage);
            }
        }
    }
}
