package com.alexvr.bedres.recipes.ritualAltar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RitualAltarRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RitualAltarRecipes> {

    @Override
    public RitualAltarRecipes fromJson(ResourceLocation recipeId, JsonObject json) {
        ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(json, "result"));
        ItemStack output = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(resourcelocation)).orElseThrow(() -> new IllegalStateException("Item: " + GsonHelper.getAsString(json, "result") + " does not exist")));
        int outputAmount = GsonHelper.getAsInt(json, "count", 1);
        output.setCount(outputAmount);

        JsonElement input = json.get("input");
        List<ItemStack> inputItems = new ArrayList<>();
        if (input.isJsonArray()) {
            for (JsonElement element : input.getAsJsonArray()) {
                JsonObject object = element.getAsJsonObject();
                ItemStack item = ItemStack.EMPTY;
                if (object.has("item")) {
                    String inputStr = object.get("item").getAsString();
                    ResourceLocation in = new ResourceLocation(inputStr);
                    item = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(in)).orElseThrow(() -> new IllegalStateException("Item: " + inputStr + " does not exist")));
                }
                if (object.has("count")) {
                    int count = object.get("count").getAsInt();
                    item.setCount(count);
                }
                inputItems.add(item);
            }
        } else {
            throw new IllegalStateException("Recipe " + recipeId.toString() + ": input is in wrong format or missing!");
        }

        return new RitualAltarRecipes(output, inputItems.toArray(new ItemStack[inputItems.size()]));
    }

    @Nullable
    @Override
    public RitualAltarRecipes fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ResourceLocation resourcelocation = new ResourceLocation(buffer.readUtf(32767));
        ItemStack output = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(resourcelocation)).orElseThrow(() -> new IllegalStateException("Item: " + resourcelocation.toString() + " does not exist")));
        int count = buffer.readInt();
        output.setCount(count);

        List<ItemStack> inputItems = new ArrayList<>();
        int size = buffer.readInt();
        for (int i = 0 ; i < size ; i++) {
            ResourceLocation rl = new ResourceLocation(buffer.readUtf(32767));
            ItemStack input = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(rl)).orElseThrow(() -> new IllegalStateException("Item: " + rl.toString() + " does not exist")));
            int inputCount = buffer.readInt();
            input.setCount(inputCount);
            inputItems.add(input);
        }
        return new RitualAltarRecipes(output, inputItems.toArray(new ItemStack[inputItems.size()]));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RitualAltarRecipes recipe) {
        buffer.writeUtf(recipe.getResultItem().getItem().getRegistryName().toString());
        buffer.writeInt(recipe.getResultItem().getCount());
        List<ItemStack> list = recipe.getIngredientList();
        buffer.writeInt(list.size());
        for (ItemStack stack : list) {
            buffer.writeUtf(stack.getItem().getRegistryName().toString());
            buffer.writeInt(stack.getCount());
        }
    }
}
