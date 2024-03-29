package shining.starj.HalfSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0 && args[0].equals("bed") )
				player.teleport(player.getBedLocation());
			else if (args.length > 0 && Bukkit.getWorld(args[0]) != null)
				player.teleport(Objects.requireNonNull(Bukkit.getWorld(args[0])).getSpawnLocation());
			else
				player.teleport(player.getWorld().getSpawnLocation());
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			if (args[len - 1].isEmpty() || "bed".startsWith(args[len - 1].toLowerCase()))
				list.add("bed");
			for (World world : Bukkit.getWorlds())
				if (args[len - 1].isEmpty() || world.getName().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(world.getName());
		}
		return list;
	}
}
