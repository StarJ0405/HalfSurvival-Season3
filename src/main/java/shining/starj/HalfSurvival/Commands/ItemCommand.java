package shining.starj.HalfSurvival.Commands;

import shining.starj.HalfSurvival.Items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemCommand implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 2) {
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
			if (off != null && off.isOnline()) {
				int count = 1;
				if (args.length >= 3)
					try {
						count = Integer.valueOf(args[2]);
					} catch (NumberFormatException nfe) {

					}
				Player player = off.getPlayer();
				Items i = Items.valueOf(args[1]);
				if (i != null) {
					ItemStack item = i.getItemStack(player);
					item.setAmount(count);
					player.getInventory().addItem(item);
					sender.sendMessage(ChatColor.GREEN + "명령어를 통해서 " + i.getDisplayName() + ChatColor.GREEN + "을 "
							+ item.getAmount() + "개 획득했습니다.");
					return true;
				} else
					sender.sendMessage(ChatColor.RED + "없는 아이템입니다.");
			} else
				sender.sendMessage(ChatColor.RED + "플레이어가 접속중이 아닙니다.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int l = args.length - 1;
		if (l == 0) {
			for (Player player : Bukkit.getOnlinePlayers())
				if (args[l].equals("") || player.getName().toLowerCase().contains(args[l].toLowerCase()))
					list.add(player.getName());
		} else if (l == 1) {
			for (Items i : Items.values())
				if (args[l].equals("") || i.getKey().toLowerCase().contains(args[l].toLowerCase()))
					list.add(i.getKey());
		}
		return list;
	}
}
