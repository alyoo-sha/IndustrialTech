package org.mod.industrialtech_ae.item;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.items.storage.StorageCellTooltipComponent;
import org.mod.industrialtech_ae.client.renderer.GenericItemRenderer;
import org.mod.industrialtech_ae.util.AmountFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BulkItemChestItem extends BlockItem {
    public BulkItemChestItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    private long getAmount(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("storage") ? stack.getTag().getCompound("storage").getLong("amount") : 0L;
    }

    private AEItemKey getFilter(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("storage")) {
            CompoundTag storageTag = stack.getTag().getCompound("storage");
            if (storageTag.contains("filter")) {
                return AEItemKey.fromTag(storageTag.getCompound("filter"));
            }
        }

        return null;
    }

    private AEItemKey getItem(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("storage")) {
            CompoundTag storageTag = stack.getTag().getCompound("storage");
            if (storageTag.contains("item")) {
                return AEItemKey.fromTag(storageTag.getCompound("item"));
            }
        }

        return null;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        AEItemKey filter = this.getFilter(stack);
        long amount = this.getAmount(stack);
        AEItemKey stored = this.getItem(stack);
        if (filter == null) {
            tooltip.add(Component.translatable("gui.industrialtech_ae.no_filter").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.translatable("gui.industrialtech_ae.contains").withStyle(ChatFormatting.GRAY).append(": ").withStyle(ChatFormatting.GRAY).append(filter.getDisplayName().copy().withStyle(ChatFormatting.GREEN)));
        }

        if (stored == null && amount <= 0L) {
            tooltip.add(Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("gui.industrialtech_ae.amount").withStyle(ChatFormatting.GRAY).append(": ").append(AmountFormatter.formatItem(amount, true, ChatFormatting.AQUA)));
        }

    }

    public @NotNull Optional<TooltipComponent> m_142422_(ItemStack stack) {
        AEItemKey filter = this.getFilter(stack);
        long amount = this.getAmount(stack);
        AEItemKey stored = this.getItem(stack);
        List<GenericStack> content = new ArrayList();
        if (stored != null) {
            content.add(new GenericStack(stored, amount));
        } else if (filter != null) {
            content.add(new GenericStack(filter, 0L));
        }

        return Optional.of(new StorageCellTooltipComponent(List.of(), content, false, true));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new GenericItemRenderer();
            }
        });
    }
}
