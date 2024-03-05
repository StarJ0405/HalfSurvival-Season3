package shining.starj.HalfSurvival.Skills;

import shining.starj.HalfSurvival.Skills.Slot.SkillChecker;
import shining.starj.HalfSurvival.Systems.Slots;

public class SkillTree {
	private final SkillLine[] lines;

	private SkillTree(SkillLine... lines) {
		this.lines = lines;
	}

	public SkillLine[] getLines() {
		return lines;
	}

	public static SkillTree from(SkillLine... lines) {
		return new SkillTree(lines);
	}

	public static SkillTree getTree(UsableSkill active1, UsableSkill active2, UsableSkill active3, UsableSkill active4,
			Skill upgrade1, Skill minigame, Skill passive, Skill transform_left2, Skill transform_middle3,
			Skill transform_right4, Skill upgrade_left2_1, Skill upgrade_middle3_1, Skill upgrade_right4_1,
			Skill passive_left1, Skill passive_middle1, Skill passive_right1, Skill passive_left2,
			Skill passive_middle2, Skill passive_right2, Skill transform_left3, Skill transform_middle4,
			Skill transform_right2, Skill upgrade_left3, Skill upgrade_middle4, Skill upgrade_right2,
			Skill passive_left3, Skill passive_middle3, Skill passive_right3, Skill upgrade_left2_2,
			Skill upgrade_middle3_2, Skill upgrade_right4_2, Skill transform_left1, Skill transform_middle1,
			Skill transform_right1, Skill transform_left4, Skill transform_middle2, Skill transform_right3,
			Skill upgrade_left4, Skill upgrade_middle2, Skill upgrade_right3) {
		return SkillTree.from(//
				SkillLine.from(Slot.from(3, active1)), //
				SkillLine.from(Slot.from(3, Slots.l, SkillChecker.from(active1, upgrade1))), //
				SkillLine.from(Slot.from(3, upgrade1), Slot.from(4, Slots.m, SkillChecker.from(upgrade1, minigame)),
						Slot.from(5, minigame)), //
				SkillLine.from(Slot.from(1, Slots.rr, SkillChecker.from(upgrade1, active2)),
						Slot.from(2, Slots.m, SkillChecker.from(upgrade1, active2)),
						Slot.from(3, Slots.plus, SkillChecker.from(upgrade1, active4),
								SkillChecker.from(upgrade1, active3), SkillChecker.from(upgrade1, active2)),
						Slot.from(4, Slots.m, SkillChecker.from(upgrade1,active4)),
						Slot.from(5, Slots.r, SkillChecker.from(upgrade1,active4))), //
				SkillLine.from(Slot.from(1, active2), Slot.from(3, active3), Slot.from(5, active4)), //
				SkillLine.from(Slot.from(1, Slots.s, SkillChecker.from(active2, passive)),
						Slot.from(2, Slots.m, SkillChecker.from(active2, passive)),
						Slot.from(3, Slots.plus, SkillChecker.from(active4, passive),
								SkillChecker.from(active3, passive), SkillChecker.from(active2, passive)),
						Slot.from(4, Slots.m, SkillChecker.from(active4, passive)),
						Slot.from(5, Slots.rs, SkillChecker.from(active4, passive))), //
				SkillLine.from(Slot.from(3, passive)), //
				SkillLine.from(Slot.from(1, Slots.rr, SkillChecker.from(passive, transform_left2)),
						Slot.from(2, Slots.m, SkillChecker.from(passive, transform_left2)),
						Slot.from(3, Slots.plus, SkillChecker.from(passive, transform_right4),
								SkillChecker.from(passive, transform_middle3),
								SkillChecker.from(passive, transform_left2)),
						Slot.from(4, Slots.m, SkillChecker.from(passive, transform_right4)),
						Slot.from(5, Slots.r, SkillChecker.from(passive, transform_right4))), //
				SkillLine.from(Slot.from(1, transform_left2), Slot.from(3, transform_middle3),
						Slot.from(5, transform_right4)), //
				SkillLine.from(Slot.from(1, Slots.l, SkillChecker.from(transform_left2, upgrade_left2_1)),
						Slot.from(3, Slots.l, SkillChecker.from(transform_middle3, upgrade_middle3_1)),
						Slot.from(5, Slots.l, SkillChecker.from(transform_right4, upgrade_right4_1))), //
				SkillLine.from(Slot.from(1, upgrade_left2_1), Slot.from(3, upgrade_middle3_1),
						Slot.from(5, upgrade_right4_1)), //
				SkillLine.from(
						Slot.from(1, Slots.k, SkillChecker.from(upgrade_left2_1, passive_left1),
								SkillChecker.from(upgrade_left2_1, passive_left2)),
						Slot.from(2, passive_left1),
						Slot.from(3, Slots.k, SkillChecker.from(upgrade_middle3_1, passive_middle1),
								SkillChecker.from(upgrade_middle3_1, passive_middle2)),
						Slot.from(4, passive_middle1),
						Slot.from(5, Slots.k, SkillChecker.from(upgrade_right4_1, passive_right1),
								SkillChecker.from(upgrade_right4_1, passive_right2)),
						Slot.from(6, passive_right1)), //
				SkillLine.from(Slot.from(1, passive_left2), Slot.from(3, passive_middle2),
						Slot.from(5, passive_right2)), //
				SkillLine.from(Slot.from(1, Slots.l, SkillChecker.from(passive_left2, transform_left3)),
						Slot.from(3, Slots.l, SkillChecker.from(passive_middle2, transform_middle4)),
						Slot.from(5, Slots.l, SkillChecker.from(passive_right2, transform_right2))),
				SkillLine.from(Slot.from(1, transform_left3), Slot.from(3, transform_middle4),
						Slot.from(5, transform_right2)), //
				SkillLine.from(
						Slot.from(1, Slots.plus, SkillChecker.from(transform_left3, upgrade_left3),
								SkillChecker.from(transform_left3, passive_left3),
								SkillChecker.from(transform_left3, upgrade_right2)),
						Slot.from(2, upgrade_left3),
						Slot.from(3, Slots.plus, SkillChecker.from(transform_middle4, upgrade_middle4),
								SkillChecker.from(transform_middle4, passive_middle3),
								SkillChecker.from(transform_middle4, upgrade_left3)),
						Slot.from(4, upgrade_middle4),
						Slot.from(5, Slots.plus, SkillChecker.from(transform_right2, upgrade_right2),
								SkillChecker.from(transform_right2, passive_right3),
								SkillChecker.from(transform_right2, upgrade_middle4)),
						Slot.from(6, upgrade_right2)), //
				SkillLine.from(Slot.from(1, passive_left3), Slot.from(3, passive_middle3),
						Slot.from(5, passive_right3)), //
				SkillLine.from(
						Slot.from(1, Slots.plus, SkillChecker.from(passive_left3, transform_left1),
								SkillChecker.from(passive_left3, upgrade_left2_2),
								SkillChecker.from(passive_left3, transform_right1)),
						Slot.from(2, Slots.n, SkillChecker.from(passive_middle3, transform_left1),
								SkillChecker.from(passive_left3, transform_left1)),
						Slot.from(3, Slots.plus, SkillChecker.from(passive_middle3, transform_middle1),
								SkillChecker.from(passive_middle3, upgrade_middle3_2),
								SkillChecker.from(passive_middle3, transform_left1)),
						Slot.from(4, Slots.n, SkillChecker.from(passive_right3, transform_middle1),
								SkillChecker.from(passive_middle3, transform_middle1)),
						Slot.from(5, Slots.plus, SkillChecker.from(passive_right3, transform_right1),
								SkillChecker.from(passive_right3, upgrade_right4_2),
								SkillChecker.from(passive_right3, transform_middle1)),
						Slot.from(6, Slots.n, SkillChecker.from(passive_left3, transform_right1),
								SkillChecker.from(passive_right3, transform_right1))), //
				SkillLine.from(Slot.from(1, upgrade_left2_2), Slot.from(2, transform_left1),
						Slot.from(3, upgrade_middle3_2), Slot.from(4, transform_middle1),
						Slot.from(5, upgrade_right4_2), Slot.from(6, transform_right1)), //
				SkillLine.from(Slot.from(1, Slots.l, SkillChecker.from(upgrade_left2_2, transform_left4)),
						Slot.from(3, Slots.l, SkillChecker.from(upgrade_middle3_2, transform_middle2)),
						Slot.from(5, Slots.l, SkillChecker.from(upgrade_right4_2, transform_right3))), //
				SkillLine.from(Slot.from(1, transform_left4), Slot.from(3, transform_middle2),
						Slot.from(5, transform_right3)), //
				SkillLine.from(Slot.from(1, Slots.l, SkillChecker.from(transform_left4, upgrade_left4)),
						Slot.from(3, Slots.l, SkillChecker.from(transform_middle2, upgrade_middle2)),
						Slot.from(5, Slots.l, SkillChecker.from(transform_right3, upgrade_right3))), //
				SkillLine.from(Slot.from(1, upgrade_left4), Slot.from(3, upgrade_middle2),
						Slot.from(5, upgrade_right3)));
	}
}
