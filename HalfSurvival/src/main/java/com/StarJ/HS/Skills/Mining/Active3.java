package com.StarJ.HS.Skills.Mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Core;
import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.SkillType;

public class Active3 extends UsableSkill {

	public Active3(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.mining, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		int size = getSize(player);
		lore.add(ChatColor.WHITE + "주변 " + (size * 2 + 1) + "x" + (size * 2 + 1) + "x" + (size * 2 + 1)
				+ " 범위내 광물을 탐색합니다.");
		return lore;
	}

	public int getSize(Player player) {
		return (int) (7
				+ (Skill.Mining.upgrade_middle3_1.hasLearn(player) ? Skill.Mining.upgrade_middle3_1.getEffect() : 0));
	}

	Material getRandom() {
		Random r = new Random();
		double now = r.nextDouble();
		if (now < 1 / 2d) // 50%
			return r.nextBoolean() ? Material.COAL_ORE : Material.DEEPSLATE_COAL_ORE;
		else if (now < 1 / 2d + 1 / 4d) // 25%
			return r.nextBoolean() ? Material.COPPER_ORE : Material.DEEPSLATE_COPPER_ORE;
		else if (now < 1 / 2d + 1 / 4d + 1 / 8d) // 12.5%
			return r.nextBoolean() ? (r.nextBoolean() ? Material.DEEPSLATE_IRON_ORE : Material.IRON_ORE)
					: (r.nextDouble() < 0.2d ? Material.NETHER_GOLD_ORE
							: (r.nextBoolean() ? Material.DEEPSLATE_GOLD_ORE : Material.GOLD_ORE));
		else if (now < 1 / 2d + 1 / 4d + 1 / 8d + 1 / 16d) // 6.25%
			return r.nextDouble() < 0.2d ? Material.NETHER_QUARTZ_ORE
					: (r.nextBoolean() ? r.nextBoolean() ? Material.DEEPSLATE_LAPIS_ORE : Material.LAPIS_ORE
							: r.nextBoolean() ? Material.DEEPSLATE_REDSTONE_ORE : Material.REDSTONE_ORE);
		else if (now < 1 / 2d + 1 / 4d + 1 / 8d + 1 / 16d + 1 / 32d) // 3.125%
			return r.nextBoolean() ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
		else if (now < 1 / 2d + 1 / 4d + 1 / 8d + 1 / 16d + 1 / 32d + 1 / 64d) // 1.5625%
			return r.nextBoolean() ? Material.DEEPSLATE_EMERALD_ORE : Material.EMERALD_ORE;
		else if (now < 1 / 2d + 1 / 4d + 1 / 8d + 1 / 16d + 1 / 32d + 1 / 64d + 1 / 128d) // 0.78125%
			return Material.ANCIENT_DEBRIS;
		return Material.STONE;
	}

	class MaterialInfo {
		public final String name;
		public final Color color;

		public MaterialInfo(String name, Color color) {
			this.name = name;
			this.color = color;
		}
	}

	MaterialInfo getInfo(Material type) {
		switch (type) {
		case COAL_ORE:
		case DEEPSLATE_COAL_ORE:
			return new MaterialInfo(ChatColor.BLACK + "석탄", Color.BLACK);
		case COPPER_ORE:
		case DEEPSLATE_COPPER_ORE:
			return new MaterialInfo(ChatColor.GOLD + "구리", Color.fromRGB(108, 60, 12));
		case DEEPSLATE_DIAMOND_ORE:
		case DIAMOND_ORE:
			return new MaterialInfo(ChatColor.AQUA + "다이아몬드", Color.fromRGB(185, 242, 255));
		case DEEPSLATE_EMERALD_ORE:
		case EMERALD_ORE:
			return new MaterialInfo(ChatColor.GREEN + "에메랄드", Color.fromRGB(0, 201, 87));
		case DEEPSLATE_GOLD_ORE:
		case GOLD_ORE:
		case NETHER_GOLD_ORE:
			return new MaterialInfo(ChatColor.YELLOW + "금", Color.fromRGB(223, 199, 108));
		case DEEPSLATE_IRON_ORE:
		case IRON_ORE:
			return new MaterialInfo(ChatColor.GRAY + "철", Color.fromRGB(161, 157, 148));
		case DEEPSLATE_LAPIS_ORE:
		case LAPIS_ORE:
			return new MaterialInfo(ChatColor.BLUE + "청금석", Color.BLUE);
		case DEEPSLATE_REDSTONE_ORE:
		case REDSTONE_ORE:
			return new MaterialInfo(ChatColor.RED + "레드스톤", Color.RED);
		case NETHER_QUARTZ_ORE:
			return new MaterialInfo(ChatColor.WHITE + "네더 석영", Color.WHITE);
		case ANCIENT_DEBRIS:
			return new MaterialInfo(ChatColor.DARK_PURPLE + "고대 잔해", Color.fromRGB(89, 70, 178));
		default:
			return null;
		}
	}

	public Material switchDeepslate(Material type) {
		switch (type) {
		case DEEPSLATE_IRON_ORE:
			return Material.IRON_ORE;
		case DEEPSLATE_COAL_ORE:
			return Material.COAL_ORE;
		case DEEPSLATE_COPPER_ORE:
			return Material.COPPER_ORE;
		case DEEPSLATE_DIAMOND_ORE:
			return Material.DIAMOND_ORE;
		case DEEPSLATE_EMERALD_ORE:
			return Material.EMERALD_ORE;
		case DEEPSLATE_GOLD_ORE:
			return Material.GOLD_ORE;
		case DEEPSLATE_LAPIS_ORE:
			return Material.LAPIS_ORE;
		case DEEPSLATE_REDSTONE_ORE:
			return Material.REDSTONE_ORE;
		default:
			return type;
		}
	}

	private class Info {
		private List<Location> locs = new ArrayList<Location>();
		private int max = 1;
		private int min = 1;

		private void add(Block block) {
			Integer minI = ConfigStore.<Integer>getBlockConfig(block, "min");
			int min = minI != null ? minI : 1;
			Integer maxI = ConfigStore.<Integer>getBlockConfig(block, "max");
			int max = maxI != null ? maxI : min;
			if (this.min < min)
				this.min = min;
			if (this.max < max)
				this.max = max;
			if (min == 1 && max == 1)
				locs.add(block.getLocation());
		}

		private void set(Material type, Player player) {
			if (this.min > 1 || this.max > 1)
				for (Location loc : locs) {
					ConfigStore.<Integer>setBlockConfig(loc.getBlock(), "min", this.min);
					ConfigStore.<Integer>setBlockConfig(loc.getBlock(), "max", this.max);
					ConfigStore.<String>setBlockConfig(loc.getBlock(), "type", Skill.Mining.active4.getKey());
					player.spawnParticle(Particle.CLOUD, loc, 10, 0.1, 0.1, 0.1);
					player.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f);
					if (Skill.Mining.upgrade_right3.confirmChance(player)) {
						double size = Skill.Mining.upgrade_right3.getEffect(0);
						double count = Skill.Mining.upgrade_right3.getEffect(1);
						a: for (double y = size; y >= -size; y--)
							for (double x = -size; x <= size; x++)
								for (double z = -size; z <= size; z++) {
									Location now = loc.clone().add(x, y, z);
									Material material = now.getBlock().getType();
									if (count <= 0)
										break a;
									else if (material.equals(Material.AIR)
											|| Skill.Mining.active2.canBreak(material) == 1) {
										count -= 1;
										now.getBlock().setType(type, true);
									}
								}
					}
				}
		}
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			player.playSound(player.getEyeLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
			Location base = player.getLocation().getBlock().getLocation();
			HashSet<String> sets = new HashSet<String>();
			HashMapStore.setPreventBreak(player, true);
			boolean transform_left3 = Skill.Mining.transform_left3.hasLearn(player);
			boolean transform_middle3 = Skill.Mining.transform_middle3.hasLearn(player);
			boolean upgrade_middle3_1 = Skill.Mining.upgrade_middle3_1.hasLearn(player);
			boolean upgrade_middle3_2 = Skill.Mining.upgrade_middle3_2.hasLearn(player);
			boolean upgrade_middle2 = Skill.Mining.upgrade_middle2.hasLearn(player);
			boolean upgrade_right3 = Skill.Mining.upgrade_right3.hasLearn(player);
			HashMap<Material, Info> infos = new HashMap<Material, Info>();
			int size = getSize(player);
			List<Location> oreLoc = new ArrayList<Location>();
			for (int y = -size; y <= size; y++)
				for (int x = -size; x <= size; x++)
					for (int z = -size; z <= size; z++) {
						Location now = base.clone().add(x, y, z);
						Block block = now.getBlock();
						Material type = block.getType();
						MaterialInfo materialInfo = getInfo(type);
						if (upgrade_middle3_2 && type.equals(Material.STONE)
								&& Skill.Mining.upgrade_middle3_2.checkChance(player)) {
							type = getRandom();
							block.setType(type, true);
							materialInfo = getInfo(type);
						}
						if (materialInfo != null && Break(transform_left3, player, block, materialInfo.name)) {
							sets.add(materialInfo.name);
							spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, materialInfo.color);
							if (upgrade_middle2)
								oreLoc.add(block.getLocation());
						}
						if (Skill.Mining.active2.canBreak(type) == 2 && upgrade_right3) {
							type = switchDeepslate(type);
							if (!infos.containsKey(type))
								infos.put(type, new Info());
							infos.get(type).add(block);
						}
					}
			String find = "";
			for (String set : sets)
				if (!set.equals(""))
					find += (find.equals("") ? "" : ", ") + set;
			if (find.equals(""))
				player.sendMessage(ChatColor.GREEN + "발견 블럭 : 없음");
			else
				player.sendMessage(ChatColor.GREEN + "발견 블럭 : " + find);
			ItemStack main = player.getInventory().getItemInMainHand().clone();
			if (oreLoc.size() > 0)
				for (Location loc : oreLoc)
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y <= 1; y++)
							for (int z = -1; z <= 1; z++) {
								Block nearblock = loc.clone().add(x, y, z).getBlock();
								Material nearType = nearblock.getType();
								if (Skill.Mining.active2.canBreak(nearType) == 1)
									nearblock.breakNaturally(main);
								else if (nearType.equals(Material.LAVA) || nearType.equals(Material.WATER))
									nearblock.setType(Material.AIR, true);
							}
			for (Material type : infos.keySet())
				infos.get(type).set(type, player);
			HashMapStore.setPreventBreak(player, false);
		}
		return true;
	}

	boolean isSame(Material type1, Material type2) {
		switch (type1) {
		case COAL_ORE:
		case DEEPSLATE_COAL_ORE:
			return type2.equals(Material.COAL_ORE) || type2.equals(Material.DEEPSLATE_COAL_ORE);
		case COPPER_ORE:
		case DEEPSLATE_COPPER_ORE:
			return type2.equals(Material.COPPER_ORE) || type2.equals(Material.DEEPSLATE_COPPER_ORE);
		case DEEPSLATE_DIAMOND_ORE:
		case DIAMOND_ORE:
			return type2.equals(Material.DIAMOND_ORE) || type2.equals(Material.DEEPSLATE_DIAMOND_ORE);
		case DEEPSLATE_EMERALD_ORE:
		case EMERALD_ORE:
			return type2.equals(Material.EMERALD_ORE) || type2.equals(Material.DEEPSLATE_EMERALD_ORE);
		case DEEPSLATE_GOLD_ORE:
		case GOLD_ORE:
		case NETHER_GOLD_ORE:
			return type2.equals(Material.GOLD_ORE) || type2.equals(Material.DEEPSLATE_GOLD_ORE)
					|| type2.equals(Material.NETHER_GOLD_ORE);
		case DEEPSLATE_IRON_ORE:
		case IRON_ORE:
			return type2.equals(Material.IRON_ORE) || type2.equals(Material.DEEPSLATE_IRON_ORE);
		case DEEPSLATE_LAPIS_ORE:
		case LAPIS_ORE:
			return type2.equals(Material.LAPIS_ORE) || type2.equals(Material.DEEPSLATE_LAPIS_ORE);
		case DEEPSLATE_REDSTONE_ORE:
		case REDSTONE_ORE:
			return type2.equals(Material.REDSTONE_ORE) || type2.equals(Material.DEEPSLATE_REDSTONE_ORE);
		case NETHER_QUARTZ_ORE:
			return type2.equals(Material.NETHER_QUARTZ_ORE);
		case ANCIENT_DEBRIS:
			return type2.equals(Material.ANCIENT_DEBRIS);
		default:
			return false;
		}
	}

	public int find(Player player, Location base, Material type) {
		return find(player, base, type, getSize(player));
	}

	public int find(Player player, Location base, Material material, int size) {
		HashSet<String> sets = new HashSet<String>();
		HashMapStore.setPreventBreak(player, true);
		boolean transform_left3 = Skill.Mining.transform_left3.hasLearn(player);
		boolean transform_middle3 = Skill.Mining.transform_middle3.hasLearn(player);
		boolean upgrade_middle3_1 = Skill.Mining.upgrade_middle3_1.hasLearn(player);
		boolean upgrade_middle3_2 = Skill.Mining.upgrade_middle3_2.hasLearn(player);
		boolean upgrade_middle2 = Skill.Mining.upgrade_middle2.hasLearn(player);
		boolean upgrade_right3 = Skill.Mining.upgrade_right3.hasLearn(player);
		HashMap<Material, Info> infos = new HashMap<Material, Info>();
		int i = 0;
		List<Location> oreLoc = new ArrayList<Location>();
		for (int y = -size; y <= size; y++)
			for (int x = -size; x <= size; x++)
				for (int z = -size; z <= size; z++) {
					Location now = base.clone().add(x, y, z);
					Block block = now.getBlock();
					MaterialInfo materialInfo = getInfo(material);
					if (upgrade_middle3_2 && material.equals(Material.STONE)
							&& Skill.Mining.upgrade_middle3_2.checkChance(player)) {
						material = getRandom();
						block.setType(material, true);
						materialInfo = getInfo(material);
					}
					if (isSame(block.getType(), material) && materialInfo != null
							&& Break(transform_left3, player, block, materialInfo.name)) {
						sets.add(materialInfo.name);
						spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, material, materialInfo.color);
						if (upgrade_middle2)
							oreLoc.add(block.getLocation());
						i++;
					}
					if (upgrade_right3) {
						if (!infos.containsKey(material))
							infos.put(material, new Info());
						infos.get(material).add(block);
					}
				}
		String find = "";
		for (String set : sets)
			find += (find.equals("") ? "" : ", ") + set;
		if (find.equals(""))
			player.sendMessage(ChatColor.GREEN + "발견 블럭 : 없음");
		else
			player.sendMessage(ChatColor.GREEN + "발견 블럭 : " + find);
		ItemStack main = player.getInventory().getItemInMainHand().clone();
		if (oreLoc.size() > 0)
			for (Location loc : oreLoc)
				for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						for (int z = -1; z <= 1; z++) {
							Block nearblock = loc.clone().add(x, y, z).getBlock();
							Material nearType = nearblock.getType();
							if (Skill.Mining.active2.canBreak(nearType) == 1)
								nearblock.breakNaturally(main);
							else if (nearType.equals(Material.LAVA) || nearType.equals(Material.WATER))
								nearblock.setType(Material.AIR, true);
						}
		for (Material type : infos.keySet())
			infos.get(type).set(type, player);
		HashMapStore.setPreventBreak(player, false);
		return i;
	}

	public void find(Player player, Location base) {
		HashSet<String> sets = new HashSet<String>();
		HashMapStore.setPreventBreak(player, true);
		boolean transform_left3 = Skill.Mining.transform_left3.hasLearn(player);
		boolean transform_middle3 = Skill.Mining.transform_middle3.hasLearn(player);
		boolean upgrade_middle3_1 = Skill.Mining.upgrade_middle3_1.hasLearn(player);
		boolean upgrade_middle3_2 = Skill.Mining.upgrade_middle3_2.hasLearn(player);
		boolean upgrade_middle2 = Skill.Mining.upgrade_middle2.hasLearn(player);
		boolean upgrade_right3 = Skill.Mining.upgrade_right3.hasLearn(player);
		HashMap<Material, Info> infos = new HashMap<Material, Info>();
		int size = getSize(player);
		List<Location> oreLoc = new ArrayList<Location>();
		for (int y = -size; y <= size; y++)
			for (int x = -size; x <= size; x++)
				for (int z = -size; z <= size; z++) {
					Location now = base.clone().add(x, y, z);
					Block block = now.getBlock();
					Material type = block.getType();
					MaterialInfo materialInfo = getInfo(type);
					if (upgrade_middle3_2 && type.equals(Material.STONE)
							&& Skill.Mining.upgrade_middle3_2.checkChance(player)) {
						type = getRandom();
						block.setType(type, true);
						materialInfo = getInfo(type);
					}
					if (materialInfo != null && Break(transform_left3, player, block, materialInfo.name)) {
						sets.add(materialInfo.name);
						spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, materialInfo.color);
						if (upgrade_middle2)
							oreLoc.add(block.getLocation());
					}
					if (upgrade_right3) {
						if (!infos.containsKey(type))
							infos.put(type, new Info());
						infos.get(type).add(block);
					}
				}
		String find = "";
		for (String set : sets)
			if (!set.equals(""))
				find += (find.equals("") ? "" : ", ") + set;
		if (find.equals(""))
			player.sendMessage(ChatColor.GREEN + "발견 블럭 : 없음");
		else
			player.sendMessage(ChatColor.GREEN + "발견 블럭 : " + find);
		ItemStack main = player.getInventory().getItemInMainHand().clone();
		if (oreLoc.size() > 0)
			for (Location loc : oreLoc)
				for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						for (int z = -1; z <= 1; z++) {
							Block nearblock = loc.clone().add(x, y, z).getBlock();
							Material nearType = nearblock.getType();
							if (Skill.Mining.active2.canBreak(nearType) == 1)
								nearblock.breakNaturally(main);
							else if (nearType.equals(Material.LAVA) || nearType.equals(Material.WATER))
								nearblock.setType(Material.AIR, true);
						}
		HashMapStore.setPreventBreak(player, false);
	}

	boolean Break(boolean transform_left3, Player player, Block block, String name) {
		if (transform_left3 && Skill.Mining.transform_left3.checkChance(player)) {
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				ItemStack main = player.getInventory().getItemInMainHand();
				ItemStack off = player.getInventory().getItemInOffHand();
				player.getInventory().setItemInMainHand(off);
				HashMapStore.setPreventBreak(player, true);
				player.breakBlock(block);
				HashMapStore.setPreventBreak(player, false);
				player.sendMessage(name + ChatColor.WHITE + "을 발굴했습니다.");
				off = player.getInventory().getItemInMainHand();
				player.getInventory().setItemInMainHand(main);
				player.getInventory().setItemInOffHand(off);
			}, 1);
			return false;
		}
		return true;
	}

	void spawnBlockDisplay(boolean transform_middle3, boolean upgrade_middle3_1, Location now, Material type,
			Color color) {
		if (transform_middle3) {
			BlockDisplay display = (BlockDisplay) now.getWorld().spawnEntity(now, EntityType.BLOCK_DISPLAY);
			display.setBlock(type.createBlockData());
			display.setGlowing(true);
			if (upgrade_middle3_1)
				display.setGlowColorOverride(color);
			display.setBillboard(Billboard.FIXED);
			long life = (long) (Skill.Mining.transform_middle3.getEffect() * 1000l);
			ConfigStore.setEntityConfig(display, "type", "Find");
			ConfigStore.setEntityConfig(display, "life", System.currentTimeMillis() + life);
		}
	}
}
