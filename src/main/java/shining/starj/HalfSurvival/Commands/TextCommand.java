package shining.starj.HalfSurvival.Commands;

import shining.starj.HalfSurvival.Systems.ConfigStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.List;

public class TextCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 5) {
			try {
				World world = Bukkit.getWorld(args[0]);
				if (world != null) {
					double x = args[1].equals("~") && sender instanceof Player
							? ((Player) sender).getLocation().getBlockX()
							: Double.parseDouble(args[1]);
					double y = args[2].equals("~") && sender instanceof Player
							? ((Player) sender).getLocation().getBlockY()
							: Double.parseDouble(args[2]);
					double z = args[3].equals("~") && sender instanceof Player
							? ((Player) sender).getLocation().getBlockZ()
							: Double.parseDouble(args[3]);
					float size = Float.parseFloat(args[4]);
					String msg = "";
					for (int i = 5; i < args.length; i++)
						msg += (msg.equals("") ? "" : " ") + args[i];
					msg = ChatColor.translateAlternateColorCodes('$', msg);
					TextDisplay textDisplay = (TextDisplay) world.spawnEntity(new Location(world, x, y, z),
							EntityType.TEXT_DISPLAY);
					textDisplay.setText(msg);
					textDisplay.setAlignment(TextAlignment.CENTER);
					textDisplay.setBillboard(Billboard.VERTICAL);
					Transformation transformation = textDisplay.getTransformation();
					transformation.getScale().mul(size);
					textDisplay.setTransformation(transformation);
					ConfigStore.setEntityConfig(textDisplay, "type", "text");
					return true;
				} else
					sender.sendMessage(ChatColor.RED + "잘못된 월드입니다.");
			} catch (NumberFormatException nfe) {
				sender.sendMessage(ChatColor.RED + "잘못된 좌표값이 입력되었습니다.");
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			for (World world : Bukkit.getWorlds())
				if (args[len - 1].equals("") || world.getName().toLowerCase().contains(args[len - 1].toLowerCase()))
					list.add(world.getName());
		} else if (sender instanceof Player) {
			Player player = (Player) sender;
			if (len == 2) {
				if (args[len - 1].equals("") || (player.getLocation().getBlockX() + "").startsWith(args[len - 1]))
					list.add(player.getLocation().getBlockX() + "");
				list.add("~");
			} else if (len == 3) {
				if (args[len - 1].equals("") || (player.getLocation().getBlockY() + "").startsWith(args[len - 1]))
					list.add(player.getLocation().getBlockY() + "");
				list.add("~");
			} else if (len == 4) {
				if (args[len - 1].equals("") || (player.getLocation().getBlockZ() + "").startsWith(args[len - 1]))
					list.add(player.getLocation().getBlockZ() + "");
				list.add("~");
			}
		}
		return list;
	}
}
