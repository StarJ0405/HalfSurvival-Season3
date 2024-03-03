package com.StarJ.HS.Skills.Fishing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.SkillType;

public class Active4 extends UsableSkill {

	public Active4(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.fishing, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		if (Skill.Fishing.transform_right4.confirmChance(player))
			lore.add(ChatColor.WHITE + "다음 낚시에 4배 빠르게 물고기를 낚는다.");
		else
			lore.add(ChatColor.WHITE + "다음 낚시에 2배 빠르게 물고기를 낚는다.");
		lore.add(ChatColor.GRAY + "찌가 있는 상태에서 발동시 날라가므로 주의!");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			ConfigStore.<Boolean>setPlayerConfig(player, "Active.skills." + this.key + ".status", true);
			player.playSound(player.getEyeLocation(), Sound.ENTITY_DOLPHIN_SWIM, 1f, 1f);
			player.sendMessage(this.displayName + ChatColor.GREEN + " 발동");
			if (Skill.Fishing.transform_middle4.confirmChance(player)) {
				double multiply = 1;
				if (Skill.Fishing.upgrade_middle4.confirmChance(player)) {
					multiply = Skill.Fishing.upgrade_middle4.getEffect();
					player.sendMessage(Skill.Fishing.upgrade_middle4.getDisplayName() + ChatColor.WHITE + " 발동");
				}
				player.getAttribute(Attribute.GENERIC_LUCK)
						.setBaseValue(player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue()
								+ Skill.Fishing.transform_middle4.getEffect() * multiply);
				ConfigStore.<Boolean>setPlayerConfig(player, "fishing.skills.transform_middle4.status", true);
				player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1f);
				player.sendMessage(Skill.Fishing.transform_middle4.getDisplayName() + ChatColor.WHITE + " 발동");
			}
			if (Skill.Fishing.upgrade_right4_1.confirmChance(player)) {
				player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);
				ConfigStore.<Boolean>setPlayerConfig(player, "fishing.skills.upgrade_right4_1.status", true);
				player.sendMessage(Skill.Fishing.upgrade_right4_1.getDisplayName() + ChatColor.GREEN + " 발동");
			}

		}
		return true;
	}

}
