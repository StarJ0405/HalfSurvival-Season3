package shining.starj.HalfSurvival.Recipes;

import shining.starj.HalfSurvival.Core;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

public class CustomShapedRecipe extends CustomRecipe {
	private final String[] shape;
	private final Shape[] shapes;

	public CustomShapedRecipe(String key, ItemStack result, String[] shape, Shape... shapes) {
		this(key, result, CraftingBookCategory.MISC, shape, shapes);
	}

	public CustomShapedRecipe(String key, ItemStack result, CraftingBookCategory category, String[] shape,
			Shape... shapes) {
		super(key, result, category);
		this.shape = shape;
		this.shapes = shapes;
	}

	@Override
	public void regist() {
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Core.getCore(), this.key), this.result);
		sr.shape(shape);
		for (Shape shape : shapes)
			sr.setIngredient(shape.c, shape.choice);
		sr.setCategory(this.category);
		Bukkit.addRecipe(sr);
	}

	public static class Shape {
		private final char c;
		private final RecipeChoice choice;

		public Shape(char c, RecipeChoice choice) {
			this.c = c;
			this.choice = choice;
		}
	}
}
