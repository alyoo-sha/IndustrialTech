package org.mod.industrialtech_crafting.init;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_crafting.Industrialtech_crafting;
import org.mod.industrialtech_crafting.util.block.BaseBlockUtil;
import org.mod.industrialtech_crafting.util.item.BaseBlockItemUtil;

import java.util.function.Supplier;

public class ICraftModBlock {

    public static final DeferredRegister<Block> BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Industrialtech_crafting.MOD_ID);

    public static RegistryObject<Block> LUMINESSENCE_BLOCK = register(BLOCK, "luminessence_block",
            () -> new BaseBlockUtil(SoundType.STONE, 5.0F, 10.0F, true), ICraftModRarity.LEGENDARY.rarity);
    public static RegistryObject<Block> BLACK_IRON_BLOCK = register(BLOCK, "black_iron_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> REDSTONE_INGOT_BLOCK = register(BLOCK, "redstone_ingot_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true));
    public static RegistryObject<Block> ENHANCED_REDSTONE_INGOT_BLOCK = register(BLOCK, "enhanced_redstone_ingot_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> ENDER_INGOT_BLOCK = register(BLOCK, "ender_ingot_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true));
    public static RegistryObject<Block> ENHANCED_ENDER_INGOT_BLOCK = register(BLOCK, "enhanced_ender_ingot_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> CRYSTALTINE_BLOCK = register(BLOCK, "crystaltine_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> NETHER_STAR_BLOCK = register(BLOCK, "nether_star_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> FLUX_STAR_BLOCK = register(BLOCK, "flux_star_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);
    public static RegistryObject<Block> ENDER_STAR_BLOCK = register(BLOCK, "ender_star_block",
            () -> new BaseBlockUtil(SoundType.METAL, 5.0F, 10.0F, true), ICraftModRarity.UNCOMMON.rarity);


    //    public static final RegistryObject<Block> THE_ULTIMATE_BLOCK = register("the_ultimate_block", TheUltimateBlock::new, Rarity.EPIC);
    
//    public static final RegistryObject<Block> FRAME = register("frame", FrameBlock::new);
//    public static final RegistryObject<Block> PEDESTAL = register("pedestal", PedestalBlock::new);
//    public static final RegistryObject<Block> CRAFTING_CORE = register("crafting_core", CraftingCoreBlock::new);
//    public static final RegistryObject<Block> BASIC_TABLE = register("basic_table", BasicTableBlock::new);
//    public static final RegistryObject<Block> ADVANCED_TABLE = register("advanced_table", AdvancedTableBlock::new);
//    public static final RegistryObject<Block> ELITE_TABLE = register("elite_table", EliteTableBlock::new);
//    public static final RegistryObject<Block> ULTIMATE_TABLE = register("ultimate_table", UltimateTableBlock::new);
//    public static final RegistryObject<Block> BASIC_AUTO_TABLE = register("basic_auto_table", BasicAutoTableBlock::new);
//    public static final RegistryObject<Block> ADVANCED_AUTO_TABLE = register("advanced_auto_table", AdvancedAutoTableBlock::new);
//    public static final RegistryObject<Block> ELITE_AUTO_TABLE = register("elite_auto_table", EliteAutoTableBlock::new);
//    public static final RegistryObject<Block> ULTIMATE_AUTO_TABLE = register("ultimate_auto_table", UltimateAutoTableBlock::new);
//    public static final RegistryObject<Block> COMPRESSOR = register("compressor", CompressorBlock::new);
//    public static final RegistryObject<Block> ENDER_ALTERNATOR = register("ender_alternator", EnderAlternatorBlock::new);
//    public static final RegistryObject<Block> ENDER_CRAFTER = register("ender_crafter", EnderCrafterBlock::new);
//    public static final RegistryObject<Block> AUTO_ENDER_CRAFTER = register("auto_ender_crafter", AutoEnderCrafterBlock::new);
//    public static final RegistryObject<Block> FLUX_ALTERNATOR = register("flux_alternator", FluxAlternatorBlock::new);
//    public static final RegistryObject<Block> FLUX_CRAFTER = register("flux_crafter", FluxCrafterBlock::new);
//    public static final RegistryObject<Block> AUTO_FLUX_CRAFTER = register("auto_flux_crafter", AutoFluxCrafterBlock::new);


    /**
     * ══════════════════════════════════════════════════════════════════════
     * ════════════════════════ Базовые методы регистрации ═══════════════════════
     * ══════════════════════════════════════════════════════════════════════
     */
    public static <T extends Block> RegistryObject<T> register(
            DeferredRegister<Block> register,
            String name,
            Supplier<T> block,
            Rarity rarity
    ) {
        RegistryObject<T> registered = register.register(name, block);
        ICraftModItem.ITEM.register(name,
                () -> new BaseBlockItemUtil(registered.get(), p -> p.rarity(rarity)));
        return registered;
    }

    public static <T extends Block> RegistryObject<T> register(
            DeferredRegister<Block> register,
            String name,
            Supplier<T> block
    ) {
        RegistryObject<T> registered = register.register(name, block);
        ICraftModItem.ITEM.register(name,
                () -> new BaseBlockItemUtil(registered.get()));
        return registered;
    }

    public static <T extends Block> RegistryObject<T> registerBlockOnly(
            String name,
            Supplier<T> blockSupplier) {
        return BLOCK.register(name, blockSupplier);
    }
    /**
     * ══════════════════════════════════════════════════════════════════════
     * ═════════════════════════════ Простые блоки ════════════════════════════
     * ══════════════════════════════════════════════════════════════════════
     */

    public static RegistryObject<Block> simpleBlock(String name) {
        return simpleBlock(name, Blocks.STONE);
    }

    public static RegistryObject<Block> simpleBlock(String name, Block baseBlock) {
        return register(BLOCK, name, () -> new Block(BlockBehaviour.Properties.copy(baseBlock)));
    }

    public static RegistryObject<Block> simpleNoLoot(String name) {
        return simpleNoLoot(name, Blocks.STONE);
    }

    public static RegistryObject<Block> simpleNoLoot(String name, Block baseBlock) {
        return register(BLOCK, name,
                () -> new Block(BlockBehaviour.Properties.copy(baseBlock).noLootTable()));
    }


    public static void init() {
        BLOCK.register(Industrialtech_crafting.modEventBus);
    }
}
