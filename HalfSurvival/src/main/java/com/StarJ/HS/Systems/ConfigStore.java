package com.StarJ.HS.Systems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Entities.Entities;
import com.StarJ.HS.Entities.Pets.Pets;

public class ConfigStore {
	// Player
	public static LivingEntity getPlayerPet(Player player) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.isConfigurationSection("pets")) {
				ConfigurationSection cs = fc.getConfigurationSection("pets");
				if (cs.isString("uuid")) {
					UUID uuid = UUID.fromString(cs.getString("uuid"));
					Entity entity = Bukkit.getEntity(uuid);
					if (entity instanceof LivingEntity && entity != null && !entity.isDead())
						return (LivingEntity) entity;
				}
			}
		} catch (Exception ex) {
		}
		return null;
	}

	public static void setPlayerPet(Player player, LivingEntity livingEntity) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", player.getName());
			ConfigurationSection cs = fc.isConfigurationSection("pets") ? fc.getConfigurationSection("pets")
					: fc.createSection("pets");
			cs.set("uuid", livingEntity.getUniqueId().toString());
			fc.set("pets", cs);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static class PetInfo {
		private Pets pet;
		private DyeColor color;
		private int size;
		private Type type;

		public PetInfo(Pets pet, DyeColor color, int size, Type type) {
			this.pet = pet;
			this.color = color;
			this.size = size;
			this.type = type;
		}

		public Pets getPets() {
			return pet;
		}

		public DyeColor getColor() {
			return color;
		}

		public int getSize() {
			return size;
		}

		public Type getType() {
			return type;
		}
	}

	public static PetInfo getPlayerPetInfo(Player player) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.isConfigurationSection("pets")) {
				ConfigurationSection cs = fc.getConfigurationSection("pets");
				if (cs.isString("type")) {
					Entities pet = Entities.valueOf(cs.getString("type"));
					if (pet != null && pet instanceof Pets) {
						int size = 1;
						if (cs.isConfigurationSection("petInventory")) {
							ConfigurationSection ccs = cs.getConfigurationSection("petInventory");
							if (ccs.isInt("size"))
								size = ccs.getInt("size");
						}
						DyeColor color = DyeColor.values()[0];
						if (cs.isString("color"))
							color = DyeColor.valueOf(cs.getString("color"));
						Type catType = null;
						if (cs.isString("catType"))
							catType = Type.valueOf(cs.getString("catType"));
						return new PetInfo((Pets) pet, color, size, catType);
					}
				}
			}
		} catch (Exception ex) {
		}
		return null;
	}

	public static void setPlayerPetType(Player player, Entities entities, DyeColor color, int size, Type catType) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", player.getName());
			ConfigurationSection cs = fc.isConfigurationSection("pets") ? fc.getConfigurationSection("pets")
					: fc.createSection("pets");
			cs.set("type", entities.getKey());
			cs.set("catType", catType != null ? catType.name() : null);
			cs.set("color", color.name());
			ConfigurationSection ccs = cs.isConfigurationSection("petInventory")
					? cs.getConfigurationSection("petInventory")
					: cs.createSection("petInventory");
			int origin = ccs.isInt("size") ? ccs.getInt("size") : 1;
			ccs.set("size", size);
			if (origin < size)
				for (int i = origin; i < size; i++)
					ccs.set(i + "", null);
			else if (origin > size)
				for (int i = size; i < origin; i++)
					if (ccs.isItemStack(i + "")) {
						ItemStack item = ccs.getItemStack(i + "");
						if (!item.isSimilar(Slots.block.getItemStack(player))) {
							Item drop = player.getWorld().dropItem(player.getEyeLocation(), item);
							drop.setOwner(player.getUniqueId());
							drop.setPersistent(true);
						}
						ccs.set(i + "", null);
					}
			cs.set("petInventory", ccs);
			fc.set("pets", cs);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static Inventory getPlayerPetInventory(Player player) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.isConfigurationSection("pets")) {
				ConfigurationSection cs = fc.getConfigurationSection("pets");
				if (cs.isConfigurationSection("petInventory")) {
					ConfigurationSection ccs = cs.getConfigurationSection("petInventory");
					int size = ccs.isInt("size") ? ccs.getInt("size") : 1;
					Inventory petInv = Bukkit.createInventory(null, (size / 9 + (size % 9 > 0 ? 1 : 0)) * 9,
							GUIs.myPetInventory.getTitle());
					for (int i = 0; i < petInv.getSize(); i++)
						if (i >= size)
							petInv.setItem(i, Slots.block.getItemStack(player));
						else if (ccs.isItemStack(i + ""))
							petInv.setItem(i, ccs.getItemStack(i + ""));
					return petInv;
				}
			}
		} catch (Exception ex) {
		}
		Inventory petInv = Bukkit.createInventory(null, 9, GUIs.myPetInventory.getTitle());
		for (int i = 1; i < petInv.getSize(); i++)
			petInv.setItem(i, Slots.block.getItemStack(player));
		return petInv;
	}

	public static void setPlayerPetInventory(Player player, Inventory petInv) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", player.getName());
			ConfigurationSection cs = fc.isConfigurationSection("pets") ? fc.getConfigurationSection("pets")
					: fc.createSection("pets");
			ConfigurationSection ccs = cs.isConfigurationSection("petInventory")
					? cs.getConfigurationSection("petInventory")
					: cs.createSection("petInventory");
			int size = ccs.isInt("size") ? ccs.getInt("size") : 1;
			for (int i = 0; i < size; i++)
				ccs.set(i + "", petInv.getItem(i));
			cs.set("petInventory", ccs);
			fc.set("pets", cs);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static long getPlayerMoney(Player player) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.isInt("money"))
				return fc.getInt("money");
			else if (fc.isLong("money"))
				return fc.getLong("money");
		} catch (Exception ex) {
		}
		return 0l;
	}

	public static void setPlayerMoney(Player player, long money) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", player.getName());
			fc.set("money", money);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPlayerConfig(Player player, String key) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.contains(key))
				return (T) fc.get(key);
		} catch (Exception ex) {
		}
		return null;
	}

	public static boolean hasPlayerConfig(Player player, String key) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (file.exists()) {
				fc.load(file);
				return fc.contains(key);
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static <T> void setPlayerConfig(Player player, String key, T value) {
		File file = new File("plugins/HalfSurvival/players/" + player.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/players");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", player.getName());
			fc.set(key, value);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	// Entity
	@SuppressWarnings("unchecked")
	public static <T> T getEntityConfig(Entity entity, String key) {
		File file = new File("plugins/HalfSurvival/entities/" + entity.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/entities/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			if (fc.contains(key))
				return (T) fc.get(key);
		} catch (Exception ex) {
		}
		return null;
	}

	public static boolean hasEntityConfig(Entity entity, String key) {
		File file = new File("plugins/HalfSurvival/entities/" + entity.getUniqueId().toString() + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (file.exists()) {
				fc.load(file);
				return fc.contains(key);
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static <T> void setEntityConfig(Entity entity, String key, T value) {
		File file = new File("plugins/HalfSurvival/entities/" + entity.getUniqueId().toString() + ".yml");
		File dir = new File("plugins/HalfSurvival/entities/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("name", entity.getName());
			fc.set("customName", entity.getCustomName());
			fc.set(key, value);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static void removeEntityConfig(Entity entity) {
		File file = new File("plugins/HalfSurvival/entities/" + entity.getUniqueId().toString() + ".yml");
		if (file.exists())
			file.delete();
	}

	public static void confirmEntityConfig() {
		File dir = new File("plugins/HalfSurvival/entities/");
		for (File file : dir.listFiles()) {
			if (Bukkit.getEntity(UUID.fromString(file.getName().split("\\.")[0])) == null)
				file.delete();
		}
	}

	// Block
	public static String LocationToString(Location loc) {
		return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
	}

	public static Location StringToLocation(String sloc) {
		if (sloc == null)
			return null;
		String[] sp = sloc.split(",");
		if (sp.length >= 4)
			try {
				World world = Bukkit.getWorld(sp[0]);
				if (world != null) {
					double x = Double.parseDouble(sp[1]);
					double y = Double.parseDouble(sp[2]);
					double z = Double.parseDouble(sp[3]);
					float yaw = 0f;
					float pitch = 0f;
					if (sp.length == 6) {
						yaw = Float.parseFloat(sp[4]);
						pitch = Float.parseFloat(sp[5]);
					}
					return new Location(world, x, y, z, yaw, pitch);
				}
			} catch (NumberFormatException nfe) {

			}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBlockConfig(Block block, String key) {
		File file = new File("plugins/HalfSurvival/blocks/" + LocationToString(block.getLocation()) + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (file.exists()) {
				fc.load(file);
				if (fc.contains(key))
					return (T) fc.get(key);
			}
		} catch (Exception ex) {
		}
		return null;
	}

	public static boolean hasBlockConfig(Block block, String key) {
		File file = new File("plugins/HalfSurvival/blocks/" + LocationToString(block.getLocation()) + ".yml");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (file.exists()) {
				fc.load(file);
				return fc.contains(key);
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static <T> void setBlockConfig(Block block, String key, T value) {
		File file = new File("plugins/HalfSurvival/blocks/" + LocationToString(block.getLocation()) + ".yml");
		File dir = new File("plugins/HalfSurvival/blocks/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set("loc", block.getLocation());
			fc.set("material", block.getType().name());
			fc.set(key, value);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static void removeBlockConfig(Block block) {
		File file = new File("plugins/HalfSurvival/blocks/" + LocationToString(block.getLocation()) + ".yml");
		if (file.exists())
			file.delete();
	}

	public static void confirmBlockConfig() {
		File dir = new File("plugins/HalfSurvival/blocks/");
		for (File file : dir.listFiles()) {
			Location loc = StringToLocation(file.getName().split("\\.")[0]);
			FileConfiguration fc = new YamlConfiguration();
			try {
				if (!file.exists()) {
					dir.mkdirs();
					file.createNewFile();
				}
				fc.load(file);
				if (fc.isString("material")) {
					Material material = Material.valueOf(fc.getString("material"));
					if (material == null || !loc.getBlock().getType().equals(material))
						file.delete();
				} else
					file.delete();
			} catch (Exception ex) {
				file.delete();
			}
		}
	}

	// Portal
	public static List<portalInfo> getPortals() {
		List<portalInfo> list = new ArrayList<portalInfo>();
		File file = new File("plugins/HalfSurvival/portal.yml");
		File dir = new File("plugins/HalfSurvival/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			for (String key : fc.getKeys(false))
				if (fc.isConfigurationSection(key)) {
					ConfigurationSection cs = fc.getConfigurationSection(key);
					if (cs.isLocation("loc") && cs.isString("name"))
						list.add(new portalInfo(cs.getLocation("loc"), cs.getString("name")));
				}
		} catch (Exception ex) {
		}
		return list;
	}

	public static class portalInfo {
		public final Location loc;
		public final String name;

		public portalInfo(Location loc, String name) {
			this.loc = loc;
			this.name = name;
		}
	}

	public static void setPortalName(Location loc, String name) {
		File file = new File("plugins/HalfSurvival/portal.yml");
		File dir = new File("plugins/HalfSurvival/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			String key = LocationToString(loc);
			ConfigurationSection cs = fc.isConfigurationSection(key) ? fc.getConfigurationSection(key)
					: fc.createSection(key);
			cs.set("loc", loc);
			cs.set("name", name);
			fc.set(key, cs);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static void addPortal(Location loc) {
		File file = new File("plugins/HalfSurvival/portal.yml");
		File dir = new File("plugins/HalfSurvival/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			String key = LocationToString(loc);
			if (!fc.isConfigurationSection(key)) {
				ConfigurationSection cs = fc.createSection(key);
				cs.set("loc", loc);
				cs.set("name", key);
				fc.set(key, cs);
				fc.save(file);
			}
		} catch (Exception ex) {
		}
	}

	public static void removePortal(Location loc) {
		File file = new File("plugins/HalfSurvival/portal.yml");
		File dir = new File("plugins/HalfSurvival/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			fc.set(LocationToString(loc), null);
			fc.save(file);
		} catch (Exception ex) {
		}
	}

	public static void confirmPortals() {
		File file = new File("plugins/HalfSurvival/portal.yml");
		File dir = new File("plugins/HalfSurvival/");
		FileConfiguration fc = new YamlConfiguration();
		try {
			if (!file.exists()) {
				dir.mkdirs();
				file.createNewFile();
			}
			fc.load(file);
			for (String key : fc.getKeys(false))
				if (fc.isConfigurationSection(key)) {
					ConfigurationSection cs = fc.getConfigurationSection(key);
					if (cs.isLocation("loc"))
						if (!cs.getLocation("loc").getBlock().getType().equals(Material.NETHER_PORTAL))
							fc.set(key, null);
				}
			fc.save(file);
		} catch (Exception ex) {
		}
	}

}
