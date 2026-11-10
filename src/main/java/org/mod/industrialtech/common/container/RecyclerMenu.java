package org.mod.industrialtech.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.mod.industrialtech.common.entity.RecyclerBlockEntity;
import org.mod.industrialtech.core.init.IModMenu;

public class RecyclerMenu extends MachineMenu {

    public RecyclerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos(), new ItemStackHandler(2), new SimpleContainerData(3));
    }

    public RecyclerMenu(int id, Inventory inv, RecyclerBlockEntity be) {
        this(id, inv, be.getBlockPos(), be.items(), be.data);
    }

    private RecyclerMenu(int id, Inventory inv, BlockPos pos, IItemHandler handler, ContainerData data) {
        super(IModMenu.RECYCLER_MENU.get(), id, inv, pos, handler, data);
    }

    public int progress() {
        return data.get(0);
    }

    @Override
    public long energy() {
        return (data.get(1) & 0xFFFFL) | ((long) (data.get(2) & 0xFFFF) << 16);
    }
}