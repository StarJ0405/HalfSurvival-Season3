package com.StarJ.HS.Items.UsableItem;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Items.UsableItems;
import com.StarJ.HS.Systems.GUIs;

public class Pocket extends UsableItems {
	public static enum pocketType {
		MINING("광질 관련", Material.BLACK_SHULKER_BOX) {
			@Override
			public boolean isAllowed(ItemStack item) {
				switch (item != null ? item.getType() : Material.AIR) {
				case COAL_ORE:
				case DEEPSLATE_COAL_ORE:
				case COAL:
				case COAL_BLOCK:

				case COPPER_ORE:
				case DEEPSLATE_COPPER_ORE:
				case RAW_COPPER:
				case RAW_COPPER_BLOCK:
				case COPPER_INGOT:
				case COPPER_BLOCK:

				case IRON_ORE:
				case DEEPSLATE_IRON_ORE:
				case RAW_IRON:
				case RAW_IRON_BLOCK:
				case IRON_NUGGET:
				case IRON_INGOT:
				case IRON_BLOCK:

				case NETHER_GOLD_ORE:
				case GOLD_ORE:
				case DEEPSLATE_GOLD_ORE:
				case RAW_GOLD:
				case RAW_GOLD_BLOCK:
				case GOLD_NUGGET:
				case GOLD_INGOT:
				case GOLD_BLOCK:

				case LAPIS_ORE:
				case DEEPSLATE_LAPIS_ORE:
				case LAPIS_LAZULI:
				case LAPIS_BLOCK:

				case REDSTONE_ORE:
				case DEEPSLATE_REDSTONE_ORE:
				case REDSTONE:
				case REDSTONE_BLOCK:

				case DIAMOND_ORE:
				case DEEPSLATE_DIAMOND_ORE:
				case DIAMOND:
				case DIAMOND_BLOCK:

				case DEEPSLATE_EMERALD_ORE:
				case EMERALD_ORE:
				case EMERALD:
				case EMERALD_BLOCK:

				case ANCIENT_DEBRIS:
				case NETHERITE_SCRAP:
				case NETHERITE_INGOT:
				case NETHERITE_BLOCK:

				case NETHER_QUARTZ_ORE:
				case QUARTZ:
				case QUARTZ_BLOCK:
					return true;
				default:
					return false;
				}
			}
		},
		FARMING("농사 관련", Material.YELLOW_SHULKER_BOX) {
			@Override
			public boolean isAllowed(ItemStack item) {
				switch (item != null ? item.getType() : Material.AIR) {
				case WHEAT_SEEDS:
				case WHEAT:
					return true;
				default:
					return false;
				}

			}
		},
		FISHING("낚시 관련", Material.BLUE_SHULKER_BOX) {
			@Override
			public boolean isAllowed(ItemStack item) {
				switch (item != null ? item.getType() : Material.AIR) {
				case TROPICAL_FISH:
				case COD:
				case SALMON:
				case PUFFERFISH:
					return true;
				default:
					return false;
				}
			}
		},
		HUNTING("사냥 관련", Material.WHITE_SHULKER_BOX) {
			@Override
			public boolean isAllowed(ItemStack item) {
				switch (item != null ? item.getType() : Material.AIR) {
				case BEEF:
				case COOKED_BEEF:
				case PORKCHOP:
				case COOKED_PORKCHOP:
				case CHICKEN:
				case COOKED_CHICKEN:
				case MUTTON:
				case COOKED_MUTTON:
				case RABBIT:
				case COOKED_RABBIT:
					return true;
				default:
					return false;
				}
			}
		},
		ETC("기타 관련", Material.SHULKER_BOX) {
			@Override
			public boolean isAllowed(ItemStack item) {
				switch (item != null ? item.getType() : Material.AIR) {
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
					return false;
				default:
					return !(MINING.isAllowed(item) || FARMING.isAllowed(item) || FISHING.isAllowed(item)
							|| HUNTING.isAllowed(item));
				}

			}
		};

		private String name;
		private Material type;

		private pocketType(String name, Material type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public Material getType() {
			return type;
		}

		public static pocketType getPocketType(ItemStack item) {
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				List<String> lores = item.getItemMeta().getLore();
				for (String lore : lores)
					if (lore.contains(" / "))
						try {
							pocketType type = valueOf(ChatColor.stripColor(lore.split(" / ")[0]));
							if (type != null)
								return type;
						} catch (Exception ex) {

						}
			}
			return null;
		}

		public abstract boolean isAllowed(ItemStack item);
	}

	public static enum size {
		SMALL("소형"), MEDIUM("중형"), LARGE("대형");

		private String name;

		private size(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getSize() {
			return (this.ordinal() + 1) * 9;
		}

		public static size getSize(ItemStack item) {
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
				List<String> lores = item.getItemMeta().getLore();
				for (String lore : lores)
					if (lore.contains(" / "))
						try {
							size type = valueOf(ChatColor.stripColor(lore.split(" / ")[1]));
							if (type != null)
								return type;
						} catch (Exception ex) {

						}
			}
			return null;
		}
	}

	public Pocket(String key, String displayName, int model, pocketType pocketType, size size) {
		super(key, displayName, pocketType.getType(),
				new String[] { ChatColor.WHITE + pocketType.getName() + "의 아이템을 담을 수 있는 " + size.getName() + " 주머니",
						ChatColor.DARK_GRAY + "" + ChatColor.MAGIC + pocketType.name() + " / " + size.name() },
				1111111 + model * 100);
	}

	@Override
	public boolean Use(Player player, Block block) {
		GUIs.pocket.setInfo(player, player.getInventory().getHeldItemSlot() + "");
		GUIs.pocket.openInv(player);
		return false;
	}

	@Override
	public boolean Use(Player player, Entity et) {
		GUIs.pocket.setInfo(player, player.getInventory().getHeldItemSlot() + "");
		GUIs.pocket.openInv(player);
		return false;
	}

}
