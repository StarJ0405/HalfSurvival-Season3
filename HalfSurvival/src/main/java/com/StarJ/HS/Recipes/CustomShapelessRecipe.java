package com.StarJ.HS.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import com.StarJ.HS.Core;

public class CustomShapelessRecipe extends CustomRecipe {

	private final RecipeChoice[] choices;

	public CustomShapelessRecipe(String key, ItemStack result, RecipeChoice... choices) {
		this(key, result, CraftingBookCategory.MISC, choices);
	}

	public CustomShapelessRecipe(String key, ItemStack result, CraftingBookCategory category, RecipeChoice... choices) {
		super(key, result, category);
		this.choices = choices;
	}

	@Override
	public void regist() {
		ShapelessRecipe sr = new ShapelessRecipe(new NamespacedKey(Core.getCore(), this.key), this.result);
		for (RecipeChoice choice : this.choices)
			sr.addIngredient(choice);
		sr.setCategory(this.category);
		Bukkit.addRecipe(sr);
	}
}
