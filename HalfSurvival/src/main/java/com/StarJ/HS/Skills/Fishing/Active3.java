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
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.SkillType;

public class Active3 extends UsableSkill {

	public Active3(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.fishing, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(ChatColor.WHITE + "다음 낚시에서 희귀한 물고기가 뜰 확률이 높아진다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			ConfigStore.setPlayerConfig(player, "fishing.skills.active3.status", true);
			player.playSound(player.getEyeLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 1f, 1f);
			Integer stacki = ConfigStore.getPlayerConfig(player, "fishing.skills.transform_middle3.stack");
			int base = (int) player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue();
			int luck = 1 + (stacki != null ? stacki : 0);
			int multiply = 1;
			if (Skill.Fishing.upgrade_middle3_2.confirmChance(player)) {
				multiply = (int) Skill.Fishing.upgrade_middle3_2.getEffect();
				player.sendMessage(Skill.Fishing.upgrade_middle3_2.getDisplayName() + ChatColor.WHITE + " 발동");
				player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 1f, 1f);
			}
			player.getAttribute(Attribute.GENERIC_LUCK).setBaseValue((base + luck) * multiply);
			ConfigStore.setPlayerConfig(player, "fishing.skills.transform_middle3.stack", 0);
			if (multiply > 1)
				player.sendMessage(
						this.displayName + ChatColor.GREEN + " 발동" + ChatColor.GRAY + " <(" + base + "+" + luck + ")*"
								+ multiply + "=" + ChatColor.YELLOW + (base + luck) * multiply + ChatColor.GRAY + ">");
			else
				player.sendMessage(this.displayName + ChatColor.GREEN + " 발동" + ChatColor.GRAY + " <" + base + "+"
						+ luck + "=" + ChatColor.YELLOW + (base + luck) + ChatColor.GRAY + ">");
			if (Skill.Fishing.transform_right3.confirmChance(player)) {
				if (Skill.Fishing.upgrade_right3.confirmChance(player)) {
					player.sendMessage(Skill.Fishing.upgrade_right3.getDisplayName() + ChatColor.WHITE + " 발동");
					for (UsableSkill active : Skill.Fishing.getActives()) {
						double percent = Skill.Fishing.upgrade_right3.getEffect();
						long remain = active.getRemainUseTime(player);
						if (remain > 0) {
							remain -= (long) (active.getCooldown(player) * percent);
							active.setLastUseTime(player, remain / 1000d);
							HashMapStore.setCooldown(player, active, remain);
							if (remain > 0)
								player.sendMessage(active.getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
										+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
										+ (String.format("%.1f", remain / 1000d)) + ")");
							else
								HashMapStore.sendCooldownMessage(player, active);
						}
					}
				} else {
					player.sendMessage(Skill.Fishing.transform_right3.getDisplayName() + ChatColor.WHITE + " 발동");
					double percent = Skill.Fishing.transform_right3.getEffect();
					long remain = getRemainUseTime(player);
					if (remain > 0) {
						remain -= (long) (getCooldown(player) * percent);
						setLastUseTime(player, remain / 1000d);
						HashMapStore.setCooldown(player, this, remain);
						if (remain > 0)
							player.sendMessage(getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
									+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
									+ (String.format("%.1f", remain / 1000d)) + ")");
						else
							HashMapStore.sendCooldownMessage(player, this);
					}
				}
			}
		}
		return true;
	}

}
