package com.StarJ.HS.Skills.Farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.SkillType;

public class Active4 extends UsableSkill {

	public Active4(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.farming, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(ChatColor.WHITE + "돌 블럭을 들고 있는 광물로 변형시킵니다.");
		lore.add(ChatColor.WHITE + "25% 확률로 드랍량이 2배로 증가한다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			Block b = player.getTargetBlockExact(5, FluidCollisionMode.NEVER);
			if (b == null || !b.getType().equals(Material.STONE)) {
				setLastUseTime(player, 0);
				HashMapStore.setCooldown(player, this, 0);
				player.sendMessage(ChatColor.RED + "타겟이 된 블럭이 없습니다.");
			} else {
				ItemStack main = player.getInventory().getItemInMainHand();
				Material type = getTransformedBlock(main);
				if (type == null) {
					setLastUseTime(player, 0);
					HashMapStore.setCooldown(player, this, 0);
					player.sendMessage(ChatColor.RED + "변형할 광물을 들어주세요.");
				} else {
					b.setType(type, true);
					main.setAmount(main.getAmount() - 1);
					player.getInventory().setItemInMainHand(main);
					ConfigStore.setBlockConfig(b, "type", getKey());
				}
			}
		}
		return true;
	}

	public void Check(Block block, ItemStack main) {
		if (new Random().nextDouble() < 0.25d) {
			Location now = block.getLocation();
			for (ItemStack item : block.getDrops(main))
				now.getWorld().dropItemNaturally(now, item);
		}
	}

	Material getTransformedBlock(ItemStack main) {
		Material type = main != null ? main.getType() : Material.AIR;
		Random r = new Random();
		switch (type) {
		case COAL:
			return r.nextBoolean() ? Material.COAL_ORE : Material.DEEPSLATE_COAL_ORE;
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
