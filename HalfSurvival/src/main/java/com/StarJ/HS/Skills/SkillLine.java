package com.StarJ.HS.Skills;

public class SkillLine {
	private final Slot[] slots;

	private SkillLine(Slot... slots) {
		this.slots = slots;
	}

	public Slot[] getSlots() {
		return slots;
	}

	public static SkillLine from(Slot... slots) {
		return new SkillLine(slots);
	}
}
