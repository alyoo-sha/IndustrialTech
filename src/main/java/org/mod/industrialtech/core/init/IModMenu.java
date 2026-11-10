package org.mod.industrialtech.core.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.container.InfiniteEnergyMenu;
import org.mod.industrialtech.common.container.MatterGeneratorMenu;
import org.mod.industrialtech.common.container.RecyclerMenu;

public final class IModMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Industrialtech.MOD_ID);
    public static void init() {
        MENUS.register(Industrialtech.modEventBus);
    }

    public static final RegistryObject<MenuType<MatterGeneratorMenu>> GENERATOR_MENU =
            MENUS.register("matter_generator", () -> IForgeMenuType.create(MatterGeneratorMenu::new));
    public static final RegistryObject<MenuType<RecyclerMenu>> RECYCLER_MENU =
            MENUS.register("recycler", () -> IForgeMenuType.create(RecyclerMenu::new));
    public static final RegistryObject<MenuType<InfiniteEnergyMenu>> INFINITE_ENERGY_MENU =
            MENUS.register("infinite_energy", () -> IForgeMenuType.create(InfiniteEnergyMenu::new));
}
