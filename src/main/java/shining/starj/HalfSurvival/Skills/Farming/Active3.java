package shining.starj.HalfSurvival.Skills.Farming;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Active3 extends UsableSkill {

	public Active3(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.farming, key, displayName, preSkills, cooldown, 0, useSlot);
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
		return (int) (5
				+ (Skill.Mining.upgrade_middle3_1.hasLearn(player) ? Skill.Mining.upgrade_middle3_1.getEffect() : 0));
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
			int size = getSize(player);
			for (int y = -size; y <= size; y++)
				for (int x = -size; x <= size; x++)
					for (int z = -size; z <= size; z++) {
						Location now = base.clone().add(x, y, z);
						Block block = now.getBlock();
						Material type = block.getType();
						switch (type) {
						case COAL_ORE:
						case DEEPSLATE_COAL_ORE:
							if (Break(transform_left3, player, block, ChatColor.BLACK + "석탄")) {
								sets.add(ChatColor.BLACK + "석탄");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, Color.BLACK);
							}
							break;
						case COPPER_ORE:
						case DEEPSLATE_COPPER_ORE:
							if (Break(transform_left3, player, block, ChatColor.GOLD + "구리")) {
								sets.add(ChatColor.GOLD + "구리");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(108, 60, 12));
							}
							break;
						case DEEPSLATE_DIAMOND_ORE:
						case DIAMOND_ORE:
							if (Break(transform_left3, player, block, ChatColor.AQUA + "다이아몬드")) {
								sets.add(ChatColor.AQUA + "다이아몬드");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(185, 242, 255));
							}
							break;
						case DEEPSLATE_EMERALD_ORE:
						case EMERALD_ORE:
							if (Break(transform_left3, player, block, ChatColor.GREEN + "에메랄드")) {
								sets.add(ChatColor.GREEN + "에메랄드");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(0, 201, 87));
							}
							break;
						case DEEPSLATE_GOLD_ORE:
						case GOLD_ORE:
						case NETHER_GOLD_ORE:
							if (Break(transform_left3, player, block, ChatColor.YELLOW + "금")) {
								sets.add(ChatColor.YELLOW + "금");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(223, 199, 108));
							}
							break;
						case DEEPSLATE_IRON_ORE:
						case IRON_ORE:
							if (Break(transform_left3, player, block, ChatColor.GRAY + "철")) {
								sets.add(ChatColor.GRAY + "철");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(161, 157, 148));
							}
							break;
						case DEEPSLATE_LAPIS_ORE:
						case LAPIS_ORE:
							if (Break(transform_left3, player, block, ChatColor.BLUE + "청금석")) {
								sets.add(ChatColor.BLUE + "청금석");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, Color.BLUE);
							}
							break;
						case DEEPSLATE_REDSTONE_ORE:
						case REDSTONE_ORE:
							if (Break(transform_left3, player, block, ChatColor.RED + "레드스톤")) {
								sets.add(ChatColor.RED + "레드스톤");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, Color.RED);
							}
							break;
						case NETHER_QUARTZ_ORE:
							if (Break(transform_left3, player, block, ChatColor.WHITE + "네더 석영")) {
								sets.add(ChatColor.WHITE + "네더 석영");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type, Color.WHITE);
							}
							break;
						case ANCIENT_DEBRIS:
							if (Break(transform_left3, player, block, ChatColor.DARK_PURPLE + "고대 잔해")) {
								sets.add(ChatColor.DARK_PURPLE + "고대 잔해");
								spawnBlockDisplay(transform_middle3, upgrade_middle3_1, now, type,
										Color.fromRGB(89, 70, 178));
							}
							break;
						default:
							break;
						}
					}
			String find = "";
			for (String set : sets)
				find += (find.equals("") ? "" : ", ") + set;
			if (find.equals(""))
				player.sendMessage(ChatColor.GREEN + "발견 블럭 : 없음");
			else
				player.sendMessage(ChatColor.GREEN + "발견 블럭 : " + find);
			HashMapStore.setPreventBreak(player, false);
		}
		return true;
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
			BlockDisplay bd = (BlockDisplay) now.getWorld().spawnEntity(now, EntityType.BLOCK_DISPLAY);
			bd.setBlock(type.createBlockData());
			bd.setGlowing(true);
			if (upgrade_middle3_1)
				bd.setGlowColorOverride(color);
			bd.setBillboard(Billboard.FIXED);
			long life = (long) (Skill.Mining.transform_middle3.getEffect() * 1000l);
			ConfigStore.setEntityConfig(bd, "type", "Find");
			ConfigStore.setEntityConfig(bd, "life", System.currentTimeMillis() + life);
		}
	}
}
