package shining.starj.HalfSurvival.Listseners;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Items.Items;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.GUIs;
import shining.starj.HalfSurvival.Systems.Slots;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryListener implements Listener {
	@EventHandler
	public void Events(InventoryOpenEvent e) {
		ItemStack[] items = e.getInventory().getContents();
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (item != null)
				if (item.getType().equals(Material.ENCHANTED_BOOK))
					items[i] = null;
				else
					for (Enchantment ench : item.getEnchantments().keySet())
						item.removeEnchantment(ench);
		}
		e.getInventory().setContents(items);
	}

	@EventHandler
	public void Events(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		InventoryView view = e.getView();
		if (e.getClickedInventory() != null) {
			String title = view.getTitle();
			GUIs gui = GUIs.getGUI(title);
			if (gui != null)
				e.setCancelled(!gui.clickInv(player, view, e.getClick(), e.getRawSlot(), e.getSlot(), e.getCursor(),
						e.getCurrentItem()));
		} else if (e.getClick().equals(ClickType.RIGHT)
				&& (e.getCursor() == null || e.getCursor().getType().equals(Material.AIR))) {
			HashMap<String, HashMap<ItemStack, Integer>> list = new HashMap<String, HashMap<ItemStack, Integer>>();
			switch (view.getTopInventory().getType()) {
			case CRAFTING: {
				ItemStack[] contents = player.getInventory().getStorageContents();
				a: for (int i = 9; i < contents.length; i++)
					if (contents[i] != null) {
						ItemStack now = contents[i];
						String key = now.getItemMeta().hasLocalizedName() ? now.getItemMeta().getLocalizedName()
								: now.getType().name();
						HashMap<ItemStack, Integer> hs = list.containsKey(key) ? list.get(key)
								: new HashMap<ItemStack, Integer>();
						contents[i] = null;
						for (ItemStack inputed : hs.keySet())
							if (inputed.isSimilar(now)) {
								hs.put(inputed, hs.get(inputed) + now.getAmount());
								continue a;
							}
						hs.put(now, now.getAmount());
						list.put(key, hs);
					}
				int i = 9;
				for (String key : list.keySet()) {
					HashMap<ItemStack, Integer> hs = list.get(key);
					for (ItemStack item : hs.keySet()) {
						int amount = hs.get(item);
						while (amount > 0) {
							ItemStack now = item.clone();
							now.setAmount(amount > 64 ? 64 : amount);
							amount -= 64;
							contents[i] = now;
							i++;
						}
					}
				}
				player.getInventory().setStorageContents(contents);
				Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/sort"));
				if (adv != null)
					player.getAdvancementProgress(adv).awardCriteria("trigger");
			}
				break;
			case SHULKER_BOX:
			case CHEST:
			case ENDER_CHEST:
				GUIs gui = GUIs.getGUI(e.getView().getTitle());
				if (gui != null && !gui.equals(GUIs.pocket)) {
					if (gui.equals(GUIs.myPetInventory)) {
						ItemStack[] contents = view.getTopInventory().getContents();
						a: for (int i = 0; i < contents.length; i++)
							if (contents[i] != null) {
								ItemStack now = contents[i];
								if (now.isSimilar(Slots.block.getItemStack(player)))
									continue a;
								String key = now.getItemMeta().hasLocalizedName() ? now.getItemMeta().getLocalizedName()
										: now.getType().name();
								HashMap<ItemStack, Integer> hs = list.containsKey(key) ? list.get(key)
										: new HashMap<ItemStack, Integer>();
								contents[i] = null;
								for (ItemStack inputed : hs.keySet())
									if (inputed.isSimilar(now)) {
										hs.put(inputed, hs.get(inputed) + now.getAmount());
										continue a;
									}
								hs.put(now, now.getAmount());
								list.put(key, hs);
							}
						int i = 0;
						for (String key : list.keySet()) {
							HashMap<ItemStack, Integer> hs = list.get(key);
							for (ItemStack item : hs.keySet()) {
								int amount = hs.get(item);
								while (amount > 0) {
									ItemStack now = item.clone();
									now.setAmount(amount > 64 ? 64 : amount);
									amount -= 64;
									contents[i] = now;
									i++;
								}
							}
						}
						view.getTopInventory().setContents(contents);
						Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/sort"));
						if (adv != null)
							player.getAdvancementProgress(adv).awardCriteria("trigger");
						ConfigStore.setPlayerPetInventory(player, view.getTopInventory());
					} else
						e.setCancelled(true);
					return;
				}
				ItemStack[] contents = view.getTopInventory().getContents();
				a: for (int i = 0; i < contents.length; i++)
					if (contents[i] != null) {
						ItemStack now = contents[i];
						String key = now.getItemMeta().hasLocalizedName() ? now.getItemMeta().getLocalizedName()
								: now.getType().name();
						HashMap<ItemStack, Integer> hs = list.containsKey(key) ? list.get(key)
								: new HashMap<ItemStack, Integer>();
						contents[i] = null;
						for (ItemStack inputed : hs.keySet())
							if (inputed.isSimilar(now)) {
								hs.put(inputed, hs.get(inputed) + now.getAmount());
								continue a;
							}
						hs.put(now, now.getAmount());
						list.put(key, hs);
					}
				int i = 0;
				for (String key : list.keySet()) {
					HashMap<ItemStack, Integer> hs = list.get(key);
					for (ItemStack item : hs.keySet()) {
						int amount = hs.get(item);
						while (amount > 0) {
							ItemStack now = item.clone();
							now.setAmount(amount > 64 ? 64 : amount);
							amount -= 64;
							contents[i] = now;
							i++;
						}
					}
				}
				view.getTopInventory().setContents(contents);
				Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/sort"));
				if (adv != null)
					player.getAdvancementProgress(adv).awardCriteria("trigger");
				String info = GUIs.pocket.getInfo(player);
				if (info != null && info != "")
					try {
						int heldSlot = Integer.valueOf(info);
						ItemStack held = player.getInventory().getItem(heldSlot);
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							if (held.getItemMeta() instanceof BlockStateMeta) {
								BlockStateMeta meta = (BlockStateMeta) held.getItemMeta();
								if (meta.getBlockState() instanceof ShulkerBox) {
									ShulkerBox box = (ShulkerBox) meta.getBlockState();
									box.getInventory().setContents(view.getTopInventory().getContents());
									meta.setBlockState(box);
									Items items = Items.valueOf(held);
									if (items != null) {
										int model = 0;
										for (ItemStack test : view.getTopInventory().getContents())
											if (test != null)
												model++;
										meta.setCustomModelData(items.getModel() + model);
									}
								}
								held.setItemMeta(meta);
								player.getInventory().setItem(heldSlot, held);
							}
						}, 1);
					} catch (Exception ex) {

					}
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	public void Events(InventoryCloseEvent e) {
		String title = e.getView().getTitle();
		GUIs gui = GUIs.getGUI(title);
		Player player = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		if (gui != null)
			gui.close(player, inv, inv.getContents());
	}

	@EventHandler
	public void Events(CraftItemEvent e) {
		ItemStack result = e.getInventory().getResult();
		Items i = Items.valueOf(result);
		Player player = (Player) e.getWhoClicked();
		if (i != null && i.getKey().equals(Items.coca_cola.getKey())) {
			Advancement adv = Bukkit
					.getAdvancement(new NamespacedKey("halfsurvival", "encyclopedia/hidden/make_coca_cola"));
			if (adv != null)
				player.getAdvancementProgress(adv).awardCriteria("trigger");
		}

	}

	@EventHandler
	public void Events(PrepareItemCraftEvent e) {
		Recipe recipe = e.getRecipe();
		if (recipe != null && recipe.getResult() != null) {
			ItemStack result = recipe.getResult();
			Items i = Items.valueOf(result);
			if (i == null) {
				int amount = 0;
				int count = 0;
				for (ItemStack item : e.getInventory().getMatrix())
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore())
						for (String lore : item.getItemMeta().getLore())
							if (lore.contains("☆") || lore.contains("★")) {
								amount++;
								count += lore.chars().filter(c -> c == '★').count();
							}
				if (amount > 0) {
					ItemMeta meta = result.getItemMeta();
					List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
					switch (count / amount) {
					case 0:
						lore.add(0, ChatColor.WHITE + "☆☆☆");
						break;
					case 1:
						lore.add(0, ChatColor.WHITE + "★☆☆");
						break;
					case 2:
						lore.add(0, ChatColor.WHITE + "★★☆");
						break;
					case 3:
						lore.add(0, ChatColor.WHITE + "★★★");
						break;
					}
					meta.setLore(lore);
					result.setItemMeta(meta);
					e.getInventory().setResult(result);
				}
			} else {
				if (i.getKey().equals(Items.coca_cola.getKey())) {
					int amount = 0;
					int count = 0;
					for (ItemStack item : e.getInventory().getMatrix())
						if (item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
								&& item.getItemMeta().getLocalizedName().equals("trash_can")
								&& item.getItemMeta().hasLore()) {
							for (String lore : item.getItemMeta().getLore())
								if (lore.contains("★★★"))
									count += 3;
							amount++;
						}
					if (amount > 0) {
						switch (count / amount) {
						case 0:
						case 1:
						case 2:
							e.getInventory().setResult(new ItemStack(Material.AIR));
							return;
						case 3:
							break;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void Events(PrepareAnvilEvent e) {
		AnvilInventory ainv = e.getInventory();
		if (ainv.getItem(1) == null && ainv.getRenameText() != null && ainv.getRenameText().equals("")) {
			ItemStack result = e.getResult();
			Items i = Items.valueOf(result);
			if (i != null) {
				ItemMeta meta = result.getItemMeta();
				meta.setDisplayName(i.getDisplayName());
				result.setItemMeta(meta);
				e.setResult(result);
			}
		}
	}

	@EventHandler
	public void Events(PrepareSmithingEvent e) {
		if (e.getInventory().getRecipe() != null) {
			ItemStack result = e.getResult();
			Items i = Items.valueOf(e.getInventory().getRecipe().getResult());
			if (i != null) {
				ItemMeta meta = result.getItemMeta();
				meta.setDisplayName(i.getDisplayName());
				meta.setLocalizedName(i.getKey());
				meta.setLore(i.getLores());
				meta.setCustomModelData(i.getModel());
				result.setItemMeta(meta);
			}
		}
	}
}
