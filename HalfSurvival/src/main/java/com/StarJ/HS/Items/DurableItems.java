package com.StarJ.HS.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class DurableItems extends Items {
	private final int durability;

	public DurableItems(String key, String displayName, Material material, String[] lores, int durability) {
		this(key, displayName, material, lores, 0, durability);
	}

	public DurableItems(String key, String displayName, Material material, String[] lores, int model, int durability) {
		super(key, displayName, material, lores, model, true);
		this.durability = durability;
	}

	@Override
	public List<String> getLores() {
		List<String> lore = new ArrayList<String>();
		lore.addAll(this.lores);
		lore.add(ChatColor.GRAY + "내구도 : " + this.durability + " / " + this.durability);
		return lore;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack item = super.getItemStack();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		lore.add(ChatColor.GRAY + "내구도 : " + this.durability + " / " + this.durability);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public void modifyItemStack(ItemStack item, int mod) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		for (int i = 0; i < lore.size(); i++) {
			String l = lore.get(i);
			if (l.contains("내구도 : "))
				try {
					String split = l.split("내구도 : ")[1];
					if (split.contains(" / ")) {
						String[] sp = split.split(" / ");
						int max = Integer.valueOf(sp[1]);
						int now = Integer.valueOf(sp[0]);
						now += mod;
						if (now > max)
							now = max;
						else if (now < 0)
							item.setAmount(0);

						lore.set(i, ChatColor.GRAY + "내구도 : " + now + " / " + max);
						if (meta instanceof Damageable) {
							int basicMax = item.getType().getMaxDurability();
							((Damageable) meta).setDamage((int) (basicMax * (max - now) * 1d / max));
						}
						break;
					}
				} catch (NumberFormatException nfe) {

				}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
