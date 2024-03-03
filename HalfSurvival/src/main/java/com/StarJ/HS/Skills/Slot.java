package com.StarJ.HS.Skills;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Systems.Slots;

public abstract class Slot {
	protected final int num;

	public Slot(int num) {
		this.num = num < 0 ? 0 : (num > 7 ? 7 : num);
	}

	public int getNum() {
		return num;
	}

	public abstract ItemStack getItemStack(Player player);

	public static class SkillSlot extends Slot {
		private final Skill skill;

		private SkillSlot(int num, Skill skill) {
			super(num);
			this.skill = skill;
		}

		@Override
		public ItemStack getItemStack(Player player) {
			return skill.getItemStack(player);
		}

		public static SkillSlot from(int num, Skill skill) {
			return new SkillSlot(num, skill);
		}
	}

	public static class LineSlot extends Slot {
		private final Slots slot;
		private final SkillChecker[] checkers;

		private LineSlot(int num, Slots slot, SkillChecker... checkers) {
			super(num);
			this.slot = slot;
			this.checkers = checkers;
		}

		@Override
		public ItemStack getItemStack(Player player) {
			int add = 0;
			for (int i = 0; i < checkers.length; i++)
				if (checkers[i].hasLearn(player))
					add += (i + 1) * (i + 1);
			return slot.getItemStack(player, add);
		}

		public static LineSlot from(int num, Slots slot, SkillChecker... checkers) {
			return new LineSlot(num, slot, checkers);
		}
	}

	public static class SkillChecker {
		private final Skill pre;
		private final Skill[] skills;

		private SkillChecker(Skill pre, Skill... skills) {
			this.pre = pre;
			this.skills = skills;
		}

		public boolean hasLearn(Player player) {
			if (pre.hasLearn(player))
				for (Skill skill : skills)
					if (skill.hasLearn(player))
						return true;
			return false;
		}

		public static SkillChecker from(Skill skill, Skill... skills) {
			return new SkillChecker(skill, skills);
		}
	}

	public static LineSlot from(int num, Slots slot, SkillChecker... checkers) {
		return new LineSlot(num, slot, checkers);
	}

	public static SkillSlot from(int num, Skill skill) {
		return new SkillSlot(num, skill);
	}
}
