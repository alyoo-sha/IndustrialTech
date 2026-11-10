package org.mod.industrialtech.providers.recipe;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.matter.MatterTier;

import java.util.function.Consumer;

public final class MatterRecipes extends RecipeProvider {
    /** 40 млн FE за предмет: 16 шт = 640 млн, 32 шт = 1,28 млрд, 64 шт = 2,56 млрд. */
    private static final long ENERGY_PER_ITEM = 40_000_000L;
    private static final int[] BATCHES = {16, 32, 64};

    public MatterRecipes(PackOutput out) {
        super(out);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        // Верстак: обычная материя из утильсырья
        Item scrap = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Industrialtech.MOD_ID, "scrap"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                        ForgeRegistries.ITEMS.getValue(new ResourceLocation(Industrialtech.MOD_ID, MatterTier.BASIC.outputId)))
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', scrap)
                .unlockedBy("has_scrap", has(scrap))
                .save(writer);

        // Генераторы: пачки 16 / 32 / 64
        for (MatterTier tier : MatterTier.values()) {
            for (int n : BATCHES) {
                MatterRecipeBuilder.save(writer, tier.blockId, tier.inputId, n, true,
                        tier.outputId, n, ENERGY_PER_ITEM * n);
            }
        }

        // Шаблон не расходуется и сразу даёт стак
        MatterRecipeBuilder.save(writer, MatterTier.BASIC.blockId, MatterTier.TEMPLATE_ID, 1, false,
                MatterTier.BASIC.outputId, 64, ENERGY_PER_ITEM * 64);
    }
}
