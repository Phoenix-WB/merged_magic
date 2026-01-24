package com.pianistwb.merged_magic.recipe;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class NoRemainderShapelessRecipe extends ShapelessRecipe {
	public NoRemainderShapelessRecipe(String group, CraftingBookCategory category, ItemStack result,
			List<Ingredient> ingredients) {
		super(group, category, result, ingredients);
	}
}
