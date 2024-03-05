package shining.starj.HalfSurvival.Skills.Mining;

import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Active1 extends UsableSkill {

	public Active1(String key, String displayName, Skill[] preSkills, double cooldown, double duration,
			useSlot useSlot) {
		super(SkillType.mining, key, displayName, preSkills, cooldown, duration, useSlot);
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
			if (Skill.Mining.transform_middle1.hasLearn(player))
				HashMapStore.setMiningTransformMiddle1(player, (int) Skill.Mining.transform_middle1.getEffect(1));
			if (Skill.Mining.transform_right1.hasLearn(player))
				HashMapStore.setMiningTransformRight1(player, (int) Skill.Mining.transform_right1.getEffect(1));
		}
		return true;
	}

	@Override
	public void setDurationTime(Player player, double duration) {
		super.setDurationTime(player, duration);
		int level = player.hasPotionEffect(PotionEffectType.FAST_DIGGING)
				? player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier()
				: 0;
		player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		if (duration > 0)
			player.addPotionEffect(
					new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (duration * 20), level, true, false, false));
	}
}
