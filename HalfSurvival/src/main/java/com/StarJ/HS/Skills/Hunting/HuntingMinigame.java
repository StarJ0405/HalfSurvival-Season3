package com.StarJ.HS.Skills.Hunting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.StarJ.HS.Core;

public class HuntingMinigame {
	private final Player player;
	private BukkitTask task;
	private int time;
	private final int tick;
	private final int maxTime;
	private final int size;
	private int chance;
	private Boolean[] correct;
	private Boolean[] answer;
	private int now;
	private Material type;

	public HuntingMinigame(Player player) {
		this.size = 10;
		this.chance = 0;
		this.correct = new Boolean[size];
		this.answer = new Boolean[size];
		this.now = 0;
		this.type = null;
		this.player = player;
		this.task = null;
		this.time = 0;
		this.tick = 4;
		this.maxTime = 20 * 20;
	}

	public void start(Material type) {
		if (task != null)
			task.cancel();
		this.type = type;
		this.now = 0;
		this.time = 0;
		Random r = new Random();
		this.chance = 1 + r.nextInt(3);
		for (int i = 0; i < size; i++) {
			this.correct[i] = i < this.chance;
			this.answer[i] = false;
		}
		List<Boolean> list = Arrays.asList(this.correct);
		Collections.shuffle(list);
		this.correct = list.toArray(Boolean[]::new);
		task = Bukkit.getScheduler().runTaskTimer(Core.getCore(), () -> {
			this.time++;
			if (this.time > maxTime) {
				task.cancel();
				task = null;
				int correct = 0;
				for (int i = 0; i < this.size; i++)
					if (this.correct[i] && this.correct[i] == this.answer[i])
						correct++;
				if (type != null && correct > 0) {
					ItemStack item = new ItemStack(type, correct);
					Item drop = player.getWorld().dropItem(player.getEyeLocation(), item);
					drop.setOwner(player.getUniqueId());
				}
				player.sendTitle("실패", "", 0, 20, 0);
				return;
			}
			if (this.time % (tick * 2) == 0) {
				this.now++;
				if (this.now >= this.size)
					this.now = 0;
			}
			// ◇◆
			String msg = "";
			for (int i = 0; i < this.size; i++)
				msg += (i == this.now ? ChatColor.GREEN : ChatColor.WHITE)
						+ (this.correct[i] ? (this.answer[i] ? "◆" : "◇") : "■");
			player.sendTitle(msg,
					"남은 기회 : " + this.chance + ChatColor.GRAY + "(" + (this.maxTime - this.time) / 20 + "s)", 0, 20, 0);
		}, 0, 1);
	}

	public void Sneak() {
		if (task != null && !task.isCancelled()) {
			this.answer[now] = true;
			this.chance--;
			if (this.chance <= 0) {
				task.cancel();
				task = null;
				int correct = 0;
				for (int i = 0; i < this.size; i++)
					if (this.correct[i] && this.correct[i] == this.answer[i])
						correct++;
				if (type != null && correct > 0) {
					ItemStack item = new ItemStack(type, correct);
					Item drop = player.getWorld().dropItem(player.getEyeLocation(), item);
					drop.setOwner(player.getUniqueId());
				}
				String msg = "";
				for (int i = 0; i < this.size; i++)
					msg += (i == this.now ? ChatColor.GREEN : ChatColor.WHITE)
							+ (this.correct[i] ? (this.answer[i] ? "◆" : "◇") : "■");
				player.sendTitle(msg, "종료!", 0, 20, 0);
			}
		}
	}

	public boolean isRunning() {
		return task != null && !task.isCancelled();
	}
}
