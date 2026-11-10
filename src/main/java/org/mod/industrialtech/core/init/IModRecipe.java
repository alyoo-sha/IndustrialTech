package org.mod.industrialtech.core.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.recipe.MatterRecipe;

public class IModRecipe {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Industrialtech.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Industrialtech.MOD_ID);

    public static final RegistryObject<RecipeType<MatterRecipe>> MATTER_TYPE =
            RECIPE_TYPES.register("matter_generation", () -> new RecipeType<MatterRecipe>() {
                @Override
                public String toString() {
                    return Industrialtech.MOD_ID + ":matter_generation";
                }
            });

    public static final RegistryObject<RecipeSerializer<MatterRecipe>> MATTER_SERIALIZER =
            RECIPE_SERIALIZERS.register("matter_generation", MatterRecipe.Serializer::new);

    public static void init() {
        RECIPE_TYPES.register(Industrialtech.modEventBus);
        RECIPE_SERIALIZERS.register(Industrialtech.modEventBus);
    }
}
