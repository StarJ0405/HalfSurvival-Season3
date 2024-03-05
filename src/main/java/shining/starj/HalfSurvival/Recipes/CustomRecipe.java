package shining.starj.HalfSurvival.Recipes;

import shining.starj.HalfSurvival.Items.Items;
import shining.starj.HalfSurvival.Recipes.CustomShapedRecipe.Shape;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CustomRecipe {
	private final static List<CustomRecipe> list = new ArrayList<CustomRecipe>();
	// 기타
	public static final CustomFurnaceRecipe time = new CustomFurnaceRecipe("time", Items.time.getItemStack(),
			fromItemStack(Items.time.getItemStack()), 20);
	// 낚싯대
	public static final CustomShapedRecipe wooden_fishing_rod = new CustomShapedRecipe("wooden_fishing_rod",
			Items.wooden_fishing_rod.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { "  a", " ab", "a b" }, new Shape('a', fromMaterial(Material.STICK)),
			new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe stone_fishing_rod = new CustomShapedRecipe("stone_fishing_rod",
			Items.stone_fishing_rod.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { "  a", " ab", "a b" }, new Shape('a', fromMaterial(Material.COBBLESTONE)),
			new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe iron_fishing_rod = new CustomShapedRecipe("iron_fishing_rod",
			Items.iron_fishing_rod.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "  a", " ab", "a b" },
			new Shape('a', fromMaterial(Material.IRON_INGOT)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe gold_fishing_rod = new CustomShapedRecipe("gold_fishing_rod",
			Items.golden_fishing_rod.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { "  a", " ab", "a b" }, new Shape('a', fromMaterial(Material.GOLD_INGOT)),
			new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe diamond_fishing_rod = new CustomShapedRecipe("diamond_fishing_rod",
			Items.diamond_fishing_rod.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { "  a", " ab", "a b" }, new Shape('a', fromMaterial(Material.DIAMOND)),
			new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomSmithingTransformRecipe netherite_fishing_rod = new CustomSmithingTransformRecipe(
			"netherite_fishing_rod", Items.netherite_fishing_rod.getItemStack(),
			fromMaterial(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
			fromItemStack(Items.diamond_fishing_rod.getItemStack()), fromMaterial(Material.NETHERITE_INGOT));
	// 장검
	public static final CustomShapedRecipe wooden_long_sword = new CustomShapedRecipe("wooden_long_sword",
			Items.wooden_long_sword.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { " b ", " b ", " a " }, new Shape('a', fromMaterial(Material.STICK)),
			new Shape('b', fromMaterial(Material.ACACIA_PLANKS, Material.BAMBOO_PLANKS, Material.BIRCH_PLANKS,
					Material.CHERRY_PLANKS, Material.CRIMSON_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS,
					Material.MANGROVE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.WARPED_PLANKS)));
	public static final CustomShapedRecipe stone_long_sword = new CustomShapedRecipe("stone_long_sword",
			Items.stone_long_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " b ", " b ", " a " },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.COBBLESTONE)));
	public static final CustomShapedRecipe iron_long_sword = new CustomShapedRecipe("iron_long_sword",
			Items.iron_long_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " b ", " b ", " a " },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.IRON_INGOT)));
	public static final CustomShapedRecipe gold_long_sword = new CustomShapedRecipe("gold_long_sword",
			Items.golden_long_sword.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { " b ", " b ", " a " }, new Shape('a', fromMaterial(Material.STICK)),
			new Shape('b', fromMaterial(Material.GOLD_INGOT)));
	public static final CustomShapedRecipe diamond_long_sword = new CustomShapedRecipe("diamond_long_sword",
			Items.diamond_long_sword.getItemStack(), CraftingBookCategory.EQUIPMENT,
			new String[] { " b ", " b ", " a " }, new Shape('a', fromMaterial(Material.STICK)),
			new Shape('b', fromMaterial(Material.DIAMOND)));
	public static final CustomSmithingTransformRecipe netherite_long_sword = new CustomSmithingTransformRecipe(
			"netherite_long_sword", Items.netherite_long_sword.getItemStack(),
			fromMaterial(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
			fromItemStack(Items.diamond_long_sword.getItemStack()), fromMaterial(Material.NETHERITE_INGOT));
	// 단검
	public static final CustomShapedRecipe wooden_short_sword = new CustomShapedRecipe("wooden_short_sword",
			Items.wooden_short_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " b ", " a " },
			new Shape('a', fromMaterial(Material.STICK)),
			new Shape('b', fromMaterial(Material.ACACIA_PLANKS, Material.BAMBOO_PLANKS, Material.BIRCH_PLANKS,
					Material.CHERRY_PLANKS, Material.CRIMSON_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS,
					Material.MANGROVE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.WARPED_PLANKS)));
	public static final CustomShapedRecipe stone_short_sword = new CustomShapedRecipe("stone_short_sword",
			Items.stone_short_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "b", "a" },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.COBBLESTONE)));
	public static final CustomShapedRecipe iron_short_sword = new CustomShapedRecipe("iron_short_sword",
			Items.iron_short_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "b", "a" },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.IRON_INGOT)));
	public static final CustomShapedRecipe gold_short_sword = new CustomShapedRecipe("gold_short_sword",
			Items.golden_short_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "b", "a" },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.GOLD_INGOT)));
	public static final CustomShapedRecipe diamond_short_sword = new CustomShapedRecipe("diamond_short_sword",
			Items.diamond_short_sword.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "b", "a" },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.DIAMOND)));
	public static final CustomSmithingTransformRecipe netherite_short_sword = new CustomSmithingTransformRecipe(
			"netherite_short_sword", Items.netherite_short_sword.getItemStack(),
			fromMaterial(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
			fromItemStack(Items.diamond_short_sword.getItemStack()), fromMaterial(Material.NETHERITE_INGOT));
	// 활
	public static final CustomShapedRecipe wooden_bow = new CustomShapedRecipe("wooden_bow",
			Items.wooden_bow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " ab", "a b", " ab" },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe stone_bow = new CustomShapedRecipe("stone_bow",
			Items.stone_bow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " ab", "a b", " ab" },
			new Shape('a', fromMaterial(Material.COBBLESTONE)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe iron_bow = new CustomShapedRecipe("iron_bow", Items.iron_bow.getItemStack(),
			CraftingBookCategory.EQUIPMENT, new String[] { " ab", "a b", " ab" },
			new Shape('a', fromMaterial(Material.IRON_INGOT)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe gold_bow = new CustomShapedRecipe("gold_bow",
			Items.golden_bow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " ab", "a b", " ab" },
			new Shape('a', fromMaterial(Material.GOLD_INGOT)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe diamond_bow = new CustomShapedRecipe("diamond_bow",
			Items.diamond_bow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { " ab", "a b", " ab" },
			new Shape('a', fromMaterial(Material.DIAMOND)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomSmithingTransformRecipe netherite_bow = new CustomSmithingTransformRecipe("netherite_bow",
			Items.netherite_bow.getItemStack(), fromMaterial(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
			fromItemStack(Items.diamond_bow.getItemStack()), fromMaterial(Material.NETHERITE_INGOT));
	// 석궁
	public static final CustomShapedRecipe wooden_crossbow = new CustomShapedRecipe("wooden_crossbow",
			Items.wooden_crossbow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "aaa", "bab", " a " },
			new Shape('a', fromMaterial(Material.STICK)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe stone_crossbow = new CustomShapedRecipe("stone_crossbow",
			Items.stone_crossbow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "aaa", "bab", " a " },
			new Shape('a', fromMaterial(Material.COBBLESTONE)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe iron_crossbow = new CustomShapedRecipe("iron_crossbow",
			Items.iron_crossbow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "aaa", "bab", " a " },
			new Shape('a', fromMaterial(Material.IRON_INGOT)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe gold_crossbow = new CustomShapedRecipe("gold_crossbow",
			Items.golden_crossbow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "aaa", "bab", " a " },
			new Shape('a', fromMaterial(Material.GOLD_INGOT)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomShapedRecipe diamond_crossbow = new CustomShapedRecipe("diamond_crossbow",
			Items.diamond_crossbow.getItemStack(), CraftingBookCategory.EQUIPMENT, new String[] { "aaa", "bab", " a " },
			new Shape('a', fromMaterial(Material.DIAMOND)), new Shape('b', fromMaterial(Material.STRING)));
	public static final CustomSmithingTransformRecipe netherite_crossbow = new CustomSmithingTransformRecipe(
			"netherite_crossbow", Items.netherite_crossbow.getItemStack(),
			fromMaterial(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
			fromItemStack(Items.diamond_crossbow.getItemStack()), fromMaterial(Material.NETHERITE_INGOT));
	// 코카콜라
	public static final CustomShapelessRecipe coca_cola = new CustomShapelessRecipe("coca_cola",
			Items.coca_cola.getItemStack(), fromMaterial(Material.COD), fromMaterial(Material.COD),
			fromMaterial(Material.COD), fromMaterial(Material.COD));
	//
	protected final String key;
	protected final ItemStack result;
	protected final CraftingBookCategory category;

	public CustomRecipe(String key, ItemStack result) {
		this(key, result, CraftingBookCategory.MISC);
	}

	public CustomRecipe(String key, ItemStack result, CraftingBookCategory category) {
		this.key = key;
		this.result = result;
		this.category = category;
		//
		list.add(this);
	}

	public abstract void regist();

	public static List<CustomRecipe> values() {
		return list;
	}

	public static RecipeChoice fromItemStack(ItemStack... items) {
		return new RecipeChoice.ExactChoice(items);
	}

	public static RecipeChoice fromMaterial(Material... types) {
		return new RecipeChoice.MaterialChoice(types);
	}

	public static void removeVanilla() {
		Iterator<Recipe> itr = Bukkit.recipeIterator();
		while (itr.hasNext())
			switch (itr.next().getResult().getType()) {
			// 금지 블럭
			case ANVIL:
			case ENCHANTING_TABLE:
				// 갑옷
			case CHAINMAIL_HELMET:
			case DIAMOND_HELMET:
			case GOLDEN_HELMET:
			case IRON_HELMET:
			case LEATHER_HELMET:
			case NETHERITE_HELMET:
			case TURTLE_HELMET:
			case CHAINMAIL_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
			case GOLDEN_CHESTPLATE:
			case IRON_CHESTPLATE:
			case LEATHER_CHESTPLATE:
			case NETHERITE_CHESTPLATE:
			case CHAINMAIL_LEGGINGS:
			case DIAMOND_LEGGINGS:
			case GOLDEN_LEGGINGS:
			case IRON_LEGGINGS:
			case LEATHER_LEGGINGS:
			case NETHERITE_LEGGINGS:
			case CHAINMAIL_BOOTS:
			case DIAMOND_BOOTS:
			case GOLDEN_BOOTS:
			case IRON_BOOTS:
			case LEATHER_BOOTS:
			case NETHERITE_BOOTS:
				// 도구
			case FISHING_ROD:
			case WOODEN_SWORD:
			case STONE_SWORD:
			case IRON_SWORD:
			case GOLDEN_SWORD:
			case DIAMOND_SWORD:
			case NETHERITE_SWORD:
			case BOW:
			case CROSSBOW:
				// 셜커
			case SHULKER_BOX:
			case BLACK_SHULKER_BOX:
			case BLUE_SHULKER_BOX:
			case BROWN_SHULKER_BOX:
			case CYAN_SHULKER_BOX:
			case GRAY_SHULKER_BOX:
			case GREEN_SHULKER_BOX:
			case YELLOW_SHULKER_BOX:
			case WHITE_SHULKER_BOX:
			case LIGHT_BLUE_SHULKER_BOX:
			case LIGHT_GRAY_SHULKER_BOX:
			case LIME_SHULKER_BOX:
			case MAGENTA_SHULKER_BOX:
			case ORANGE_SHULKER_BOX:
			case PINK_SHULKER_BOX:
			case PURPLE_SHULKER_BOX:
			case RED_SHULKER_BOX:
				// 포션
			case BREWING_STAND:
			case POTION:
			case SPLASH_POTION:
			case LINGERING_POTION:
				// 요리
			case COOKED_BEEF:
			case COOKED_CHICKEN:
			case COOKED_COD:
			case COOKED_MUTTON:
			case COOKED_PORKCHOP:
			case COOKED_RABBIT:
			case COOKED_SALMON:
				itr.remove();
			default:
				break;
			}
	}
}
