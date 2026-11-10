package org.mod.industrialtech.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.mod.industrialtech.core.init.IModRecipe;

public record MatterRecipe(ResourceLocation id, String machine, Ingredient input, int count,
                          boolean consume, ItemStack result, long energy) implements Recipe<Container> {

    /** Самый крупный рецепт этого генератора, который помещается в стак. */
    public static MatterRecipe find(Level level, String machine, ItemStack stack) {
        if (level == null || stack.isEmpty()) return null;
        MatterRecipe best = null;
        for (MatterRecipe r : level.getRecipeManager().getAllRecipesFor(IModRecipe.MATTER_TYPE.get())) {
            if (r.machine.equals(machine) && r.input.test(stack) && stack.getCount() >= r.count
                    && (best == null || r.count > best.count)) {
                best = r;
            }
        }
        return best;
    }

    @Override public boolean matches(Container c, Level l) { return false; }
    @Override public ItemStack assemble(Container c, RegistryAccess a) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override public ItemStack getResultItem(RegistryAccess a) { return result; }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return IModRecipe.MATTER_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return IModRecipe.MATTER_TYPE.get(); }

    public static class Serializer implements RecipeSerializer<MatterRecipe> {
        @Override
        public MatterRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new MatterRecipe(id,
                    GsonHelper.getAsString(json, "machine"),
                    Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input")),
                    GsonHelper.getAsInt(json, "count"),
                    GsonHelper.getAsBoolean(json, "consume", true),
                    ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")),
                    GsonHelper.getAsLong(json, "energy"));
        }

        @Override
        public MatterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new MatterRecipe(id, buf.readUtf(), Ingredient.fromNetwork(buf), buf.readVarInt(),
                    buf.readBoolean(), buf.readItem(), buf.readLong());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MatterRecipe r) {
            buf.writeUtf(r.machine);
            r.input.toNetwork(buf);
            buf.writeVarInt(r.count);
            buf.writeBoolean(r.consume);
            buf.writeItem(r.result);
            buf.writeLong(r.energy);
        }
    }
}