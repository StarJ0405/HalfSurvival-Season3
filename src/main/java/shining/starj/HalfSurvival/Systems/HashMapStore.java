package shining.starj.HalfSurvival.Systems;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Skills.Minigame;
import shining.starj.HalfSurvival.Skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class HashMapStore {
	private static boolean spanwable = false;
	private static HashMap<UUID, BukkitTask> delay = new HashMap<UUID, BukkitTask>();
	private static HashMap<UUID, HashMap<Skill, BukkitTask>> cooldowns = new HashMap<UUID, HashMap<Skill, BukkitTask>>();
	private static HashMap<UUID, HashMap<Skill, BukkitTask>> durations = new HashMap<UUID, HashMap<Skill, BukkitTask>>();
	private static HashMap<UUID, Minigame> minigames = new HashMap<UUID, Minigame>();
	private static HashSet<UUID> preventBreak = new HashSet<UUID>();
	private static HashMap<UUID, BukkitTask> breaking = new HashMap<UUID, BukkitTask>();
	private static HashMap<UUID, Block> lastBlock = new HashMap<UUID, Block>();
	private static HashMap<UUID, Integer> mining_transform_middle1 = new HashMap<UUID, Integer>();
	private static HashSet<UUID> transform_middle2 = new HashSet<UUID>();
	private static HashMap<UUID, Integer> transform_right1 = new HashMap<UUID, Integer>();
	private static HashMap<UUID, BukkitTask> fish_delay = new HashMap<UUID, BukkitTask>();
	private static HashMap<UUID, FishHook> hook = new HashMap<UUID, FishHook>();
	private static HashMap<UUID, FishHook[]> extra_hooks = new HashMap<UUID, FishHook[]>();
	private static HashMap<UUID, Integer> upgrade_right4_2 = new HashMap<UUID, Integer>();
	private static HashMap<UUID, LocationTask> change_portal = new HashMap<UUID, LocationTask>();
	private static HashMap<UUID, ItemDisplay[]> active2 = new HashMap<UUID, ItemDisplay[]>();
	private static HashMap<UUID, BukkitTask> active4 = new HashMap<UUID, BukkitTask>();
	private static HashMap<UUID, BukkitTask> angryStatus = new HashMap<UUID, BukkitTask>();

	public static boolean isSpanwable() {
		return spanwable;
	}

	public static void setSpanwable(boolean spanwable) {
		HashMapStore.spanwable = spanwable;
	}

	public static boolean isNotDelayed(Player player) {
		UUID uuid = player.getUniqueId();
		return delay.containsKey(uuid) ? delay.get(uuid).isCancelled() : true;
	}

	public static void setDelay(Player player) {
		UUID uuid = player.getUniqueId();
		if (delay.containsKey(uuid))
			delay.get(uuid).cancel();
		delay.put(uuid, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			delay.remove(uuid);
		}, 10));
	}

	public static void resetCooldown(Player player) {
		UUID uuid = player.getUniqueId();
		if (cooldowns.containsKey(uuid)) {
			HashMap<Skill, BukkitTask> tasks = cooldowns.get(uuid);
			for (BukkitTask task : tasks.values())
				task.cancel();
		}
	}

	public static void setCooldown(Player player, Skill skill, long cooldown) {
		UUID uuid = player.getUniqueId();
		if (!cooldowns.containsKey(uuid))
			cooldowns.put(uuid, new HashMap<Skill, BukkitTask>());
		HashMap<Skill, BukkitTask> tasks = cooldowns.get(uuid);
		if (tasks.containsKey(skill))
			tasks.get(skill).cancel();
		if (cooldown <= 0)
			return;
		Advancement adv = Bukkit.getAdvancement(NamespacedKey
				.fromString("halfsurvival:msg/skills/" + skill.getType().name() + "/" + skill.getKey() + "_cooldown"));
		player.getAdvancementProgress(adv).revokeCriteria("trigger");
		tasks.put(skill, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			if (adv != null) {
				player.getAdvancementProgress(adv).awardCriteria("trigger");
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getAdvancementProgress(adv).revokeCriteria("trigger");
				}, 1);
			}
		}, cooldown * 20 / 1000l));
	}

	public static void sendCooldownMessage(Player player, Skill skill) {
		Advancement adv = Bukkit.getAdvancement(NamespacedKey
				.fromString("halfsurvival:msg/skills/" + skill.getType().name() + "/" + skill.getKey() + "_cooldown"));
		if (adv != null) {
			player.getAdvancementProgress(adv).revokeCriteria("trigger");
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				player.getAdvancementProgress(adv).awardCriteria("trigger");
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getAdvancementProgress(adv).revokeCriteria("trigger");
				}, 1);
			}, 1);
		}
	}

	public static void resetDuration(Player player) {
		UUID uuid = player.getUniqueId();
		if (durations.containsKey(uuid)) {
			HashMap<Skill, BukkitTask> tasks = durations.get(uuid);
			for (BukkitTask task : tasks.values())
				task.cancel();
		}
	}

	public static void setDuration(Player player, Skill skill, long duration) {
		UUID uuid = player.getUniqueId();
		if (!durations.containsKey(uuid))
			durations.put(uuid, new HashMap<Skill, BukkitTask>());
		HashMap<Skill, BukkitTask> tasks = durations.get(uuid);
		if (tasks.containsKey(skill))
			tasks.get(skill).cancel();
		if (duration <= 0)
			return;
		Advancement adv = Bukkit.getAdvancement(NamespacedKey
				.fromString("halfsurvival:msg/skills/" + skill.getType().name() + "/" + skill.getKey() + "_duration"));
		player.getAdvancementProgress(adv).revokeCriteria("trigger");
		tasks.put(skill, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			if (adv != null) {
				player.getAdvancementProgress(adv).awardCriteria("trigger");
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getAdvancementProgress(adv).revokeCriteria("trigger");
				}, 1);
			}
		}, duration * 20 / 1000l));
	}

	public static void sendDurationMessage(Player player, Skill skill) {
		Advancement adv = Bukkit.getAdvancement(NamespacedKey
				.fromString("halfsurvival:msg/skills/" + skill.getType().name() + "/" + skill.getKey() + "_duration"));
		if (adv != null) {
			player.getAdvancementProgress(adv).revokeCriteria("trigger");
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				player.getAdvancementProgress(adv).awardCriteria("trigger");
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getAdvancementProgress(adv).revokeCriteria("trigger");
				}, 1);
			}, 1);
		}
	}

	public static Minigame getMinigame(Player player) {
		UUID uuid = player.getUniqueId();
		if (!minigames.containsKey(uuid))
			minigames.put(uuid, new Minigame(player));
		return minigames.get(uuid);
	}

	public static boolean isPreventBreak(Player player) {
		return preventBreak.contains(player.getUniqueId());
	}

	public static void setPreventBreak(Player player, boolean status) {
		if (status)
			preventBreak.add(player.getUniqueId());
		else
			preventBreak.remove(player.getUniqueId());
	}

	public static Block getLastBlock(Player player) {
		UUID uuid = player.getUniqueId();
		return lastBlock.containsKey(uuid) ? lastBlock.get(uuid) : null;
	}

	public static boolean isBreaking(Player player) {
		UUID uuid = player.getUniqueId();
		return breaking.containsKey(uuid) && !breaking.get(uuid).isCancelled();
	}

	public static void setBreaking(Player player, Block block) {
		UUID uuid = player.getUniqueId();
		if (breaking.containsKey(uuid))
			breaking.get(uuid).cancel();
		lastBlock.put(uuid, block);
		breaking.put(uuid, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			breaking.remove(uuid);
			lastBlock.remove(uuid);
		}, 10));
	}

	public static void removeBreaking(Player player) {
		UUID uuid = player.getUniqueId();
		if (breaking.containsKey(uuid))
			breaking.get(uuid).cancel();
		lastBlock.remove(uuid);
		breaking.remove(uuid);
	}

	public static int getMiningTransformMiddle1(Player player) {
		UUID uuid = player.getUniqueId();
		return mining_transform_middle1.containsKey(uuid) ? mining_transform_middle1.get(uuid) : 0;
	}

	public static void setMiningTransformMiddle1(Player player, int stack) {
		UUID uuid = player.getUniqueId();
		if (stack > 0)
			mining_transform_middle1.put(uuid, stack);
		else
			mining_transform_middle1.remove(uuid);
	}

	public static int getMiningTransformRight1(Player player) {
		UUID uuid = player.getUniqueId();
		return transform_right1.containsKey(uuid) ? transform_right1.get(uuid) : 0;
	}

	public static void setMiningTransformRight1(Player player, int stack) {
		UUID uuid = player.getUniqueId();
		if (stack > 0)
			transform_right1.put(uuid, stack);
		else
			transform_right1.remove(uuid);
	}

	public static boolean isTransformMiddle2(Player player) {
		return transform_middle2.contains(player.getUniqueId());
	}

	public static void setTransformMiddle2(Player player, boolean status) {
		UUID uuid = player.getUniqueId();
		if (status)
			transform_middle2.add(uuid);
		else
			transform_middle2.remove(uuid);
	}

	public static boolean isFishDelay(Player player) {
		UUID uuid = player.getUniqueId();
		return fish_delay.containsKey(uuid) ? !fish_delay.get(uuid).isCancelled() : false;
	}

	public static void setFishDelay(Player player) {
		UUID uuid = player.getUniqueId();
		fish_delay.put(uuid, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			BukkitTask task = fish_delay.get(uuid);
			if (task != null)
				task.cancel();
			fish_delay.remove(uuid);
		}, 30));
	}

	public static void removeFishDelay(Player player) {
		UUID uuid = player.getUniqueId();
		if (fish_delay.containsKey(uuid)) {
			fish_delay.get(uuid).cancel();
			fish_delay.remove(uuid);
		}
	}

	public static FishHook getFishHook(Player player) {
		UUID uuid = player.getUniqueId();
		return hook.containsKey(uuid) ? hook.get(uuid) : null;
	}

	public static void setFishHook(Player player, FishHook hook) {
		HashMapStore.hook.put(player.getUniqueId(), hook);
	}

	public static FishHook[] getExtraFishHooks(Player player) {
		UUID uuid = player.getUniqueId();
		if (extra_hooks.containsKey(uuid)) {
			boolean delete = true;
			FishHook[] hooks = extra_hooks.get(uuid);
			for (FishHook hook : hooks) {
				if (hook != null && !hook.isDead())
					delete = false;
			}
			if (delete) {
				extra_hooks.remove(uuid);
				return null;
			} else
				return hooks;
		}
		return null;
	}

	public static void setExtraFishHooks(Player player, FishHook[] hooks) {
		extra_hooks.put(player.getUniqueId(), hooks);
	}

	public static int getBaitStack(Player player) {
		UUID uuid = player.getUniqueId();
		if (upgrade_right4_2.containsKey(uuid))
			return upgrade_right4_2.get(uuid);
		return 0;
	}

	public static void setBaitStack(Player player, int stack) {
		upgrade_right4_2.put(player.getUniqueId(), stack);
	}

	// change portal
	public static LocationTask getChangePortal(Player player) {
		UUID uuid = player.getUniqueId();
		return change_portal.containsKey(uuid) ? change_portal.get(uuid) : null;
	}

	public static void setChangePortal(Player player, LocationTask locationTask) {
		UUID uuid = player.getUniqueId();
		if (change_portal.containsKey(uuid))
			change_portal.get(uuid).cancel();
		change_portal.put(uuid, locationTask);
	}

	public static class LocationTask {
		private final BukkitTask task;
		public final Location loc;

		public LocationTask(BukkitTask task, Location loc) {
			this.task = task;
			this.loc = loc;
		}

		public boolean isCancelled() {
			return task == null || task.isCancelled();
		}

		public void cancel() {
			if (task != null)
				task.cancel();
		}
	}

	// active2
	public static ItemDisplay[] getActive2(Player player) {
		UUID uuid = player.getUniqueId();
		if (active2.containsKey(uuid)) {
			ItemDisplay[] displays = active2.get(uuid);
			for (int i = 0; i < displays.length; i++) {
				ItemDisplay display = displays[i];
				if (display == null || display.isDead())
					displays[i] = null;
			}
			if (displays.length == 1 && displays[0] != null)
				return displays;
			else if (displays.length == 2)
				if (displays[0] != null && displays[1] != null)
					return displays;
				else if (displays[0] != null)
					return new ItemDisplay[] { displays[0] };
				else if (displays[1] != null)
					return new ItemDisplay[] { displays[1] };
		}
		return null;
	}

	public static void setActive2(Player player, ItemDisplay[] itemDisplays) {
		UUID uuid = player.getUniqueId();
		if (active2.containsKey(uuid)) {
			ItemDisplay[] displays = active2.get(uuid);
			for (ItemDisplay display : displays)
				if (display != null && !display.isDead())
					display.remove();
		}
		active2.put(uuid, itemDisplays);
	}

	// active4
	public static boolean isActive4Delay(Player player) {
		UUID uuid = player.getUniqueId();
		return active4.containsKey(uuid) ? !active4.get(uuid).isCancelled() : false;
	}

	public static void setActive4Delay(Player player) {
		UUID uuid = player.getUniqueId();
		if (active4.containsKey(uuid))
			active4.get(uuid).cancel();
		active4.put(uuid, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			active4.remove(uuid);
		}, 10));
	}

	// 분노 상태
	public static boolean isAngryStatus(Player player) {
		UUID uuid = player.getUniqueId();
		return angryStatus.containsKey(uuid) ? !angryStatus.get(uuid).isCancelled() : false;
	}

	public static void setAngryStatus(Player player) {
		UUID uuid = player.getUniqueId();
		if (angryStatus.containsKey(uuid))
			angryStatus.get(uuid).cancel();
		player.sendMessage(ChatColor.DARK_RED + "분노 모드 ON " + ChatColor.GREEN + "피해량 : "
				+ String.format("%.1f", Skill.Hunting.getAngryMultiply(player)) + "배");
		angryStatus.put(uuid, Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			angryStatus.remove(uuid);
			if (player.isOnline())
				player.sendMessage(ChatColor.RED + "분노 모드 OFF");
		}, (long) Skill.Hunting.transform_left2.getEffect(1) * 20));
	}
}