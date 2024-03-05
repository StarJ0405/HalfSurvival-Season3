package shining.starj.HalfSurvival.Recipes;

import shining.starj.HalfSurvival.Core;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;

public class CustomSmithingTransformRecipe extends CustomRecipe {
	private final RecipeChoice template;
	private final RecipeChoice base;
	private final RecipeChoice addition;

	public CustomSmithingTransformRecipe(String key, ItemStack result, RecipeChoice template, RecipeChoice base,
			RecipeChoice addition) {
		super(key, result);
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	@Override
	public void regist() {
		SmithingTransformRecipe str = new SmithingTransformRecipe(new NamespacedKey(Core.getCore(), this.key),
				this.result, template, base, addition);
		Bukkit.addRecipe(str);
	}
}
