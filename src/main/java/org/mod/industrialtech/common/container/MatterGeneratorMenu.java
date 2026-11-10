package org.mod.industrialtech.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.mod.industrialtech.common.block.MachineBlock;
import org.mod.industrialtech.common.entity.MatterGeneratorBlockEntity;
import org.mod.industrialtech.common.matter.MatterTier;
import org.mod.industrialtech.core.init.IModMenu;
import org.mod.industrialtech.core.init.MatterRegistry;

/** Одно меню для всех 10 генераторов. */
public class MatterGeneratorMenu extends MachineMenu {
    private final MatterTier tier;

    /** Клиент: данные приходят пакетами, слоты синхронизирует ванильный механизм. */
    public MatterGeneratorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos(), new ItemStackHandler(2), new SimpleContainerData(4));
    }

    /** Сервер. */
    public MatterGeneratorMenu(int id, Inventory inv, MatterGeneratorBlockEntity be) {
        this(id, inv, be.getBlockPos(), be.items(), be.data);
    }

    private MatterGeneratorMenu(int id, Inventory inv, BlockPos pos, IItemHandler handler, ContainerData data) {
        super(IModMenu.GENERATOR_MENU.get(), id, inv, pos, handler, data);
        MatterTier resolved = MatterTier.BASIC;
        if (inv.player.level().getBlockState(pos).getBlock() instanceof MachineBlock b && b.tier != null) {
            resolved = b.tier;
        }
        this.tier = resolved;
    }

    public MatterTier tier() {
        return tier;
    }

    @Override
    public long energy() {
        long value = 0;
        for (int i = 0; i < 4; i++) {
            value |= ((long) (data.get(i) & 0xFFFF)) << (16 * i);
        }
        return value;
    }
}