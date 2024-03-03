package com.StarJ.HS.Systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.StarJ.HS.Core;
import com.StarJ.HS.Entities.Pets.CatPet;
import com.StarJ.HS.Entities.Pets.DogPet;
import com.StarJ.HS.Entities.Pets.Pets;
import com.StarJ.HS.Items.Items;
import com.StarJ.HS.Items.UsableItem.Pocket;
import com.StarJ.HS.Items.UsableItem.Pocket.pocketType;
import com.StarJ.HS.Items.UsableItem.Pocket.size;
import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.SkillLine;
import com.StarJ.HS.Skills.SkillTree;
import com.StarJ.HS.Skills.Slot;
import com.StarJ.HS.Skills.Slot.SkillSlot;
import com.StarJ.HS.Systems.ConfigStore.PetInfo;
import com.StarJ.HS.Systems.ConfigStore.portalInfo;
import com.StarJ.HS.Systems.HashMapStore.LocationTask;

public enum GUIs {
	skillType(ChatColor.GREEN + "스킬 종류") {
		@Override
		public void openInv(Player player) {
			Inventory inv = Bukkit.createInventory(null, 3 * 9, this.title);
			ItemStack[] contents = inv.getContents();
			contents[10] = SkillType.mining.getItemStack(player);
			contents[12] = SkillType.farming.getItemStack(player);
			contents[14] = SkillType.hunting.getItemStack(player);
			contents[16] = SkillType.fishing.getItemStack(player);
			for (int i = 0; i < contents.length; i++)
				if (contents[i] == null)
					contents[i] = Slots.block.getItemStack(player);
			inv.setContents(contents);
			player.openInventory(inv);
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			switch (slot) {
			case 10:
				player.closeInventory();
				setInfo(player, SkillType.mining.name());
				skill.openInv(player);
				break;
			case 12:
				player.closeInventory();
				setInfo(player, SkillType.farming.name());
				skill.openInv(player);
				break;
			case 14:
				player.closeInventory();
				setInfo(player, SkillType.hunting.name());
				skill.openInv(player);
				break;
			case 16:
				player.closeInventory();
				setInfo(player, SkillType.fishing.name());
				skill.openInv(player);
				break;
			}
			return false;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {
			setInfo(player, null);
		}
	},
	skill(ChatColor.AQUA + "스킬 트리") {

		@Override
		public void openInv(Player player) {
			Inventory inv = Bukkit.createInventory(null, 6 * 9, this.title);
			setInfo(player, 0 + "");
			inv.setContents(getContents(player, false));
			player.openInventory(inv);
		}

		private ItemStack[] getContents(Player player, boolean check) {
			ItemStack[] contents = new ItemStack[6 * 9];
			try {
				SkillType type = SkillType.valueOf(skillType.getInfo(player));
				contents[8 + 9 * 0] = type.getItemStack(player);
				contents[8 + 9 * 1] = Slots.reset.getItemStack(player);
				contents[8 + 9 * 2] = Slots.block.getItemStack(player);
				contents[8 + 9 * 3] = Slots.back.getItemStack(player);
				contents[8 + 9 * 4] = Slots.up.getItemStack(player);
				contents[8 + 9 * 5] = Slots.down.getItemStack(player);
				for (int i = 0; i < 6; i++) {
					contents[0 + 9 * i] = Slots.block.getItemStack(player);
					contents[7 + 9 * i] = Slots.block.getItemStack(player);
				}
				int start = 0;
				try {
					start = Integer.valueOf(getInfo(player));
				} catch (NumberFormatException nfe) {

				}
				if (start < 0)
					start = 0;
				SkillTree tree = type.getSkillTree();
				SkillLine[] lines = tree.getLines();
				for (int i = start; i < lines.length; i++) {
					SkillLine line = lines[i];
					if (i < start + 6) {
						for (Slot slot : line.getSlots())
							contents[(i - start) * 9 + slot.getNum()] = slot.getItemStack(player);
					} else if (check)
						for (Slot slot : line.getSlots())
							if (slot instanceof SkillSlot)
								slot.getItemStack(player);
				}
			} catch (Exception ex) {
			}
			for (int i = 0; i < contents.length; i++)
				if (contents[i] == null)
					contents[i] = Slots.empty.getItemStack(player);
			return contents;
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			if (rawSlot < view.getTopInventory().getSize()) {
				try {
					SkillType type = SkillType.valueOf(skillType.getInfo(player));
					int start = 0;
					try {
						start = Integer.valueOf(getInfo(player));
					} catch (NumberFormatException nfe) {

					}
					int max = type.getSkillTree().getLines().length;
					switch (rawSlot) {
					case 17:
						for (Skill skill : Skill.values(type))
							skill.setLearn(player, false);
						setInfo(player, 0 + "");
						break;
					case 35:
						player.closeInventory();
						skillType.openInv(player);
						break;
					case 44:
						if (start == 0)
							start = max - 6;
						else {
							start -= clickType.isShiftClick() ? 6 : 1;
							start = start < 0 ? 0 : start;
						}
						setInfo(player, start + "");
						break;
					case 53:
						if (start == max - 6)
							start = 0;
						else {
							start += clickType.isShiftClick() ? 6 : 1;
							start = start < max - 6 ? start : max - 6;
						}
						setInfo(player, start + "");
						break;
					default:
						Skill skill = Skill.valueOf(current);
						if (skill != null) {
							if (skill.canLearn(player) == 0
									&& (clickType.equals(ClickType.LEFT) || clickType.equals(ClickType.SHIFT_LEFT)))
								skill.setLearn(player, true);
							else if (skill.canLearn(player) == 1
									&& (clickType.equals(ClickType.RIGHT) || clickType.equals(ClickType.SHIFT_RIGHT))) {
								skill.setLearn(player, false);
								Inventory inv = view.getTopInventory();
								inv.setContents(getContents(player, true));
								return false;
							}
							if (type.equals(SkillType.hunting))
								Skill.Hunting.setMaxAbsorption(player);
						}
						break;
					}
					Inventory inv = view.getTopInventory();
					inv.setContents(getContents(player, false));
				} catch (Exception ex) {
					player.closeInventory();
				}
			}
			return false;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {

		}
	},
	portal(ChatColor.DARK_PURPLE + "포탈") {

		@Override
		public void openInv(Player player) {
			Inventory inv = Bukkit.createInventory(null, 6 * 9, this.title);
			setInfo(player, 0 + "");
			inv.setContents(getContents(player));
			player.openInventory(inv);
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			if (current != null && rawSlot < 5 * 9) {
				Location loc = ConfigStore
						.StringToLocation(ChatColor.stripColor(current.getItemMeta().getLocalizedName()));
				if (loc != null) {
					if (clickType.isShiftClick()) {
						player.sendMessage(ChatColor.WHITE + "변경하실 이름을 10초 이내 입력해주시길 바랍니다.");
						HashMapStore.setChangePortal(player,
								new LocationTask(Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
									if (player.isOnline())
										player.sendMessage(ChatColor.RED + "입력시간을 초과했습니다.");
								}, 20 * 10), loc));
						player.closeInventory();
					} else {
						Advancement adv = Bukkit
								.getAdvancement(NamespacedKey.fromString("halfsurvival:newfunction/portal"));
						player.getAdvancementProgress(adv).awardCriteria("trigger");
						player.closeInventory();
						player.teleport(loc);
					}
				}
			} else {
				int page = 0;
				try {
					page = Integer.valueOf(getInfo(player));
				} catch (NumberFormatException nfe) {

				}
				switch (rawSlot) {
				case 48:
					if (clickType.isShiftClick())
						page -= 10;
					else
						page--;
					break;
				case 50:
					if (clickType.isShiftClick())
						page += 10;
					else
						page++;
					break;
				}
				if (page < 0)
					page = 0;
				int max = (ConfigStore.getPortals().size() - 1) / 45;
				if (page > max)
					page = max;
				player.closeInventory();
				setInfo(player, page + "");
				openInv(player);
			}
			return false;
		}

		private ItemStack[] getContents(Player player) {
			int page = 0;
			int max = (ConfigStore.getPortals().size() - 1) / 45;
			try {
				page = Integer.valueOf(getInfo(player));
			} catch (NumberFormatException nfe) {

			}
			ItemStack[] contents = new ItemStack[6 * 9];

			contents[0 + 9 * 5] = Slots.block.getItemStack(player);
			contents[1 + 9 * 5] = Slots.block.getItemStack(player);
			contents[2 + 9 * 5] = Slots.block.getItemStack(player);
			contents[3 + 9 * 5] = Slots.left.getItemStack(player);
			contents[4 + 9 * 5] = getPage(player, page, max);
			contents[5 + 9 * 5] = Slots.right.getItemStack(player);
			contents[6 + 9 * 5] = Slots.block.getItemStack(player);
			contents[7 + 9 * 5] = Slots.block.getItemStack(player);
			contents[8 + 9 * 5] = Slots.block.getItemStack(player);
			List<portalInfo> list = ConfigStore.getPortals();
			for (int i = 0; i < 5 * 9; i++)
				if (page * 5 * 9 + i >= list.size())
					break;
				else
					contents[i] = getLocationItem(list.get(page * 5 * 9 + i));

			return contents;
		}

		ItemStack getPage(Player player, int page, int max) {
			ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			item.setAmount(page == 0 ? 1 : page);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "현재 페이지");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "페이지 : " + page + " / " + max);
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}

		ItemStack getLocationItem(portalInfo info) {
			ItemStack item = new ItemStack(Material.COMPASS);
			ItemMeta meta = item.getItemMeta();
			String strloc = ConfigStore.LocationToString(info.loc);
			meta.setDisplayName(ChatColor.GREEN + info.name);
			meta.setLocalizedName(strloc);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "바이옴 : " + info.loc.getBlock().getBiome().toString());
			lore.add(ChatColor.WHITE + "위치 : " + strloc);
			lore.add(ChatColor.RED + "쉬프트 클릭시 이름 변경");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {
			setInfo(player, null);
		}

	},
	pocket(ChatColor.WHITE + "주머니") {

		@Override
		public void openInv(Player player) {
			String info = getInfo(player);
			if (info != null && info != "")
				try {
					ItemStack held = player.getInventory().getItem(Integer.valueOf(info));
					size size = Pocket.size.getSize(held);
					if (size != null) {
						Inventory inv = Bukkit.createInventory(null, size.getSize(), title);
						if (held.getItemMeta() instanceof BlockStateMeta) {
							BlockStateMeta meta = (BlockStateMeta) held.getItemMeta();
							if (meta.getBlockState() instanceof ShulkerBox) {
								ShulkerBox box = (ShulkerBox) meta.getBlockState();
								ItemStack[] contents = inv.getContents();
								ItemStack[] pre = box.getInventory().getContents();
								for (int i = 0; i < contents.length; i++)
									contents[i] = pre[i];
								inv.setContents(contents);
							}
						}
						player.openInventory(inv);
					}
				} catch (Exception ex) {

				}
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			if (clickType.equals(ClickType.SWAP_OFFHAND)) {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
					player.updateInventory();
				}, 1);
				return false;
			}
			if (clickType.equals(ClickType.NUMBER_KEY))
				return false;
			String info = getInfo(player);
			if (info != null && info != "")
				try {
					int heldSlot = Integer.valueOf(info);
					ItemStack held = player.getInventory().getItem(heldSlot);
					if (current != null) {
						pocketType pocketType = Pocket.pocketType.getPocketType(held);
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
						if (rawSlot < view.getTopInventory().getSize() || slot != heldSlot)
							return pocketType.isAllowed(current);
					} else if (cursor != null) {
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
						return true;
					}
				} catch (Exception ex) {

				}

			return false;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {
			setInfo(player, null);
		}
	},
	sell(ChatColor.WHITE + "판매기") {
		@Override
		public void openInv(Player player) {
			Inventory inv = Bukkit.createInventory(null, 6 * 9, this.title);
			player.openInventory(inv);
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			return true;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {
			long price = 0;
			for (ItemStack item : contents)
				if (item != null)
					if (item.getType().name().contains("SHULKER_BOX")) {
						if (item.getItemMeta() instanceof BlockStateMeta) {
							BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
							if (meta.getBlockState() instanceof ShulkerBox) {
								ShulkerBox box = (ShulkerBox) meta.getBlockState();
								for (ItemStack nowItem : box.getInventory().getContents())
									price += getPrice(nowItem);
								box.getInventory().clear();
								meta.setBlockState(box);
								Items items = Items.valueOf(item);
								if (items != null)
									meta.setCustomModelData(items.getModel());
							}
							item.setItemMeta(meta);
							player.getWorld().dropItem(player.getEyeLocation(), item);
						}
					} else
						price += getPrice(item);
			if (price > 0) {
				long origin = ConfigStore.getPlayerMoney(player);
				ConfigStore.setPlayerMoney(player, origin + price);
				player.sendMessage(ChatColor.YELLOW + "" + price + ChatColor.WHITE + "원 획득 (최종 : " + ChatColor.YELLOW
						+ (origin + price) + ChatColor.WHITE + ")");
			}
		}

	},
	myPet(ChatColor.GREEN + "펫 설정") {

		@Override
		public void openInv(Player player) {
			Inventory inv = Bukkit.createInventory(null, 3 * 9, this.title);
			ItemStack[] contents = inv.getContents();
			contents[9 * 1 + 2] = getSpawn();
			contents[9 * 1 + 6] = getUpgrade(player);
			for (int i = 0; i < contents.length; i++)
				if (contents[i] == null)
					contents[i] = Slots.block.getItemStack(player);
			inv.setContents(contents);
			player.openInventory(inv);
		}

		private ItemStack getSpawn() {
			ItemStack item = new ItemStack(Material.EGG);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "소환");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + "좌클릭시 소환");
			lore.add(ChatColor.RED + "우클릭시 소환 해제");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}

		private ItemStack getUpgrade(Player player) {
			ItemStack item = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + "재설정");
			List<String> lore = new ArrayList<String>();
			PetInfo info = ConfigStore.getPlayerPetInfo(player);
			if (info != null) {
				Pets pets = info.getPets();
				lore.add(ChatColor.WHITE + "인벤토리 종류 : " + pets.getCustomName());
				lore.add(ChatColor.WHITE + "인벤토리 크기 : " + info.getSize());
				lore.add(ChatColor.WHITE + "색깔 : " + getColorName(info.getColor()));
				if (pets instanceof CatPet) {
					Type catType = info.getType();
					if (catType != null)
						lore.add(ChatColor.WHITE + "타입 : " + getTypeName(catType));
				}
			} else
				lore.add(ChatColor.RED + "미지급 상태");
			Integer stackI = ConfigStore.getPlayerConfig(player, "pets.petInventory.stack");
			int stack = stackI != null ? stackI : 0;
			lore.add(ChatColor.YELLOW + "가격 : " + ConfigStore.getPlayerMoney(player) + " / " + getPrice(stack));
			lore.add(ChatColor.GREEN + "클릭시 애완 동물의 인벤토리가 랜덤으로 재설정됩니다.");
			lore.add(ChatColor.RED + "크기 축소시 남는 아이템은 바닥에 떨어뜨립니다.");
			lore.add(ChatColor.RED + "업그레이드 기존 펫은 소환 해제됩니다.");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
			return item;
		}

		private String getTypeName(Type catType) {
			switch (catType) {
			case ALL_BLACK:
				return "검은 고양이";
			case BLACK:
				return "턱시도 고양이";
			case BRITISH_SHORTHAIR:
				return "브리티시 숏헤어 고양이";
			case CALICO:
				return "삼색털 고양이";
			case JELLIE:
				return "젤리 고양이";
			case PERSIAN:
				return "페르시안 고양이";
			case RAGDOLL:
				return "래그돌 고양이";
			case RED:
				return "빨간색 고양이";
			case SIAMESE:
				return "샴 고양이";
			case TABBY:
				return "얼룩 고양이";
			case WHITE:
				return "하얀색 고양이";
			default:
				return "";
			}
		}

		private String getColorName(DyeColor color) {
			switch (color) {
			case BLACK:
				return "검정";
			case BLUE:
				return "파랑";
			case BROWN:
				return "고동";
			case CYAN:
				return "시안";
			case GRAY:
				return "회색";
			case GREEN:
				return "초록";
			case LIGHT_BLUE:
				return "하늘";
			case LIGHT_GRAY:
				return "무채";
			case LIME:
				return "연두";
			case MAGENTA:
				return "자홍";
			case ORANGE:
				return "주황";
			case PINK:
				return "분홍";
			case PURPLE:
				return "보라";
			case RED:
				return "빨강";
			case WHITE:
				return "하양";
			case YELLOW:
				return "노랑";
			default:
				return "";
			}
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			switch (rawSlot) {
			case 11: {
				if (clickType.equals(ClickType.LEFT) || clickType.equals(ClickType.SHIFT_LEFT)) {
					// 소환 or 재소환
					LivingEntity livingEntity = ConfigStore.getPlayerPet(player);
					if (livingEntity != null)
						livingEntity.remove();
					PetInfo info = ConfigStore.getPlayerPetInfo(player);
					Pets pets = null;
					DyeColor color = null;
					Type type = null;
					if (info == null) {
						pets = Pets.getRandomType();
						color = Pets.getRandomColor();
						if (pets instanceof DogPet)
							ConfigStore.setPlayerPetType(player, pets, color, 1, null);
						else {
							type = Pets.getRandomCatType();
							ConfigStore.setPlayerPetType(player, pets, color, 1, type);
						}
					} else {
						pets = info.getPets();
						color = info.getColor();
						type = info.getType();
					}
					livingEntity = pets.spawnEntity(player.getLocation());
					pets.setOwner(player, livingEntity);
					if (pets instanceof DogPet)
						((DogPet) pets).setColor(livingEntity, color);
					else if (pets instanceof CatPet)
						((CatPet) pets).setColor(livingEntity, color, type);
					ConfigStore.setPlayerPet(player, livingEntity);
					ConfigStore.setPlayerConfig(player, "pets.status", true);
					player.closeInventory();
					Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/mypet"));
					if (adv != null)
						player.getAdvancementProgress(adv).awardCriteria("trigger");
				} else {
					// 소환 해제
					LivingEntity livingEntity = ConfigStore.getPlayerPet(player);
					if (livingEntity != null)
						livingEntity.remove();
					ConfigStore.setPlayerConfig(player, "pets.status", null);
					player.closeInventory();
				}
			}
				break;
			case 15: {
				// 업그레이드
				Integer stackI = ConfigStore.getPlayerConfig(player, "pets.petInventory.stack");
				int stack = stackI != null ? stackI : 0;
				long money = ConfigStore.getPlayerMoney(player);
				long price = getPrice(stack);
				if (money >= price) {
					LivingEntity livingEntity = ConfigStore.getPlayerPet(player);
					if (livingEntity != null)
						livingEntity.remove();
					ConfigStore.setPlayerConfig(player, "pets.status", null);
					Pets pets = Pets.getRandomType();
					if (pets instanceof DogPet)
						ConfigStore.setPlayerPetType(player, pets, Pets.getRandomColor(), Pets.getRandomSize(), null);
					else
						ConfigStore.setPlayerPetType(player, pets, Pets.getRandomColor(), Pets.getRandomSize(),
								Pets.getRandomCatType());
					ConfigStore.setPlayerMoney(player, money - price);
					ConfigStore.setPlayerConfig(player, "pets.petInventory.stack", stack + 1);
					view.getTopInventory().setItem(15, getUpgrade(player));
				} else {
					player.sendMessage(ChatColor.RED + "돈이 부족합니다.");
					player.closeInventory();
				}
			}
				break;
			}
			return false;
		}

		private long getPrice(int stack) {
			return (Math.max(1, Math.min(50, stack))) * 2000;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {

		}

	},
	myPetInventory(ChatColor.GREEN + "펫 창고") {

		@Override
		public void openInv(Player player) {
			Inventory inv = ConfigStore.getPlayerPetInventory(player);
			player.openInventory(inv);
		}

		@Override
		public boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
				ItemStack cursor, ItemStack current) {
			if (current != null && current.isSimilar(Slots.block.getItemStack(player)))
				return false;
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				ConfigStore.setPlayerPetInventory(player, view.getTopInventory());
			}, 1);
			return true;
		}

		@Override
		public void close(Player player, Inventory inv, ItemStack[] contents) {
			ConfigStore.setPlayerPetInventory(player, inv);
		}

	}
	//
	;

	protected final String title;
	protected final HashMap<UUID, String> infos;

	private GUIs(String title) {
		this.title = title;
		this.infos = new HashMap<UUID, String>();

	}

	public String getTitle() {
		return this.title;
	}

	public abstract void openInv(Player player);

	public abstract boolean clickInv(Player player, InventoryView view, ClickType clickType, int rawSlot, int slot,
			ItemStack cursor, ItemStack current);

	public abstract void close(Player player, Inventory inv, ItemStack[] contents);

	public void setInfo(Player player, String info) {
		infos.put(player.getUniqueId(), info);
	}

	public String getInfo(Player player) {
		return infos.containsKey(player.getUniqueId()) ? infos.get(player.getUniqueId()) : "";
	}

	public static GUIs getGUI(String title) {
		for (GUIs gui : values())
			if (gui.title.contentEquals(title))
				return gui;
		return null;
	}

	private static int getStarPoint(ItemStack item) {
		int count = 0;
		if (item.hasItemMeta() && item.getItemMeta().hasLore())
			for (String lore : item.getItemMeta().getLore())
				if (lore.contains("★")) {
					count += lore.chars().filter(c -> c == '★').count();
					break;
				}
		switch (count) {
		case 3:
			return 5;
		case 2:
			return 3;
		case 1:
			return 2;
		default:
			return 1;
		}
	}

	private static long getPrice(Material type) {
		switch (type) {
		default:
			return 1;
		}
	}

	private static long getPrice(String key) {
		switch (key) {
		// 쓰레기
		case "starfish":
			return 74;
		case "seahorse":
			return 96;
		case "mola":
			return 136;
		case "trash_can":
			return 60;
		case "plastic_bottle":
			return 60;
		case "jellyfish":
			return 96;
		case "hermit_crab":
			return 74;
		case "betta":
			return 233;
		case "smelt":
			return 136;
		case "hyeill":
			return 12500l;
		case "cod":
			return 365;
		case "salmon":
			return 365;
		case "tropical_fish":
			return 365;
		case "pufferfish":
			return 365;
		case "tuna":
			return 901;
		case "killerwhale":
			return 479;
		case "marlin":
			return 567;
		case "shark":
			return 479;
		case "manta":
			return 696;
		case "lamprey_eel":
			return 696;
		case "squid":
			return 901;
		case "octopus":
			return 1277;
		case "shrimp":
			return 1613;
		case "turtle":
			return 567;
		case "crab":
			return 1277;
		case "lobster":
			return 1613;
		case "whale":
			return 9200;
		case "shell":
			return 11323;
		case "camouflaged_octopus":
			return 14720;
		case "wrasse":
			return 21029;
		default:
			return 0;
		}
	}

	public static long getPrice(ItemStack item) {
		if (item != null) {
			int starPoint = getStarPoint(item);
			if (item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
					&& item.getItemMeta().getLocalizedName() != null) {
				long now = getPrice(item.getItemMeta().getLocalizedName());
				if (now > 0)
					return now * starPoint * item.getAmount();
			}
			return getPrice(item.getType()) * starPoint * item.getAmount();
		} else
			return 0;
	}
}
