package shining.starj.HalfSurvival.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BowItems extends DurableItems {
	private final double damage;
	private final double arrowSpeed;

	public BowItems(String key, String displayName, Material material, String[] lores, int durability,
			double arrowSpeed, double damage) {
		this(key, displayName, material, lores, 0, durability, arrowSpeed, damage);
	}

	public BowItems(String key, String displayName, Material material, String[] lores, int model, int durability,
			double arrowSpeed, double damage) {
		super(key, displayName, material, lores, model, durability);
		this.arrowSpeed = arrowSpeed;
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}

	public double getArrowSpeed() {
		return arrowSpeed;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack item = super.getItemStack();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		if (this.arrowSpeed != 0 && this.damage != 0) {
			lore.add(ChatColor.GRAY + "");
			lore.add(ChatColor.GRAY + "주로 사용하는 손에 있을 때:");
			if (this.arrowSpeed > 0)
				lore.add(ChatColor.BLUE + "+" + String.format("%.1f", this.arrowSpeed) + " 화살 속도");
			else if (this.arrowSpeed < 0)
				lore.add(ChatColor.RED + String.format("%.1f", this.arrowSpeed) + " 화살 속도");

			if (this.damage > 0)
				lore.add(ChatColor.BLUE + "+" + String.format("%.0f", this.damage) + " 공격 피해");
			else if (this.damage < 0)
				lore.add(ChatColor.RED + String.format("%.0f", this.damage) + " 공격 피해");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
