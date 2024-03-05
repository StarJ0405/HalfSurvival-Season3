package shining.starj.HalfSurvival.Skills.Hunting;

import shining.starj.HalfSurvival.Skills.Hunting.Hunting.WeaponType;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Active4 extends UsableSkill {

	public Active4(String key, String displayName, Skill[] preSkills, double cooldown, double duration,
			useSlot useSlot) {
		super(SkillType.hunting, key, displayName, preSkills, cooldown, duration, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(WeaponType.LongSword.getColorName() + ChatColor.WHITE + " : 지속시간동안 공격시 범위에 추가피해를 준다.");
		lore.add(WeaponType.ShortSword.getColorName() + ChatColor.WHITE + " : 지속시간동안 공격시 범위 내 한명에게 추가 피해를 준다.");
		lore.add(ChatColor.RED + "     : 범위 내 적이 한명인 경우 해당 적에게 50%의 피해로 준다.");
		lore.add(WeaponType.Bow.getColorName() + ChatColor.WHITE + " : 화살 발사시 추가 범위 화살을 3발 더 발사한다.");
		lore.add(WeaponType.Crossbow.getColorName() + ChatColor.WHITE + " : 화살 발사시 화살을 한번더 발사한다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			ItemStack off = player.getInventory().getItemInOffHand();
			long duration = getDuration(player);
			if (duration > 0) {
				setDurationTime(player, duration / 1000d);
				HashMapStore.setDuration(player, this, duration);
			}
			player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT, 2f, 2f);
			player.getInventory().setItemInOffHand(player.getInventory().getItemInMainHand());
			player.getInventory().setItemInMainHand(off);
		}
		return true;
	}

}
