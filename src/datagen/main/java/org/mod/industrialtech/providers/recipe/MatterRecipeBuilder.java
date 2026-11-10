package org.mod.industrialtech.providers.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.core.init.IModRecipe;

import java.util.function.Consumer;

public final class MatterRecipeBuilder {
    private MatterRecipeBuilder() {}

    public static void save(Consumer<FinishedRecipe> out, String machine, String input, int count,
                            boolean consume, String result, int resultCount, long energy) {
        ResourceLocation id = new ResourceLocation(Industrialtech.MOD_ID,
                "matter/" + machine + "/" + input + "_x" + count);

        out.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("machine", machine);
                JsonObject in = new JsonObject();
                in.addProperty("item", Industrialtech.MOD_ID + ":" + input);
                json.add("input", in);
                json.addProperty("count", count);
                json.addProperty("consume", consume);
                JsonObject res = new JsonObject();
                res.addProperty("item", Industrialtech.MOD_ID + ":" + result);
                res.addProperty("count", resultCount);
                json.add("result", res);
                json.addProperty("energy", energy);
            }

            @Override public ResourceLocation getId() { return id; }
            @Override public RecipeSerializer<?> getType() { return IModRecipe.MATTER_SERIALIZER.get(); }
            @Override public JsonObject serializeAdvancement() { return null; }
            @Override public ResourceLocation getAdvancementId() { return null; }
        });
    }
}