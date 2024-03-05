package shining.starj.HalfSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SudoCommand implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 1) {
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
				Player player = off.getPlayer();
				boolean op = player.isOp();
				String command = "";
				for (int i = 1; i < args.length; i++)
					command += args[i] + " ";
				player.setOp(true);
				player.performCommand(command);
				player.setOp(op);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1)
			for (Player player : Bukkit.getOnlinePlayers())
				if (args[len - 1].equals("") || player.getName().toUpperCase().startsWith(args[len - 1].toUpperCase()))
					list.add(player.getName());
		return list;
	}

}
