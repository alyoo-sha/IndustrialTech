package org.mod.industrialtech.test;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;

public class modBlock {
    public static final DeferredRegister<Block> BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Industrialtech.MOD_ID);

//    public static RegistryObject<Block> LUMINESSENCE_BLOCK = register(BLOCK, "luminessence_block",
//            () -> new BaseBlockUtil(SoundType.STONE, 5.0F, 10.0F, true), ICraftModRarity.LEGENDARY.rarity);
}
