package shining.starj.HalfSurvival.Commands;

import shining.starj.HalfSurvival.Entities.Entities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
	// spawn [type] [world] [x] [y] [z] [yaw] [pitch]
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 5)
			try {
				Entities entities = Entities.valueOf(args[0]);
				World world = args[1].equals("~") && sender instanceof Player
						? ((Player) sender).getLocation().getWorld()
						: Bukkit.getWorld(args[1]);
				if (world == null) {
					sender.sendMessage(ChatColor.RED + "존재하지 않는 월드입니다.");
					return true;
				}
				double x = args[2].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getBlockX()
						: Double.parseDouble(args[2]);
				double y = args[3].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getBlockY()
						: Double.parseDouble(args[3]);
				double z = args[4].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getBlockZ()
						: Double.parseDouble(args[4]);
				float yaw = 0f;
				float pitch = 0f;
				if (args.length >= 7) {
					yaw = args[5].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getYaw()
							: Float.parseFloat(args[5]);
					pitch = args[6].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getPitch()
							: Float.parseFloat(args[6]);
				}
				entities.spawnEntity(new Location(world, x, y, z, yaw, pitch));
				return true;
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "숫자가 입력되어야하는 곳에 다른 값이 입력되었습니다.");
			}
		else if (args.length == 1 && sender instanceof Player)
			try {
				Entities entities = Entities.valueOf(args[0]);
				Player player = (Player) sender;
				entities.spawnEntity(player.getLocation());
				return true;
			} catch (Exception ex) {
			}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			for (Entities entities : Entities.values())
				if (args[len - 1].equals("") || entities.getKey().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(entities.getKey());
		} else if (len == 2) {
			for (World world : Bukkit.getWorlds())
				if (args[len - 1].equals("") || world.getName().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(world.getName());
			list.add("~");
		} else if (sender instanceof Player) {
			Location loc = ((Player) sender).getLocation();
			if (len == 3) {
				if (args[len - 1].equals("") || (loc.getBlockX() + "").startsWith(args[len - 1]))
					list.add(loc.getBlockX() + "");
				list.add("~");
			} else if (len == 4) {
				if (args[len - 1].equals("") || (loc.getBlockY() + "").startsWith(args[len - 1]))
					list.add(loc.getBlockY() + "");
				list.add("~");
			} else if (len == 5) {
				if (args[len - 1].equals("") || (loc.getBlockZ() + "").startsWith(args[len - 1]))
					list.add(loc.getBlockZ() + "");
				list.add("~");
			} else if (len == 6) {
				if (args[len - 1].equals("") || (loc.getYaw() + "").startsWith(args[len - 1]))
					list.add(loc.getYaw() + "");
				list.add("~");
			} else if (len == 7) {
				if (args[len - 1].equals("") || (loc.getPitch() + "").startsWith(args[len - 1]))
					list.add(loc.getPitch() + "");
				list.add("~");
			}
		}
		return list;
	}
}
