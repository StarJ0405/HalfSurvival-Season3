package shining.starj.HalfSurvival.Commands;

import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.closeInventory();
			for (SkillType skillType : SkillType.values())
				if (args.length == 0) {
					skillType.setLevel(player, 0);
					skillType.setExp(player, 0);
				} else if (args.length == 1) {
					skillType.setLevel(player, 100);
					skillType.setExp(player, 0);
				} else if (args.length == 3) {
					skillType.setLevel(player, skillType.getLevel(player) + 1);
					skillType.setExp(player, 0);
				} else if (args.length == 4) {
					skillType.setLevel(player, skillType.getLevel(player) - 1);
					skillType.setExp(player, 0);
				}
			if (args.length == 2)
				for (Skill skill : Skill.values())
					if (skill instanceof UsableSkill) {
						((UsableSkill) skill).setDurationTime(player, 0);
						((UsableSkill) skill).setLastUseTime(player, 0);
					}

			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		return list;
	}
}
