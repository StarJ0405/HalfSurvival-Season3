package com.StarJ.HS.Skills.Farming;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.SkillType;

public class Active1 extends UsableSkill {

	public Active1(String key, String displayName, Skill[] preSkills, double cooldown, double duration,
			useSlot useSlot) {
		super(SkillType.farming, key, displayName, preSkills, cooldown, duration, useSlot);
	}

	@Override
	public long getCooldown(Player player) {
		return (long) (super.getCooldown(player)
				* (Skill.Mining.upgrade1.confirmChance(player) ? Skill.Mining.upgrade1.getEffect() : 1));
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(ChatColor.WHITE + "지속시간 동안 블럭을 파괴하는 속도가 증가합니다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			long duration = getDuration(player);
			if (duration > 0) {
				setDurationTime(player, duration / 1000d);
				HashMapStore.setDuration(player, this, duration);
			}
			player.playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (duration * 20 / 1000l), 0,
					true, false, false));
		}
		return true;
	}

	@Override
	public void setDurationTime(Player player, double duration) {
		super.setDurationTime(player, duration);
		if (duration > 0) {
			player.addPotionEffect(
					new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (duration * 20), 0, true, false, false));
		} else
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
	}
}
