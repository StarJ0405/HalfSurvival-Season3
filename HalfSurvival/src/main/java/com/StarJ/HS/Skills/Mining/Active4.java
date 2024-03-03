package com.StarJ.HS.Skills.Mining;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.SkillType;

public class Active4 extends UsableSkill {

	public Active4(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.mining, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(ChatColor.WHITE + "돌/심층암/응회암 블럭을 랜덤 광물로 변형시킵니다.");
		if (Skill.Mining.transform_right4.hasLearn(player))
			lore.add(ChatColor.WHITE + "광물을 들고 있는 경우 해당 광물로 변형시킵니다.");
		if (Skill.Mining.upgrade_middle4.hasLearn(player))
			lore.add(ChatColor.WHITE + "100% 확률로 추가 드랍이 발생합니다.");
		else
			lore.add(ChatColor.WHITE + "25% 확률로 +1번 더 드랍합니다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			Block b = player.getTargetBlockExact(5, FluidCollisionMode.NEVER);
			if (b == null || !(b.getType().equals(Material.STONE) || b.getType().equals(Material.DEEPSLATE)
					|| b.getType().equals(Material.TUFF))) {
				setLastUseTime(player, 0);
				HashMapStore.setCooldown(player, this, 0);
				player.sendMessage(ChatColor.RED + "돌/심층암/응회함 중 하나를 대상으로 해야합니다");
			} else
				act(player, b);
		}
		return true;
	}

	public void act(Player player, Block b) {
		int size = 0;
		boolean transform_right4 = Skill.Mining.transform_right4.confirmChance(player);
		boolean transform_middle4 = Skill.Mining.transform_middle4.confirmChance(player);
		boolean upgrade_middle4 = Skill.Mining.upgrade_middle4.confirmChance(player);
		int modX = 0;
		int modY = 0;
		int modZ = 0;
		player.spawnParticle(Particle.DRAGON_BREATH, b.getLocation(), 100, 0.1, 0.1, 0.1);
		player.playSound(b.getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 1f, 1f);
		if (Skill.Mining.upgrade_right4_1.confirmChance(player)) {
			size = (int) Skill.Mining.upgrade_right4_1.getEffect();
			List<Block> blocks = player.getLastTwoTargetBlocks(null, 10);
			if (blocks.size() > 1) {
				BlockFace face = blocks.get(1).getFace(blocks.get(0));
				modX = -face.getModX();
				modY = -face.getModY();
				modZ = -face.getModZ();
			}
		}
		for (int y = -size + modY; y <= size + modY; y++)
			for (int x = -size + modX; x <= size + modX; x++)
				for (int z = -size + modZ; z <= size + modZ; z++)
					if ((x == 0 && y == 0 && z == 0) || Skill.Mining.upgrade_right4_1.checkChance(player, 1)) {
						Block now = b.getLocation().clone().add(x, y, z).getBlock();
						if (now != null && (now.getType().equals(Material.STONE)
								|| now.getType().equals(Material.DEEPSLATE) || now.getType().equals(Material.TUFF)))
							if (transform_right4) {
								ItemStack main = player.getInventory().getItemInMainHand();
								Material type = getTransformedBlock(main);
								if (type == null) {
									// setLastUseTime(player, 0);
									// HashMapStore.setCooldown(player, this, 0);
									// player.sendMessage(ChatColor.RED + "변형할 광물을 들어주세요.");
									type = getRandomBlock();
									now.setType(type, true);
									ConfigStore.<String>setBlockConfig(now, "type", getKey());
									ConfigStore.<Integer>setBlockConfig(now, "min", 1);
									if (transform_middle4) {
										int max = Skill.Mining.active3.find(player, now.getLocation(), type);
										if (upgrade_middle4) {
											ConfigStore.<Integer>setBlockConfig(now, "max", max > 5 ? 5 : max);
											Bukkit.broadcastMessage(ChatColor.GREEN + "발견 수 : " + max);
										} else
											Bukkit.broadcastMessage(ChatColor.RED + "미발견");
									} else
										ConfigStore.<Integer>setBlockConfig(now, "max", 2);
								} else {
									now.setType(type, true);
									main.setAmount(main.getAmount() - 1);
									player.getInventory().setItemInMainHand(main);
									ConfigStore.<String>setBlockConfig(now, "type", getKey());
									ConfigStore.<Integer>setBlockConfig(now, "min", 1);
									if (transform_middle4) {
										int max = Skill.Mining.active3.find(player, now.getLocation(), type);
										if (upgrade_middle4) {
											ConfigStore.<Integer>setBlockConfig(now, "max", max > 5 ? 5 : max);
											Bukkit.broadcastMessage(ChatColor.GREEN + "발견 수 : " + max);
										} else
											Bukkit.broadcastMessage(ChatColor.RED + "미발견");
									} else
										ConfigStore.<Integer>setBlockConfig(now, "max", 2);
								}
							} else {
								Material type = getRandomBlock();
								now.setType(type, true);
								ConfigStore.<String>setBlockConfig(now, "type", getKey());
								ConfigStore.<Integer>setBlockConfig(now, "min", 1);
								if (transform_middle4) {
									int max = Skill.Mining.active3.find(player, now.getLocation(), type);
									if (upgrade_middle4) {
										ConfigStore.<Integer>setBlockConfig(now, "max", max > 5 ? 5 : max);
										Bukkit.broadcastMessage(ChatColor.GREEN + "발견 수 : " + max);
									} else
										Bukkit.broadcastMessage(ChatColor.RED + "미발견");
								} else
									ConfigStore.<Integer>setBlockConfig(now, "max", 2);
							}
					}
	}

	public void Check(Player player, Block block, ItemStack main) {
		if (new Random().nextDouble() < 0.25d || Skill.Mining.upgrade_middle4.hasLearn(player)) {
			Location now = block.getLocation();
			Integer minI = ConfigStore.<Integer>getBlockConfig(block, "min");
			int min = minI != null ? minI : 1;
			Integer maxI = ConfigStore.<Integer>getBlockConfig(block, "max");
			int max = maxI != null ? maxI : min;
			if (max <= min)
				max = min + 1;
			int amount = min + new Random().nextInt(max - min + 1);
			// Bukkit.broadcastMessage(min + " ~ " + max + " = " + amount);
			for (int i = 0; i < amount; i++)
				for (ItemStack item : block.getDrops(main))
					now.getWorld().dropItemNaturally(now, item);
		}
	}

	public Material getRandomBlock() {
		Random r = new Random();
		double now = r.nextDouble();
		if (now < 0.453d)
			return r.nextBoolean() ? Material.COAL_ORE : Material.DEEPSLATE_COAL_ORE;
		else if (now < 0.453d + 0.125d)
			return r.nextBoolean() ? Material.COPPER_ORE : Material.DEEPSLATE_COPPER_ORE;
		else if (now < 0.453d + 0.125d + 0.125d)
			return r.nextBoolean() ? Material.DEEPSLATE_IRON_ORE : Material.IRON_ORE;
		else if (now < 0.453d + 0.125d + 0.125d + 0.125d) {
			Material[] types = new Material[] { Material.DEEPSLATE_GOLD_ORE, Material.GOLD_ORE,
					Material.NETHER_GOLD_ORE };
			return types[r.nextInt(types.length)];
		} else if (now < 0.453d + 0.125d + 0.125d + 0.125d + 0.063d)
			return r.nextBoolean() ? Material.DEEPSLATE_REDSTONE_ORE : Material.REDSTONE_ORE;
		else if (now < 0.453d + 0.125d + 0.125d + 0.125d + 0.063d + 0.063d)
			return r.nextBoolean() ? Material.DEEPSLATE_LAPIS_ORE : Material.LAPIS_ORE;
		else if (now < 0.453d + 0.125d + 0.125d + 0.125d + 0.063d + 0.063d + 0.031d)
			return r.nextBoolean() ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
		else
			return r.nextBoolean() ? Material.DEEPSLATE_EMERALD_ORE : Material.EMERALD_ORE;
	}

	Material getTransformedBlock(ItemStack main) {
		Material type = main != null ? main.getType() : Material.AIR;
		Random r = new Random();
		switch (type) {
		case COAL:
			return r.nextBoolean() ? Material.COAL_ORE : Material.DEEPSLATE_COAL_ORE;
		case RAW_COPPER:
		case COPPER_INGOT:
			return r.nextBoolean() ? Material.COPPER_ORE : Material.DEEPSLATE_COPPER_ORE;
		case DIAMOND:
			return r.nextBoolean() ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
		case EMERALD:
			return r.nextBoolean() ? Material.DEEPSLATE_EMERALD_ORE : Material.EMERALD_ORE;
		case RAW_GOLD:
		case GOLD_INGOT:
			Material[] types = new Material[] { Material.DEEPSLATE_GOLD_ORE, Material.GOLD_ORE,
					Material.NETHER_GOLD_ORE };
			return types[r.nextInt(types.length)];
		case RAW_IRON:
		case IRON_INGOT:
			return r.nextBoolean() ? Material.DEEPSLATE_IRON_ORE : Material.IRON_ORE;
		case LAPIS_LAZULI:
			return r.nextBoolean() ? Material.DEEPSLATE_LAPIS_ORE : Material.LAPIS_ORE;
		case REDSTONE:
			return r.nextBoolean() ? Material.DEEPSLATE_REDSTONE_ORE : Material.REDSTONE_ORE;
		case QUARTZ:
			return Material.NETHER_QUARTZ_ORE;
		case NETHERITE_INGOT:
			return Material.ANCIENT_DEBRIS;
		default:
			return null;
		}
	}
}
