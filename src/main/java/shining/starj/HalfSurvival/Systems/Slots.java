package shining.starj.HalfSurvival.Systems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Slots {
	empty(0), block(1111011), l(1111111), m(1111211), plus(1111311), k(1111411), j(1111511), h(1111611), n(1111711),
	r(1111811), rr(1111911), s(1112011), rs(1112111), up(ChatColor.WHITE + "▲", 1112211),
	down(ChatColor.WHITE + "▼", 1112311), back(ChatColor.GRAY + "돌아가기", 1112411),
	reset(ChatColor.GOLD + "초기화", 1112511), left(ChatColor.WHITE + "◀", 1112611), right(ChatColor.WHITE + "▶", 1112711)

	//
	;

	private final String displayName;
	private final int model;

	private Slots(int model) {
		this(ChatColor.GRAY + "", model);
	}

	private Slots(String displayName, int model) {
		this.displayName = displayName;
		this.model = model;
	}

	public ItemStack getItemStack(Player player) {
		return getItemStack(player, 0);
	}

	public ItemStack getItemStack(Player player, int add) {
		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.displayName);
		meta.setCustomModelData(this.model + add);
		item.setItemMeta(meta);
		return item;
	}
}
