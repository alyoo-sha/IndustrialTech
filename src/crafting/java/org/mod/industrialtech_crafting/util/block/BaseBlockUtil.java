package org.mod.industrialtech_crafting.util.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Function;

public class BaseBlockUtil extends Block {
    public BaseBlockUtil(Function<Properties, Properties> properties) {
        super((Properties)properties.apply(Properties.of()));
    }

    public BaseBlockUtil(SoundType sound, float hardness, float resistance) {
        super(Properties.of().sound(sound).strength(hardness, resistance));
    }

    public BaseBlockUtil(SoundType sound, float hardness, float resistance, boolean tool) {
        super(Properties.of().sound(sound).strength(hardness, resistance).requiresCorrectToolForDrops());
    }
}
