package shining.starj.HalfSurvival.Items;

import shining.starj.HalfSurvival.Items.UsableItem.Pocket;
import shining.starj.HalfSurvival.Items.UsableItem.Pocket.pocketType;
import shining.starj.HalfSurvival.Items.UsableItem.Pocket.size;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Items {
	private final static List<Items> list = new ArrayList<Items>();
	// 기타
	public static final Items time = new Items("time", ChatColor.WHITE + "시간", Material.CLOCK,
			new String[] { ChatColor.WHITE + "시간을 흐르게하는 용이니 괜히 건들지말고 나두자." }, false);
	// 낚시대
	public static final DurableItems wooden_fishing_rod = new DurableItems("wooden_fishing_rod",
			ChatColor.WHITE + "나무 낚싯대", Material.FISHING_ROD, new String[] { ChatColor.WHITE + "조잡하게 만들어진 나무 낚싯대" },
			64);
	public static final DurableItems stone_fishing_rod = new DurableItems("stone_fishing_rod",
			ChatColor.WHITE + "돌 낚싯대", Material.FISHING_ROD, new String[] { ChatColor.WHITE + "꽤 잘 만들어진 돌 낚싯대" },
			1111011, 64 * 2);
	public static final DurableItems iron_fishing_rod = new DurableItems("iron_fishing_rod", ChatColor.WHITE + "철 낚싯대",
			Material.FISHING_ROD, new String[] { ChatColor.WHITE + "상당히 쓸만한 철 낚싯대" }, 1111012, 64 * 4);
	public static final DurableItems golden_fishing_rod = new DurableItems("golden_fishing_rod",
			ChatColor.WHITE + "금 낚싯대", Material.FISHING_ROD, new String[] { ChatColor.WHITE + "비싸보이지만 쓸데없는 금 낚싯대" },
			1111013, 64 / 2);
	public static final DurableItems diamond_fishing_rod = new DurableItems("diamond_fishing_rod",
			ChatColor.WHITE + "다이아몬드 낚싯대", Material.FISHING_ROD, new String[] { ChatColor.WHITE + "훌륭한 다이아몬드 낚싯대" },
			1111014, 64 * 10);
	public static final DurableItems netherite_fishing_rod = new DurableItems("netherite_fishing_rod",
			ChatColor.WHITE + "네더라이트 낚싯대", Material.FISHING_ROD,
			new String[] { ChatColor.WHITE + "매우 훌륭한 최강의 네더라이트 낚싯대" }, 1111015, 64 * 20);
	// 장검
	public static final SwordItems wooden_long_sword = new SwordItems("wooden_long_sword", ChatColor.WHITE + "나무 장검",
			Material.WOODEN_SWORD, new String[] { ChatColor.WHITE + "조잡하게 만들어진 나무 장검" }, 59, 4, 1.6, 0d);
	public static final SwordItems stone_long_sword = new SwordItems("stone_long_sword", ChatColor.WHITE + "돌 장검",
			Material.STONE_SWORD, new String[] { ChatColor.WHITE + "꽤 잘 만들어진 돌 장검" }, 131, 5, 1.6, 0d);
	public static final SwordItems iron_long_sword = new SwordItems("iron_long_sword", ChatColor.WHITE + "철 장검",
			Material.IRON_SWORD, new String[] { ChatColor.WHITE + "상당히 쓸만한 철 장검" }, 250, 6, 1.6, 0d);
	public static final SwordItems golden_long_sword = new SwordItems("golden_long_sword", ChatColor.WHITE + "금 장검",
			Material.GOLDEN_SWORD, new String[] { ChatColor.WHITE + "비싸보이지만 쓸데없는 금 장검" }, 32, 4, 1.6, 0d);
	public static final SwordItems diamond_long_sword = new SwordItems("diamond_long_sword",
			ChatColor.WHITE + "다이아몬드 장검", Material.DIAMOND_SWORD, new String[] { ChatColor.WHITE + "훌륭한 다이아몬드 장검" },
			1561, 7, 1.6, 0d);
	public static final SwordItems netherite_long_sword = new SwordItems("netherite_long_sword",
			ChatColor.WHITE + "네더라이트 장검", Material.NETHERITE_SWORD,
			new String[] { ChatColor.WHITE + "매우 훌륭한 최강의 네더라이트 장검" }, 2031, 8, 1.6, 0d);
	// 단검
	public static final SwordItems wooden_short_sword = new SwordItems("wooden_short_sword", ChatColor.WHITE + "나무 단검",
			Material.WOODEN_SWORD, new String[] { ChatColor.WHITE + "조잡하게 만들어진 나무 단검" }, 1011110, 44, 4, 1.6, 0.05d);
	public static final SwordItems stone_short_sword = new SwordItems("stone_short_sword", ChatColor.WHITE + "돌 단검",
			Material.STONE_SWORD, new String[] { ChatColor.WHITE + "꽤 잘 만들어진 돌 단검" }, 1011110, 98, 5, 1.6, 0.1d);
	public static final SwordItems iron_short_sword = new SwordItems("iron_short_sword", ChatColor.WHITE + "철 단검",
			Material.IRON_SWORD, new String[] { ChatColor.WHITE + "상당히 쓸만한 철 단검" }, 1011110, 187, 6, 1.6, 0.15d);
	public static final SwordItems golden_short_sword = new SwordItems("golden_short_sword", ChatColor.WHITE + "금 단검",
			Material.GOLDEN_SWORD, new String[] { ChatColor.WHITE + "비싸보이지만 쓸데없는 금 단검" }, 1011110, 24, 4, 1.6, 0.1d);
	public static final SwordItems diamond_short_sword = new SwordItems("diamond_short_sword",
			ChatColor.WHITE + "다이아몬드 단검", Material.DIAMOND_SWORD, new String[] { ChatColor.WHITE + "훌륭한 다이아몬드 단검" },
			1011110, 1170, 7, 1.6, 0.2d);
	public static final SwordItems netherite_short_sword = new SwordItems("netherite_short_sword",
			ChatColor.WHITE + "네더라이트 단검", Material.NETHERITE_SWORD,
			new String[] { ChatColor.WHITE + "매우 훌륭한 최강의 네더라이트 단검" }, 1011110, 1523, 8, 1.6, 0.25d);
	// 단궁
	public static final BowItems wooden_bow = new BowItems("wooden_bow", ChatColor.WHITE + "나무 단궁", Material.BOW,
			new String[] { ChatColor.WHITE + "조잡하게 만들어진 나무 단궁" }, 384 / 4, 0.6, 2);
	public static final BowItems stone_bow = new BowItems("stone_bow", ChatColor.WHITE + "돌 단궁", Material.BOW,
			new String[] { ChatColor.WHITE + "꽤 잘 만들어진 돌 단궁" }, 1111111, 384 / 2, 0.8, 4);
	public static final BowItems iron_bow = new BowItems("iron_bow", ChatColor.WHITE + "철 단궁", Material.BOW,
			new String[] { ChatColor.WHITE + "상당히 쓸만한 철 단궁" }, 1111112, 384, 1.0, 6);
	public static final BowItems golden_bow = new BowItems("golden_bow", ChatColor.WHITE + "금 단궁", Material.BOW,
			new String[] { ChatColor.WHITE + "비싸보이지만 쓸데없는 금 단궁" }, 1111113, 384 / 10, 5.0, 5);
	public static final BowItems diamond_bow = new BowItems("diamond_bow", ChatColor.WHITE + "다이아몬드 단궁",
			Material.BOW, new String[] { ChatColor.WHITE + "훌륭한 다이아몬드 단궁" }, 1111114, 384 * 2, 1.2, 8);
	public static final BowItems netherite_bow = new BowItems("netherite_bow", ChatColor.WHITE + "네더라이트 단궁",
			Material.BOW, new String[] { ChatColor.WHITE + "매우 훌륭한 최강의 네더라이트 단궁" }, 1111115, 384 * 5, 1.5, 10);
	// 석궁
	public static final BowItems wooden_crossbow = new BowItems("wooden_crossbow", ChatColor.WHITE + "나무 석궁",
			Material.CROSSBOW, new String[] { ChatColor.WHITE + "조잡하게 만들어진 나무 석궁" }, 465 / 4, 0.6, 3);
	public static final BowItems stone_crossbow = new BowItems("stone_crossbow", ChatColor.WHITE + "돌 석궁",
			Material.CROSSBOW, new String[] { ChatColor.WHITE + "꽤 잘 만들어진 돌 석궁" }, 1111111, 465 / 2, 0.8, 6);
	public static final BowItems iron_crossbow = new BowItems("iron_crossbow", ChatColor.WHITE + "철 석궁",
			Material.CROSSBOW, new String[] { ChatColor.WHITE + "상당히 쓸만한 철 석궁" }, 1111112, 465, 1.0, 9);
	public static final BowItems golden_crossbow = new BowItems("golden_crossbow", ChatColor.WHITE + "금 석궁",
			Material.CROSSBOW, new String[] { ChatColor.WHITE + "비싸보이지만 쓸데없는 금 석궁" }, 1111113, 465 / 10, 5.0, 7);
	public static final BowItems diamond_crossbow = new BowItems("diamond_crossbow", ChatColor.WHITE + "다이아몬드 석궁",
			Material.CROSSBOW, new String[] { ChatColor.WHITE + "훌륭한 다이아몬드 석궁" }, 1111114, 465 * 2, 1.2, 12);
	public static final BowItems netherite_crossbow = new BowItems("netherite_crossbow",
			ChatColor.WHITE + "네더라이트 석궁", Material.CROSSBOW, new String[] { ChatColor.WHITE + "매우 훌륭한 최강의 네더라이트 석궁" },
			1111115, 465 * 5, 1.5, 15);
	// 주머니
	public static final Pocket etc_small_pocket = new Pocket("etc_small_pocket", ChatColor.WHITE + "소형 주머니", 0,
			pocketType.ETC, size.SMALL);
	public static final Pocket etc_medium_pocket = new Pocket("etc_medium_pocket", ChatColor.WHITE + "중형 주머니", 1,
			pocketType.ETC, size.MEDIUM);
	public static final Pocket etc_large_pocket = new Pocket("etc_large_pocket", ChatColor.WHITE + "대형 주머니", 2,
			pocketType.ETC, size.LARGE);

	public static final Pocket mining_small_pocket = new Pocket("mining_small_pocket", ChatColor.WHITE + "소형 광부 주머니", 0,
			pocketType.MINING, size.SMALL);
	public static final Pocket mining_medium_pocket = new Pocket("mining_medium_pocket", ChatColor.WHITE + "중형 광부 주머니",
			1, pocketType.MINING, size.MEDIUM);
	public static final Pocket mining_large_pocket = new Pocket("mining_large_pocket", ChatColor.WHITE + "대형 광부 주머니", 2,
			pocketType.MINING, size.LARGE);

	public static final Pocket fishing_small_pocket = new Pocket("fishing_small_pocket", ChatColor.WHITE + "소형 낚시 주머니",
			0, pocketType.FISHING, size.SMALL);
	public static final Pocket fishing_medium_pocket = new Pocket("fishing_medium_pocket",
			ChatColor.WHITE + "중형 낚시 주머니", 1, pocketType.FISHING, size.MEDIUM);
	public static final Pocket fishing_large_pocket = new Pocket("fishing_large_pocket", ChatColor.WHITE + "대형 낚시 주머니",
			2, pocketType.FISHING, size.LARGE);

	public static final Pocket hunting_small_pocket = new Pocket("hunting_small_pocket", ChatColor.WHITE + "소형 사냥 주머니",
			0, pocketType.HUNTING, size.SMALL);
	public static final Pocket hunting_medium_pocket = new Pocket("hunting_medium_pocket",
			ChatColor.WHITE + "중형 사냥 주머니", 1, pocketType.HUNTING, size.MEDIUM);
	public static final Pocket hunting_large_pocket = new Pocket("hunting_large_pocket", ChatColor.WHITE + "대형 사냥 주머니",
			2, pocketType.HUNTING, size.LARGE);

	public static final Pocket farming_small_pocket = new Pocket("farming_small_pocket", ChatColor.WHITE + "소형 농사 주머니",
			0, pocketType.FARMING, size.SMALL);
	public static final Pocket farming_medium_pocket = new Pocket("farming_medium_pocket",
			ChatColor.WHITE + "중형 농사 주머니", 1, pocketType.FARMING, size.MEDIUM);
	public static final Pocket farming_large_pocket = new Pocket("farming_large_pocket", ChatColor.WHITE + "대형 농사 주머니",
			2, pocketType.FARMING, size.LARGE);
	// 코카 콜라
	public static final Items coca_cola = new Items("coca_cola", ChatColor.AQUA + "코카 콜라", Material.POTION,
			new String[] { ChatColor.WHITE + "마시면 기분이 좋아 붕 뜨는 기분이 든다." }, 1111111, true);
	//
	protected final String key;
	protected final String displayName;
	protected final Material material;
	protected final List<String> lores;
	protected final int model;
	protected final boolean interact;

	public Items(String key, String displayName, Material material, String[] lores) {
		this(key, displayName, material, lores, 0, true);
	}

	public Items(String key, String displayName, Material material, String[] lores, int model) {
		this(key, displayName, material, lores, model, true);
	}

	public Items(String key, String displayName, Material material, String[] lores, boolean interact) {
		this(key, displayName, material, lores, 0, interact);
	}

	public Items(String key, String displayName, Material material, String[] lores, int model, boolean interact) {
		this.key = key;
		this.displayName = displayName;
		this.material = material;
		this.lores = Arrays.asList(lores);
		this.model = model;
		this.interact = interact;
		//
		list.add(this);
	}

	public String getKey() {
		return key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLores() {
		return lores;
	}

	public int getModel() {
		return model;
	}

	public boolean isInteract() {
		return interact;
	}

	public ItemStack getItemStack() {
		ItemStack item = new ItemStack(this.material);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(this.model);
		meta.setDisplayName(this.displayName);
		meta.setLocalizedName(this.key);
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getItemStack(Player player) {
		return getItemStack();
	}

	public static Items valueOf(ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName())
			return valueOf(item.getItemMeta().getLocalizedName());
		return null;
	}

	public static Items valueOf(String key) {
		for (Items item : list)
			if (item.key.equals(key))
				return item;
		return null;
	}

	public static List<Items> values() {
		final List<Items> list = new ArrayList<Items>();
		list.addAll(Items.list);
		return list;
	}

}
