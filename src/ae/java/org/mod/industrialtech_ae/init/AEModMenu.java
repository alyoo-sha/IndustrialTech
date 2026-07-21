package org.mod.industrialtech_ae.init;

import org.mod.industrialtech_ae.menu.BulkFluidTankMenu;
import org.mod.industrialtech_ae.menu.BulkItemChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;

public class AEModMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Industrialtech_ae.MOD_ID);
    public static void init() {
        MENUS.register(Industrialtech_ae.modEventBus);
    }

    public static final RegistryObject<MenuType<BulkFluidTankMenu>> BULK_FLUID_TANK_MENU = MENUS.register("bulk_fluid_tank", () -> IForgeMenuType.create(BulkFluidTankMenu::new));
    public static final RegistryObject<MenuType<BulkItemChestMenu>> BULK_ITEM_CHEST_MENU = MENUS.register("bulk_item_chest", () -> IForgeMenuType.create(BulkItemChestMenu::new));
}
