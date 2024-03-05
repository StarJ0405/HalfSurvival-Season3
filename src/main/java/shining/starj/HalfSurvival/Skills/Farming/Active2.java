package shining.starj.HalfSurvival.Skills.Farming;

import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Active2 extends UsableSkill {

	public Active2(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.farming, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(ChatColor.WHITE + "바라보는 불럭을 기준으로 3x3x3 범위 대부분의 블럭을 캡니다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			Block b = player.getTargetBlockExact(5, FluidCollisionMode.NEVER);
			if (b == null || b.getType().equals(Material.AIR)) {
				setLastUseTime(player, 0);
				HashMapStore.setCooldown(player, this, 0);
				player.sendMessage(ChatColor.RED + "타겟이 된 블럭이 없습니다.");
			} else {
				boolean preventDurability = Skill.Mining.upgrade_left2_2.confirmChance(player);
				ItemStack main = player.getInventory().getItemInMainHand();
				ItemStack off = player.getInventory().getItemInOffHand();
				if (preventDurability)
					player.getInventory().setItemInMainHand(off.clone());
				else
					player.getInventory().setItemInMainHand(off);
				HashMapStore.setPreventBreak(player, true);
				act(b, player);
				HashMapStore.setPreventBreak(player, false);
				if (!preventDurability)
					off = player.getInventory().getItemInMainHand();
				player.getInventory().setItemInMainHand(main);
				player.getInventory().setItemInOffHand(off);
			}
		}
		return true;
	}

	public void act(Block b, Player player) {
		List<Material> list = new ArrayList<Material>();
		boolean upgrade_left4 = Skill.Mining.upgrade_left4.hasLearn(player);
		Location loc = b.getLocation();
		List<Block> blocks = player.getLastTwoTargetBlocks(null, 10);
		int modX = 0;
		int modY = 0;
		int modZ = 0;
		if (blocks.size() > 1) {
			BlockFace face = blocks.get(1).getFace(blocks.get(0));
			modX = -face.getModX();
			modY = -face.getModY();
			modZ = -face.getModZ();
		}

		if (Skill.Mining.upgrade_left2_1.confirmChance(player)) {
			for (int i = 0; i < Skill.Mining.upgrade_left2_1.getEffect(); i++) {
				for (int y = -1 + modY * (1 + i * 3); y <= 1 + modY * (1 + i * 3); y++)
					for (int x = -1 + modX * (1 + i * 3); x <= 1 + modX * (1 + i * 3); x++)
						for (int z = -1 + modZ * (1 + i * 3); z <= 1 + modZ * (1 + i * 3); z++) {
							Location now = loc.clone().add(x, y, z);
							Material type = now.getBlock().getType();
							int canBreak = canBreak(type);
							if (canBreak > 0) {
								player.breakBlock(now.getBlock());
								player.playSound(now, Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 1f);
								player.spawnParticle(Particle.BLOCK_DUST, now, 10, 0.1, 0.1, 0.1, 0.1,
										type.createBlockData());
								if (upgrade_left4 && canBreak == 2)
									list.add(type);
							}
						}
			}
		} else
			for (int y = -1 + modY; y <= 1 + modY; y++)
				for (int x = -1 + modX; x <= 1 + modX; x++)
					for (int z = -1 + modZ; z <= 1 + modZ; z++) {
						Location now = loc.clone().add(x, y, z);
						Material type = now.getBlock().getType();
						int canBreak = canBreak(type);
						if (canBreak > 0) {
							player.breakBlock(now.getBlock());
							player.playSound(now, Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 1f);
							player.spawnParticle(Particle.BLOCK_DUST, now, 10, 0.1, 0.1, 0.1, 0.1,
									type.createBlockData());
							if (upgrade_left4 && canBreak == 2)
								list.add(type);
						}
					}
		if (upgrade_left4 && list.size() > 0) {
			long cooldown = Skill.Mining.active1.getRemainUseTime(player);
			if (cooldown > 0) {
				cooldown -= (long) (Skill.Mining.active4.getCooldown(player)
						* (1 - list.size() * Skill.Mining.upgrade_left4.getEffect()));
				if (cooldown > 0) {
					Skill.Mining.active4.setLastUseTime(player, cooldown / 1000d);
					HashMapStore.setCooldown(player, Skill.Mining.active4, cooldown);
				} else {
					Skill.Mining.active4.setLastUseTime(player, 0);
					HashMapStore.setCooldown(player, Skill.Mining.active4, 0);
					HashMapStore.sendCooldownMessage(player, Skill.Mining.active4);
				}
			}
		}
	}

	public int canBreak(Material type) {
		switch (type) {
		case STONE:
		case COBBLESTONE:
		case MOSSY_COBBLESTONE:
		case SMOOTH_STONE:
		case STONE_BRICKS:
		case DIORITE:
		case POLISHED_DIORITE:
		case ANDESITE:
		case POLISHED_ANDESITE:
		case DEEPSLATE:
		case COBBLED_DEEPSLATE:
		case PACKED_MUD:
		case SANDSTONE:
		case CHISELED_SANDSTONE:
		case SMOOTH_SANDSTONE:
		case RED_SANDSTONE:
		case CHISELED_RED_SANDSTONE:
		case SMOOTH_RED_SANDSTONE:
		case SEA_LANTERN:
		case PRISMARINE:
		case PRISMARINE_BRICKS:
		case DARK_PRISMARINE:
		case NETHERRACK:
		case NETHER_BRICK:
		case RED_NETHER_BRICKS:
		case BASALT:
		case SMOOTH_BASALT:
		case POLISHED_BASALT:
		case BLACKSTONE:
		case GILDED_BLACKSTONE:
		case END_STONE:
		case PURPUR_BLOCK:
		case COAL_BLOCK:
		case IRON_BLOCK:
		case GOLD_BLOCK:
		case REDSTONE_BLOCK:
		case EMERALD_BLOCK:
		case LAPIS_BLOCK:
		case DIAMOND_BLOCK:
		case NETHERITE_BLOCK:
		case QUARTZ_BLOCK:
		case AMETHYST_BLOCK:
		case COPPER_BLOCK:
		case DIRT:
		case COARSE_DIRT:
		case ROOTED_DIRT:
		case MUD:
		case CLAY:
		case GRAVEL:
		case SAND:
		case RED_SAND:
		case MOSS_BLOCK:
		case MOSS_CARPET:
		case GRANITE:
		case CALCITE:
		case TUFF:
		case DRIPSTONE_BLOCK:
		case POINTED_DRIPSTONE:
		case MAGMA_BLOCK:
		case OBSIDIAN:
		case CRYING_OBSIDIAN:
		case CRIMSON_NYLIUM:
		case WARPED_NYLIUM:
		case SOUL_SAND:
		case SOUL_SOIL:
		case BONE_BLOCK:
		case ANCIENT_DEBRIS:
		case RAW_COPPER_BLOCK:
		case RAW_GOLD_BLOCK:
		case RAW_IRON_BLOCK:
		case GLOWSTONE:
		case BUDDING_AMETHYST:
		case LARGE_AMETHYST_BUD:
		case MEDIUM_AMETHYST_BUD:
		case SMALL_AMETHYST_BUD:
		case SCULK:
		case SCULK_VEIN:
			return 1;
		case COAL_ORE:
		case DEEPSLATE_COAL_ORE:
		case IRON_ORE:
		case DEEPSLATE_IRON_ORE:
		case COPPER_ORE:
		case DEEPSLATE_COPPER_ORE:
		case GOLD_ORE:
		case DEEPSLATE_GOLD_ORE:
		case REDSTONE_ORE:
		case DEEPSLATE_REDSTONE_ORE:
		case EMERALD_ORE:
		case DEEPSLATE_EMERALD_ORE:
		case LAPIS_ORE:
		case DEEPSLATE_LAPIS_ORE:
		case DIAMOND_ORE:
		case DEEPSLATE_DIAMOND_ORE:
		case NETHER_GOLD_ORE:
		case NETHER_QUARTZ_ORE:
			return 2;
		default:
			return 0;
		}
	}
}
