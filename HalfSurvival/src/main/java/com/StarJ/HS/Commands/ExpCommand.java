package com.StarJ.HS.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.StarJ.HS.Systems.SkillType;

public class ExpCommand implements CommandExecutor, TabCompleter {
	// exp [player] [skill_type] [amount]
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			try {
				OfflinePlayer off = null;
				if (args[0].equals("@p")) {
					if (sender instanceof Player)
						off = (Player) sender;
					else if (sender instanceof BlockCommandSender) {
						Location loc = ((BlockCommandSender) sender).getBlock().getLocation();
						for (Entity et : loc.getWorld().getNearbyEntities(loc, 10, 10, 10))
							if (et != sender && et instanceof Player) {
								if (off == null)
									off = (Player) et;
								else if (off.isOnline()
										&& loc.distance(off.getPlayer().getLocation()) > loc.distance(et.getLocation()))
									off = (Player) et;

							}
					}
				} else
					off = Bukkit.getOfflinePlayer(args[0]);
				if (off.isOnline()) {
					SkillType type = SkillType.valueOf(args[1]);
					if (type != null) {
						int amount = Integer.valueOf(args[2]);
						type.addExp(off.getPlayer(), amount > 0 ? amount : 0);
						return true;
					} else
						sender.sendMessage(ChatColor.RED + "잘못된 스킬 종류 입니다.");
				} else
					sender.sendMessage(ChatColor.RED + "해당 플레이어가 접속중이 아닙니다.");
			} catch (NumberFormatException nfe) {
				sender.sendMessage(ChatColor.RED + "경험치양이 숫자가 아닌 다른 값이 입력되었습니다.");
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			for (Player player : Bukkit.getOnlinePlayers())
				if (args[len - 1].equals("") || player.getName().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(player.getName());
		} else if (len == 2)
			for (SkillType type : SkillType.values())
				if (args[len - 1].equals("") || type.name().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(type.name());
		return list;
	}
}
