package shining.starj.HalfSurvival.Skills.Fishing;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class FishingMinigame {
	private final Player player;
	private BukkitTask task;
	private int time;
	private final int tick;
	private final int maxTime;
	private final int size;
	private final int down;
	private final int up;
	private int now;
	private ItemStack fish;
	private int fishLoc;

	public FishingMinigame(Player player) {
		this.player = player;
		this.task = null;
		this.time = 0;
		this.tick = 6;
		this.maxTime = 10 * 20;
		this.size = 12;
		this.up = 2;
		this.down = 1;
		this.now = 0;
		this.fish = null;
		this.fishLoc = 0;
	}

	public void start(ItemStack item) {
		if (task != null)
			task.cancel();
		this.fish = item;
		this.now = 0;
		this.time = 0;
		this.fishLoc = size / 2;
		Random r = new Random();
		task = Bukkit.getScheduler().runTaskTimer(Core.getCore(), () -> {
			this.time++;
			if (this.time > this.maxTime) {
				this.task.cancel();
				this.task = null;
				if (this.fish != null) {
					String msg = "";
					for (int i = 0; i < this.size; i++)
						msg += (i >= this.now - this.down && i <= this.now + this.up ? ChatColor.GREEN
								: ChatColor.WHITE) + (i == fishLoc ? "🐟" : "🌊");
					if (this.fishLoc >= this.now - this.down && this.fishLoc <= this.now + this.up) {
						fish = fish.clone();
						fish.setAmount((int) Skill.Fishing.minigame.getEffect());
						Item drop = this.player.getWorld().dropItem(this.player.getEyeLocation(), fish);
						drop.setOwner(this.player.getUniqueId());
						player.sendTitle(msg, ChatColor.GREEN + "성공!", 0, 20, 0);
					} else
						player.sendTitle(msg, ChatColor.RED + "실패!", 0, 20, 0);
				} else
					player.sendTitle(ChatColor.RED + "오류 발생", "", 0, 20, 0);
				return;
			}
			if (this.time % tick == 0) {
				this.fishLoc += -1 + r.nextInt(3);
				if (this.fishLoc < 1)
					this.fishLoc = 1;
				else if (this.fishLoc >= this.size - 1)
					this.fishLoc = this.size - 2;
				this.now++;
				if (this.now >= this.size - this.up)
					this.now = this.size - this.up - 1;
			}
			// 🐟 🌊
			String msg = "";
			for (int i = 0; i < this.size; i++)
				msg += (i >= this.now - this.down && i <= this.now + this.up ? ChatColor.GREEN : ChatColor.WHITE)
						+ (i == fishLoc ? "🐟" : "🌊");
			this.player.sendTitle(msg, ChatColor.GRAY + "남은 시간 : " + (this.maxTime - this.time) / 20 + "s", 0, 20, 0);
		}, 0, 1);
	}

	public void Sneak() {
		if (task != null && !task.isCancelled()) {
			this.now-=2;
			if (this.now < this.down)
				this.now = this.down;
			String msg = "";
			for (int i = 0; i < this.size; i++)
				msg += (i >= this.now - 1 && i <= this.now + 1 ? ChatColor.GREEN : ChatColor.WHITE)
						+ (i == fishLoc ? "🐟" : "🌊");
			this.player.sendTitle(msg, ChatColor.GRAY + "남은 시간 : " + (this.maxTime - this.time) / 20 + "s", 0, 20, 0);
		}
	}

	public boolean isRunning() {
		return task != null && !task.isCancelled();
	}
}
