package shining.starj.HalfSurvival.Commands;

import shining.starj.HalfSurvival.Systems.ConfigStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class MoneyCommand implements CommandExecutor, TabCompleter {
	public static LinkedHashMap<Player, Long> sortByValue(LinkedHashMap<Player, Long> hs) {
		List<Entry<Player, Long>> list = new LinkedList<Entry<Player, Long>>(hs.entrySet());
		Collections.sort(list, new Comparator<Entry<Player, Long>>() {
			@Override
			public int compare(Entry<Player, Long> o1, Entry<Player, Long> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		LinkedHashMap<Player, Long> temp = new LinkedHashMap<Player, Long>();
		for (Entry<Player, Long> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DecimalFormat df = new DecimalFormat("###,###");
		if (args.length == 0 && sender instanceof Player) {
			Player player = (Player) sender;
			long money = ConfigStore.getPlayerMoney(player);
			player.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.WHITE + "님의 소지 금액 : " + ChatColor.YELLOW
					+ df.format(money) + ChatColor.WHITE + "원");
			return true;
		} else if (args.length == 1) {
			LinkedHashMap<Player, Long> hs = new LinkedHashMap<Player, Long>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				long money = ConfigStore.getPlayerMoney(player);
				hs.put(player, money);
			}
			hs = sortByValue(hs);
			int i = 0;
			for (Player player : hs.keySet())
				sender.sendMessage(
						ChatColor.GREEN + "" + (i++) + " - " + ChatColor.GOLD + player.getName() + ChatColor.WHITE
								+ "의 소지 금액 : " + ChatColor.YELLOW + df.format(hs.get(player)) + ChatColor.WHITE + "원");
			return true;
		} else if (args.length == 3) {
			OfflinePlayer targetOff = Bukkit.getOfflinePlayer(args[1]);
			if (targetOff.isOnline()) {
				Player target = targetOff.getPlayer();
				try {
					long input = Long.parseLong(args[2]);
					if (args[0].equalsIgnoreCase("send") || args[0].equalsIgnoreCase("s")) {
						if (sender instanceof Player) {
							Player player = (Player) sender;
							long money = ConfigStore.getPlayerMoney(player);
							if (money < input)
								sender.sendMessage(ChatColor.RED + "입력 값보다 적은 값의 돈을 소지하고 있습니다.");
							else {
								ConfigStore.setPlayerMoney(player, money - input);
								long has = ConfigStore.getPlayerMoney(target);
								ConfigStore.setPlayerMoney(target, has + input);
								player.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.WHITE + "님에게 "
										+ ChatColor.YELLOW + df.format(input) + ChatColor.WHITE + "원을 송신했습니다.");
								target.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.WHITE + "님으로부터 "
										+ ChatColor.YELLOW + df.format(input) + ChatColor.WHITE + "원을 송신받았습니다.");
							}
							return true;
						} else
							sender.sendMessage(ChatColor.RED + "플레이어만 사용 가능합니다.");
					} else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
						long has = ConfigStore.getPlayerMoney(target);
						ConfigStore.setPlayerMoney(target, has + input);
						sender.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.WHITE + "님에게 "
								+ ChatColor.YELLOW + df.format(input) + ChatColor.WHITE + "원을 지급했습니다.");
						target.sendMessage(ChatColor.GOLD + "운영자" + ChatColor.WHITE + "님으로부터 " + ChatColor.YELLOW
								+ df.format(input) + ChatColor.WHITE + "원을 지급받았습니다.");
						return true;
					} else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("t")) {
						long has = ConfigStore.getPlayerMoney(target);
						has -= input;
						ConfigStore.setPlayerMoney(target, has < 0 ? 0l : has);
						sender.sendMessage(ChatColor.GOLD + target.getName() + ChatColor.WHITE + "님에게 "
								+ ChatColor.YELLOW + df.format(input) + ChatColor.WHITE + "원을 제거했습니다.");
						target.sendMessage(ChatColor.GOLD + "운영자" + ChatColor.WHITE + "님으로부터 " + ChatColor.YELLOW
								+ df.format(input) + ChatColor.WHITE + "원을 제거됐습니다.");
						return true;
					}
				} catch (NumberFormatException nfe) {
					sender.sendMessage(ChatColor.RED + "잘못된 값이 입력되었습니다.");
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + args[1] + ChatColor.RED + "님은 접속중이 아니거나 잘못된 이름입니다.");
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			if (args[len - 1].equals("") || "send".startsWith(args[len - 1].toLowerCase()))
				list.add("send");
			if (args[len - 1].equals("") || "list".startsWith(args[len - 1].toLowerCase()))
				list.add("list");
			if (sender instanceof ConsoleCommandSender || sender.isOp()) {
				if (args[len - 1].equals("") || "give".startsWith(args[len - 1].toLowerCase()))
					list.add("give");
				if (args[len - 1].equals("") || "take".startsWith(args[len - 1].toLowerCase()))
					list.add("take");
			}
		} else if (len == 2)
			for (Player player : Bukkit.getOnlinePlayers())
				if (args[len - 1].equals("") || player.getName().toLowerCase().startsWith(args[len - 1].toLowerCase()))
					list.add(player.getName());
		return list;
	}
}
