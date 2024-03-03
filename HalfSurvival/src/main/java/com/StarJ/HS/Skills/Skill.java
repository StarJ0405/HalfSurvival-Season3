package com.StarJ.HS.Skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.StarJ.HS.Skills.Farming.Farming;
import com.StarJ.HS.Skills.Fishing.Fishing;
import com.StarJ.HS.Skills.Hunting.Hunting;
import com.StarJ.HS.Skills.Mining.Mining;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.SkillType;

public class Skill {
	private static HashSet<Skill> list = new HashSet<Skill>();

	//
	public static final Mining Mining = new Mining();
	public static final Farming Farming = new Farming();
	public static final Hunting Hunting = new Hunting();
	public static final Fishing Fishing = new Fishing();

	//
	protected final SkillType skillType;
	protected final String key;
	protected final Type type;
	protected final String displayName;
	protected final List<String> lores;
	protected final String[] SubSkillTypes;
	protected final Skill[] preSkills;
	protected final int needPoint;
	protected final Need[] needSubSkillTypes;
	protected final Skill[] needSkills;
	protected final String[] prohibitSkills;
	protected final double[] chances;
	protected final double[] effects;

	public Skill(SkillType skillType, String key, Type type, String displayName, String[] lores, String[] SubSkillTypes,
			Skill[] preSkills, Need[] needSubSkillTypes, Skill[] needSkills, String[] prohibitSkills) {
		this(skillType, key, type, displayName, lores, SubSkillTypes, preSkills, needSubSkillTypes, needSkills,
				prohibitSkills, new double[0], new double[0]);
	}

	public Skill(SkillType skillType, String key, Type type, String displayName, String[] lores, String[] SubSkillTypes,
			Skill[] preSkills, Need[] needSubSkillTypes, Skill[] needSkills, String[] prohibitSkills,
			double[] chances) {
		this(skillType, key, type, displayName, lores, SubSkillTypes, preSkills, needSubSkillTypes, needSkills,
				prohibitSkills, chances, new double[0]);
	}

	public Skill(SkillType skillType, String key, Type type, String displayName, String[] lores, String[] SubSkillTypes,
			Skill[] preSkills, Need[] needSubSkillTypes, Skill[] needSkills, String[] prohibitSkills, double[] chances,
			double[] effects) {
		this.skillType = skillType;
		this.key = key;
		this.type = type;
		this.displayName = displayName;
		this.lores = Arrays.asList(lores);
		this.SubSkillTypes = SubSkillTypes;
		this.preSkills = preSkills;
		this.needPoint = type.needPoint;
		this.needSubSkillTypes = needSubSkillTypes;
		this.needSkills = needSkills;
		this.prohibitSkills = prohibitSkills;
		this.chances = chances;
		this.effects = effects;
		//
		list.add(this);
	}

	public String getDisplayName() {
		return type.color + displayName;
	}

	public String getKey() {
		return key;
	}

	public SkillType getType() {
		return skillType;
	}

	public SkillType getSkillType() {
		return skillType;
	}

	public Skill[] getPreSkills() {
		return preSkills;
	}

	private enum State {
		cantlearn, canlearn, learned, prohibited
	}

	public List<Skill> getProhibitSkills() {
		List<Skill> list = new ArrayList<Skill>();
		for (String prohibitSkill : prohibitSkills) {
			Skill skill = valueOf(prohibitSkill);
			if (skill != null)
				list.add(skill);
		}
		return list;
	}

	public List<String> getLore(Player player) {
		return lores;
	}

	public ItemStack getItemStack(Player player) {
		ItemStack item = new ItemStack(this.skillType.getMaterial());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.type.color + this.displayName);
		meta.setLocalizedName(this.key);
		List<String> lore = new ArrayList<String>();
		String subSkilltypes = "";
		if (this.SubSkillTypes.length > 0)
			for (String subSkillType : this.SubSkillTypes)
				subSkilltypes += (subSkilltypes.equals("") ? "" : ", ") + subSkillType;
		if (!subSkilltypes.equals(""))
			lore.add(ChatColor.GRAY + "[" + subSkilltypes + "]");
		lore.addAll(getLore(player));
		State state = hasLearn(player) ? State.learned : State.cantlearn;

		boolean delete = true;
		String preSkills = "";
		if (this.preSkills.length == 0) {
			state = state.equals(State.learned) ? State.learned : State.canlearn;
			delete = false;
		} else {
			for (Skill preSkill : this.preSkills) {
				preSkills += (preSkills.equals("") ? "" : ", ") + preSkill.displayName;
				if (delete && preSkill.hasLearn(player))
					delete = false;
			}
		}
		if (!preSkills.equals(""))
			if (this.preSkills.length == 1)
				lore.add(ChatColor.AQUA + "선행 스킬 : [" + preSkills + ChatColor.AQUA + "]");
			else
				lore.add(ChatColor.AQUA + "선행 스킬 : [" + preSkills + ChatColor.AQUA + "] 중 하나");

		delete = delete || false;
		String needSkills = "";
		for (Skill needSkill : this.needSkills) {
			needSkills += (needSkills.equals("") ? "" : ", ") + needSkill.displayName;
			if (!delete && !needSkill.hasLearn(player))
				delete = true;
		}
		if (!needSkills.equals(""))
			lore.add(ChatColor.AQUA + "필요 스킬 : [" + needSkills + "]");

		boolean prohibit = false;
		String prohibitSkills = "";
		for (Skill prohibitSkill : getProhibitSkills()) {
			prohibitSkills += (prohibitSkills.equals("") ? "" : ", ") + prohibitSkill.displayName;
			if (!prohibit && prohibitSkill.hasLearn(player)) {
				prohibit = true;
				delete = true;
			}
		}
		if (!prohibitSkills.equals(""))
			lore.add(ChatColor.DARK_RED + "금지 스킬 : [" + prohibitSkills + "]");

		for (Need need : needSubSkillTypes) {
			int now = getPoint(player, need.subSkillType);
			int needPoint = need.point;
			lore.add(ChatColor.GREEN + need.subSkillType + " : " + now + " / " + needPoint);
			if (now < needPoint)
				delete = true;
		}
		lore.add(ChatColor.WHITE + "필요 포인트 : " + needPoint);

		if (delete) {
			setLearn(player, false);
			state = prohibit ? State.prohibited : State.cantlearn;
		} else
			state = state.equals(State.learned) ? State.learned : State.canlearn;
		meta.setCustomModelData(1111111 + this.type.ordinal() * 100 + state.ordinal());
		int a;
//		if (state.equals(State.canlearn) || state.equals(State.learned))
		meta.setLore(lore);
//		else
//			meta.setLore(Arrays.asList(new String[] { ChatColor.RED + "아직 배울 수 없습니다." }));
		meta.addItemFlags(ItemFlag.values());
		item.setItemMeta(meta);
		return item;
	}

	public int canLearn(Player player) {
		if (hasLearn(player))
			return 1;
		else if (this.needPoint > ((this.skillType.getLevel(player) / 3 + 1) - getPoint(player, this.skillType)))
			return -1;
		else {
			for (Skill needSkill : needSkills)
				if (!needSkill.hasLearn(player))
					return -3;
			for (Need need : needSubSkillTypes)
				if (need.point > getPoint(player, need.subSkillType))
					return -4;
			for (Skill prohibitSkill : getProhibitSkills())
				if (prohibitSkill.hasLearn(player))
					return -5;
			if (preSkills.length == 0)
				return 0;
			else
				for (Skill preSkill : preSkills)
					if (preSkill.hasLearn(player))
						return 0;
			return -2;
		}
	}

	public boolean hasLearn(Player player) {
		Boolean learn = ConfigStore.<Boolean>getPlayerConfig(player,
				this.skillType.name() + ".skills." + this.key + ".learned");
		return learn != null ? learn : false;
	}

	public void setLearn(Player player, boolean learn) {
		ConfigStore.<Boolean>setPlayerConfig(player, this.skillType.name() + ".skills." + this.key + ".learned", learn);
	}

//	public static int getPoint(Player player) {
//		int point = 0;
//		for (Skill skill : values())
//			if (skill.hasLearn(player))
//				point += skill.needPoint;
//		return point;
//	}

	public static int getPoint(Player player, SkillType type) {
		int point = 0;
		for (Skill skill : values())
			if (skill.skillType.equals(type) && skill.hasLearn(player))
				point += skill.needPoint;
		return point;
	}

	public static int getPoint(Player player, String subSkillType) {
		int point = 0;
		for (Skill skill : values())
			if (Arrays.asList(skill.SubSkillTypes).contains(subSkillType) && skill.hasLearn(player))
				point += 1;
		return point;
	}

	public static int[] getPoints(Player player, SkillType type) {
		int[] points = new int[] { 0, 0, 0, 0 };
		for (Skill skill : values())
			if (skill.skillType.equals(type) && skill.hasLearn(player)) {
				points[0] += skill.needPoint;
				List<String> list = Arrays.asList(skill.SubSkillTypes);
				if (list.contains(type.leftSkillType))
					points[1] += 1;
				else if (list.contains(type.middleSkillType))
					points[2] += 1;
				else if (list.contains(type.rightSkillType))
					points[3] += 1;
			}
		return points;
	}

	public boolean confirmChance(Player player) {
		return confirmChance(player, 0, new double[0], new double[0], new double[0]);
	}

	public boolean confirmChance(Player player, int num) {
		return confirmChance(player, num, new double[0], new double[0], new double[0]);
	}

	public boolean confirmChance(Player player, int num, double[] add) {
		return confirmChance(player, num, add, new double[0], new double[0]);
	}

	public boolean confirmChance(Player player, int num, double[] add, double[] multiply) {
		return confirmChance(player, num, add, multiply, new double[0]);
	}

	public boolean confirmChance(Player player, int num, double[] add, double[] multiply, double[] multiply_last) {
		if (hasLearn(player))
			if (chances.length == 0)
				return true;
			else if (chances.length > num)
				return new Random().nextDouble() < (chances[num] * (multiply.length > num ? multiply[num] : 1)
						+ (add.length > num ? add[num] : 0)) * (multiply_last.length > num ? multiply_last[num] : 1);
		return false;
	}

	public boolean checkChance(Player player) {
		return checkChance(player, 0, new double[0], new double[0], new double[0]);
	}

	public boolean checkChance(Player player, int num) {
		return checkChance(player, num, new double[0], new double[0], new double[0]);
	}

	public boolean checkChance(Player player, int num, double[] add) {
		return checkChance(player, num, add, new double[0], new double[0]);
	}

	public boolean checkChance(Player player, int num, double[] add, double[] multiply) {
		return checkChance(player, num, add, multiply, new double[0]);
	}

	public boolean checkChance(Player player, int num, double[] add, double[] multiply, double[] multiply_last) {
		if (chances.length == 0)
			return true;
		else if (chances.length > num)
			return new Random().nextDouble() < (chances[num] * (multiply.length > num ? multiply[num] : 1)
					+ (add.length > num ? add[num] : 0)) * (multiply_last.length > num ? multiply_last[num] : 1);
		return false;
	}

	public double getChances() {
		return getChances(0);
	}

	public double getChances(int num) {
		return this.chances.length > num ? this.chances[num] : 0;
	}

	public double getEffect() {
		return getEffect(0);
	}

	public double getEffect(int num) {
		return this.effects.length > num ? this.effects[num] : 0;
	}

	public static List<Skill> values(SkillType type) {
		switch (type) {
		case mining:
			return Mining.values();
		case farming:
			return Farming.values();
		case hunting:
			return Hunting.values();
		case fishing:
			return Fishing.values();
		}
		return Arrays.asList(list.toArray(Skill[]::new));
	}

	public static List<Skill> values() {
		return Arrays.asList(list.toArray(Skill[]::new));
	}

	public static Skill valueOf(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
				? valueOf(item.getItemMeta().getLocalizedName())
				: null;
	}

	public static Skill valueOf(String key) {
		for (Skill skill : list)
			if (skill.key.equals(key))
				return skill;
		return null;
	}

	public static class Need {
		public final String subSkillType;
		public final int point;

		public Need(String subSkillType, int point) {
			this.subSkillType = subSkillType;
			this.point = point;
		}
	}

	public enum Type {
		Active(ChatColor.GOLD, 1), Passive(ChatColor.GRAY, 1), Transform(ChatColor.LIGHT_PURPLE, 2),
		Upgrade(ChatColor.YELLOW, 2);

		private final ChatColor color;
		private final int needPoint;

		private Type(ChatColor color, int needPoint) {
			this.color = color;
			this.needPoint = needPoint;
		}
	}

	public void damage(Player att, LivingEntity vic, double damage) {
		if (vic != null) {
			int tick = vic.getNoActionTicks();
			vic.setNoDamageTicks(10);
			vic.damage(damage, att);
			vic.setTicksLived(tick);
		}
	}

	public Vector getModifiedVector(Vector direction, Vector mod) {
		return getModifiedVector(direction, mod.getX(), mod.getY(), mod.getZ());
	}

	public Vector getModifiedVector(Vector direction, double front, double up, double right) {
		double y = direction.getY();
		double x = direction.getX();
		double z = direction.getZ();

		double fx = x * front + -z * right;
		double fy = y + up;
		double fz = z * front + x * right;
		return new Vector(fx, fy, fz);
	}
}
