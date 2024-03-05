package shining.starj.HalfSurvival.Recipes;

import shining.starj.HalfSurvival.Core;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.recipe.CookingBookCategory;

public class CustomFurnaceRecipe extends CustomRecipe {
	private final RecipeChoice choice;
	private final float exp;
	private final int cookingtime;
	private final CookingBookCategory category;

	public CustomFurnaceRecipe(String key, ItemStack result, RecipeChoice choice, int cookingtime) {
		this(key, result, CookingBookCategory.MISC, choice, 0f, cookingtime);
	}

	public CustomFurnaceRecipe(String key, ItemStack result, RecipeChoice choice, float exp, int cookingtime) {
		this(key, result, CookingBookCategory.MISC, choice, exp, cookingtime);
	}

	public CustomFurnaceRecipe(String key, ItemStack result, RecipeChoice choice, CookingBookCategory category,
			int cookingtime) {
		this(key, result, category, choice, 0f, cookingtime);
	}

	public CustomFurnaceRecipe(String key, ItemStack result, CookingBookCategory category, RecipeChoice choice,
			float exp, int cookingtime) {
		super(key, result);
		this.category = category;
		this.choice = choice;
		this.exp = exp;
		this.cookingtime = cookingtime;
	}

	@Override
	public void regist() {
		FurnaceRecipe fr = new FurnaceRecipe(new NamespacedKey(Core.getCore(), this.key), this.result, choice, exp,
				cookingtime);
		fr.setCategory(this.category);
		Bukkit.addRecipe(fr);
	}

}
