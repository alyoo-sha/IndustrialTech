package org.mod.industrialtech_ae.client;

import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import org.mod.industrialtech_ae.ae2.BaseCellInventory;
import org.mod.industrialtech_ae.client.gui.BulkFluidTankScreen;
import org.mod.industrialtech_ae.client.gui.BulkItemChestScreen;
import org.mod.industrialtech_ae.client.renderer.GenericBlockRenderer;
import org.mod.industrialtech_ae.init.AEModItem;
import org.mod.industrialtech_ae.init.AEModMenu;
import org.mod.industrialtech_ae.item.BulkFluidCellItem;
import org.mod.industrialtech_ae.item.BulkItemCellItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.init.AEModBlockEntity;

@Mod.EventBusSubscriber(modid = Industrialtech_ae.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Ae2BulkCellsClient {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AEModBlockEntity.BULK_FLUID_TANK_BE.get(), ctx -> new GenericBlockRenderer(ctx));
        event.registerBlockEntityRenderer(AEModBlockEntity.BULK_ITEM_CHEST_BE.get(), ctx -> new GenericBlockRenderer(ctx));
    }

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) {
                return 16777215;
            } else {
                BaseCellInventory<AEFluidKey> inv = BulkFluidCellItem.HANDLER.getCellInventory(stack, (ISaveProvider)null);
                return getCellColor(inv);
            }
        }, new ItemLike[]{(ItemLike) AEModItem.BULK_FLUID_CELL.get()});
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) {
                return 16777215;
            } else {
                BaseCellInventory<AEItemKey> inv = BulkItemCellItem.HANDLER.getCellInventory(stack, (ISaveProvider)null);
                return getCellColor(inv);
            }
        }, new ItemLike[]{(ItemLike)AEModItem.BULK_ITEM_CELL.get()});
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCellModels.registerModel((ItemLike)AEModItem.BULK_FLUID_CELL.get(), Industrialtech_ae.makeId("block/bulk_fluid_cell"));
            StorageCellModels.registerModel((ItemLike)AEModItem.BULK_ITEM_CELL.get(), Industrialtech_ae.makeId("block/bulk_item_cell"));
            MenuScreens.register((MenuType) AEModMenu.BULK_FLUID_TANK_MENU.get(), BulkFluidTankScreen::new);
            MenuScreens.register((MenuType)AEModMenu.BULK_ITEM_CHEST_MENU.get(), BulkItemChestScreen::new);
        });
    }

    private static int getCellColor(BaseCellInventory<?> inv) {
        if (inv == null) {
            return 0;
        } else {
            CellState state = inv.getStatus();
            int var10000;
            switch (state) {
                case EMPTY -> var10000 = 65280;
                case NOT_EMPTY -> var10000 = 43775;
                case FULL -> var10000 = 16711680;
                default -> var10000 = 0;
            }

            return var10000;
        }
    }
}
