package shining.starj.HalfSurvival.Skills.Hunting;

import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.Skill.Need;
import shining.starj.HalfSurvival.Skills.Skill.Type;
import shining.starj.HalfSurvival.Skills.SkillTree;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Skills.UsableSkill.useSlot;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Hunting {
	private final List<Skill> list = new ArrayList<Skill>();
	//
	public final Active1 active1 = new Active1("hunting_active1", "이동기", new Skill[0], 1, useSlot.left);
	//
	public final Skill upgrade1 = new Skill(SkillType.hunting, "hunting_upgrade1", Type.Upgrade, "이동 공격",
			new String[] { active1.getDisplayName() + ChatColor.WHITE + " 사용시 공격이 추가됩니다.", ChatColor.WHITE + "" },
			new String[0], new Skill[] { active1 }, new Need[0], new Skill[0], new String[0], new double[0],
			new double[] { 0.5 });
	public final Skill minigame = new Skill(SkillType.hunting, "hunting_minigame", Type.Passive, "미니게임",
			new String[] { ChatColor.WHITE + "광물 파괴시 5% 확률로 미니게임이 발생합니다.", ChatColor.WHITE + "쉬프트로 게임 진행 가능",
					ChatColor.GRAY + "성공 횟수당 +1개씩 최대 3개 블럭 지급" },
			new String[0], new Skill[] { upgrade1 }, new Need[0], new Skill[0], new String[0], new double[] { 0.05d });
	//
	public final Active2 active2 = new Active2("hunting_active2", "연계기", new Skill[] { upgrade1 }, 3, 1.5,
			useSlot.right);
	public final Active3 active3 = new Active3("hunting_active3", "기본기", new Skill[] { upgrade1 }, 3,
			useSlot.shiftRight);
	public final Active4 active4 = new Active4("hunting_active4", "강화기", new Skill[] { upgrade1 }, 1, 100,
			useSlot.shiftLeft);
	//
	public final Skill passive = new Skill(SkillType.hunting, "hunting_passive1", Type.Passive, "자동 보호",
			new String[] { ChatColor.WHITE + "피격시 5초에 한번 보호막이 20 들어옵니다." }, new String[0],
			new Skill[] { active2, active3, active4 }, new Need[0], new Skill[0], new String[0], new double[0],
			new double[] { 5, 20 });
	//
	public final Skill transform_left2 = new Skill(SkillType.hunting, "hunting_transform_left2", Type.Transform,
			"작은 분노",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + " 발동시 25% 확률로 분노가 1스택 쌓입니다.",
					ChatColor.WHITE + "최대 스택시 피해를 입히면 5초동안 피해를 1.5배로 입힙니다.", ChatColor.RED + "최대 5스택" },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.25d }, new double[] { 10, 5, 1.5 });
	public final Skill transform_middle3 = new Skill(SkillType.hunting, "hunting_transform_middle3", Type.Transform,
			"위치 파악", new String[] { ChatColor.WHITE + "탐색한 광물의 정확한 위치를 10초간 파악할 수 있습니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[0], new double[] { 10 });
	public final Skill transform_right4 = new Skill(SkillType.hunting, "hunting_transform_right4", Type.Transform,
			"등가 교환", new String[] { active4.getDisplayName() + ChatColor.WHITE + "이 들고 있는 광물로 변형으로 변경됩니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	//
	public final Skill upgrade_left2_1 = new Skill(SkillType.hunting, "hunting_upgrade_left2_1", Type.Upgrade,
			ChatColor.WHITE + "강한 분노", new String[] { ChatColor.WHITE + "분노 발동시 피해량이 1.5배에서 3배로 변경됩니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { transform_left2 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 2 });
	public final Skill upgrade_middle3_1 = new Skill(SkillType.hunting, "hunting_upgrade_middle3_1", Type.Upgrade,
			"정밀 탐색",
			new String[] { ChatColor.WHITE + "탐색한 광물의 종류를 색깔로 구분 가능합니다.",
					ChatColor.WHITE + "탐색 범위가 전방향으로 +3칸씩 증가합니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { transform_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 3 });
	public final Skill upgrade_right4_1 = new Skill(SkillType.hunting, "hunting_upgrade_right4_1", Type.Upgrade,
			"범위 증가",
			new String[] { active4.getDisplayName() + ChatColor.WHITE + "의 범위가 ±1칸씩 증가합니다.",
					ChatColor.WHITE + "추가 칸들은 각각 25% 확률로 변형됩니다.", ChatColor.RED + "소모되는 아이템의 수가 증가합니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { transform_right4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 1d, 0.25d }, new double[] { 1 });
	//
	public final Skill passive_left1 = new Skill(SkillType.hunting, "hunting_passive_left1", Type.Passive, "잦은 분노",
			new String[] { ChatColor.WHITE + "공격시 5% 확률로 분노를 1스택 증가시킵니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[] { "passive_middle1", "passive_right1" }, new double[] { 0.05d });
	public final Skill passive_middle1 = new Skill(SkillType.hunting, "hunting_passive_middle1", Type.Passive, "환경 저항",
			new String[] { ChatColor.WHITE + "블럭 파괴시 25% 확률로 2분동안 야간투시와 전달체의 힘 및 화염저항을 얻습니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_right1" }, new double[] { 0.25d },
			new double[] { 120 });
	public final Skill passive_right1 = new Skill(SkillType.hunting, "hunting_passive_right1", Type.Passive, "호문쿨루스",
			new String[] { ChatColor.WHITE + "광물 파괴시 20% 확률로 광물을 복제합니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_middle1" }, new double[] { 0.2d });
	//
	public final Skill passive_left2 = new Skill(SkillType.hunting, "hunting_passive_left2", Type.Passive, "덜 풀린 분노",
			new String[] { ChatColor.WHITE + "분노 발동시 20% 확률로 분노 게이지의 절반을 돌려받습니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.2d }, new double[] { 0.5d });
	public final Skill passive_middle2 = new Skill(SkillType.hunting, "hunting_passive_middle2", Type.Passive,
			"신속한 움직임", new String[] { ChatColor.WHITE + "블럭 파괴시 25% 확률로 2분동안 이동속도와 점프력이 증가합니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d }, new double[] { 120d });
	public final Skill passive_right2 = new Skill(SkillType.hunting, "hunting_passive_right2", Type.Passive, "찾았다 이놈!",
			new String[] { ChatColor.WHITE + "관련 블럭 파괴시 10% 확률로 랜덤 광물로 변형됩니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.1d });
	//
	public final Skill transform_left3 = new Skill(SkillType.hunting, "hunting_transform_left3", Type.Transform, "짜증",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + " 발동시 40% 확률로 분노가 1스택 쌓입니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { passive_left2 }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[] { 0.4d });
	public final Skill transform_middle4 = new Skill(SkillType.hunting, "hunting_transform_middle4", Type.Transform,
			"친구 찾기",
			new String[] {
					active4.getDisplayName() + ChatColor.WHITE + " 발동시 해당 블럭 기준으로 " + active3.getDisplayName()
							+ ChatColor.WHITE + "을 발동합니다.",
					ChatColor.RED + "단, " + active4.getDisplayName() + ChatColor.RED + "로 변형시킨 종류의 블럭만 탐색합니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { passive_middle2 }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	public final Skill transform_right2 = new Skill(SkillType.hunting, "hunting_transform_right2", Type.Transform,
			"파괴 대신 창조",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "가 더이상 광물을 파괴하지 않습니다.",
					ChatColor.WHITE + "25% 확률로 랜덤 광물로 변형됩니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { passive_right2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.25d });
	//
	public final Skill upgrade_left3 = new Skill(SkillType.hunting, "hunting_upgrade_left3", Type.Upgrade, "노련한 기본기",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + " 발동시 25% 확률로 재사용대기시간이 초기화됩니다." },
			new String[] { SkillType.hunting.leftSkillType, SkillType.hunting.middleSkillType },
			new Skill[] { transform_left3, transform_middle4 }, new Need[0], new Skill[0], new String[0],
			new double[] { 0.25d });
	public final Skill upgrade_middle4 = new Skill(SkillType.hunting, "hunting_upgrade_middle4", Type.Upgrade, "인싸 광물",
			new String[] {
					active4.getDisplayName() + ChatColor.WHITE + "으로 생성된 광물의 최대 추가 드랍 횟수가 발동시 발견한 광물 수 만큼 증가합니다.",
					active4.getDisplayName() + ChatColor.WHITE + "의 드랍 확률이 100%로 변경됩니다.",
					ChatColor.RED + "최대 5번까지 증가합니다." },
			new String[] { SkillType.hunting.middleSkillType, SkillType.hunting.rightSkillType },
			new Skill[] { transform_middle4, transform_right2 }, new Need[0], new Skill[0], new String[0]);
	public final Skill upgrade_right2 = new Skill(SkillType.hunting, "hunting_upgrade_right2", Type.Upgrade, "성장하는 블럭",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "으로 파괴 또는 변형된 광물의 최대 추가 드랍 횟수가 3회 증가합니다.",
					ChatColor.RED + "드랍 확률은 " + active4.getDisplayName() + ChatColor.RED + "의 확률을 따릅니다." },
			new String[] { SkillType.hunting.leftSkillType, SkillType.hunting.rightSkillType },
			new Skill[] { transform_right2, transform_left3 }, new Need[0], new Skill[0], new String[0], new double[0],
			new double[] { 3 });
	//
	public final Skill passive_left3 = new Skill(SkillType.hunting, "hunting_passive_left3", Type.Passive, "분노 교환",
			new String[] { ChatColor.WHITE + "분노 미발동시 10% 확률로 분노 스택 퍼센트 만큼 더 피해가 증가합니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { transform_left3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d });
	public final Skill passive_middle3 = new Skill(SkillType.hunting, "hunting_passive_middle3", Type.Passive, "다우징",
			new String[] { ChatColor.WHITE + "광물 좌클릭시 5% 확률로 7칸 범위내 동일 종류 광물을 찾아냅니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { transform_middle4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d }, new double[] { 3 });
	public final Skill passive_right3 = new Skill(SkillType.hunting, "hunting_passive_right3", Type.Passive, "자가 복제",
			new String[] { ChatColor.WHITE + "광물 파괴시 20% 확률로 주변 1칸 범위내 한블럭에 전염됩니다.",
					ChatColor.RED + "광물은 공기 혹은 관련 블럭으로만 전염됩니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { transform_right2 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.2d }, new double[] { 1, 1 });

	//
	public final Skill upgrade_left2_2 = new Skill(SkillType.hunting, "hunting_upgrade_left2_2", Type.Upgrade, "분노 폭발",
			new String[] {
					ChatColor.WHITE + "분노 모드에서 " + active2.getDisplayName() + ChatColor.WHITE + " 사용시 연계기가 강화됩니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { passive_left3 }, new Need[0], new Skill[0],
			new String[0]);
	public final Skill transform_left1 = new Skill(SkillType.hunting, "hunting_transform_left1", Type.Transform,
			"중첩되는 폭주",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로 " + active1.getDisplayName() + ChatColor.WHITE
					+ "의 성급함 레벨이 오릅니다.", ChatColor.GRAY + "최대 10 레벨" },
			new String[] { SkillType.hunting.leftSkillType, SkillType.hunting.middleSkillType },
			new Skill[] { passive_left3, passive_middle3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 9 });
	public final Skill upgrade_middle3_2 = new Skill(SkillType.hunting, "hunting_upgrade_middle3_2", Type.Upgrade,
			"히든 탐색",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + "으로 탐색시 1% 확률로 돌 블럭 속에 숨겨진 광물을 찾습니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { passive_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.01d });
	public final Skill transform_middle1 = new Skill(SkillType.hunting, "hunting_transform_middle1", Type.Transform,
			"증가되는 폭주",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로 " + active1.getDisplayName() + ChatColor.WHITE
					+ "의 지속시간이 30초 증가합니다.", ChatColor.GRAY + "최대 5번" },
			new String[] { SkillType.hunting.middleSkillType, SkillType.hunting.rightSkillType },
			new Skill[] { passive_middle3, passive_right3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 30, 5 });
	public final Skill upgrade_right4_2 = new Skill(SkillType.hunting, "hunting_upgrade_right4_2", Type.Upgrade,
			"중첩 연단", new String[] { ChatColor.WHITE + "연단으로 생성된 광물 파괴시 5% 확률로 연단 발동" },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { passive_right3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d });
	public final Skill transform_right1 = new Skill(SkillType.hunting, "hunting_transform_right1", Type.Transform,
			"감소된 휴식시간",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로" + active1.getDisplayName() + ChatColor.WHITE
					+ "의 재사용대기시간을 30초 감소시킵니다." + ChatColor.GRAY + "최대 5번" },
			new String[] { SkillType.hunting.leftSkillType, SkillType.hunting.rightSkillType },
			new Skill[] { passive_right3, passive_left3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 30, 5 });
	//
	public final Skill transform_left4 = new Skill(SkillType.hunting, "hunting_transform_left4", Type.Transform,
			"이것은 광물인가 폭탄인가",
			new String[] {
					ChatColor.WHITE + "변환 시킨 광물을 파괴시 " + active2.getDisplayName() + ChatColor.WHITE + "가 발동합니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { upgrade_left2_2 }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	public final Skill transform_middle2 = new Skill(SkillType.hunting, "hunting_transform_middle2", Type.Transform,
			"탐색 파괴",
			new String[] {
					ChatColor.WHITE + "탐색된 광물을 " + active2.getDisplayName() + ChatColor.WHITE
							+ "으로 파괴시 재사용대기시간이 10% 감소합니다.",
					ChatColor.WHITE + "25% 확률로 " + active3.getDisplayName() + ChatColor.WHITE + "이 발동합니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { upgrade_middle3_2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.25d }, new double[] { 0.1d });
	public final Skill transform_right3 = new Skill(SkillType.hunting, "hunting_transform_right3", Type.Transform,
			"성질 복사",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + "의 범위내 연단된 광물과 같은 종류의 광물들은 연단 옵션을 옮겨받습니다.",
					ChatColor.GRAY + "파괴시 얻는 최소-최대 추가 횟수 복제" },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { upgrade_right4_2 }, new Need[0],
			new Skill[] { active3 }, new String[0]);
	//
	public final Skill upgrade_left4 = new Skill(SkillType.hunting, "hunting_upgrade_left4", Type.Upgrade, "폭탄광",
			new String[] { transform_left4.getDisplayName() + ChatColor.WHITE + "에 의해 파괴된 광물 수에 비례해 "
					+ active4.getDisplayName() + ChatColor.WHITE + "의 재사용대기시간이 10%씩 감소합니다." },
			new String[] { SkillType.hunting.leftSkillType }, new Skill[] { transform_left4 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 0.1d });
	public final Skill upgrade_middle2 = new Skill(SkillType.hunting, "hunting_upgrade_middle2", Type.Upgrade, "경로 탐색",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "로 발생한 탐색이 탐색된 광물 근처 관련 블럭을 파괴합니다.",
					transform_middle2.getDisplayName() + ChatColor.GRAY + "의 탐사 발동 확률이 3배로 증가합니다.",
					ChatColor.WHITE + "추가로 탐색된 광물 파괴시 " + active3.getDisplayName() + "의 재사용대기시간도 10% 감소합니다." },
			new String[] { SkillType.hunting.middleSkillType }, new Skill[] { transform_middle2 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 3 });
	public final Skill upgrade_right3 = new Skill(SkillType.hunting, "hunting_upgrade_right3", Type.Upgrade, "전염병",
			new String[] { transform_right3.getDisplayName() + ChatColor.WHITE + " 적용시 1칸 범위내 한 블럭이 해당 광물로 바뀝니다." },
			new String[] { SkillType.hunting.rightSkillType }, new Skill[] { transform_right3 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 1, 1 });

	public SkillTree getSkillTree() {
		return SkillTree.getTree(active1, active2, active3, active4, upgrade1, minigame, passive, transform_left2,
				transform_middle3, transform_right4, upgrade_left2_1, upgrade_middle3_1, upgrade_right4_1,
				passive_left1, passive_middle1, passive_right1, passive_left2, passive_middle2, passive_right2,
				transform_left3, transform_middle4, transform_right2, upgrade_left3, upgrade_middle4, upgrade_right2,
				passive_left3, passive_middle3, passive_right3, upgrade_left2_2, upgrade_middle3_2, upgrade_right4_2,
				transform_left1, transform_middle1, transform_right1, transform_left4, transform_middle2,
				transform_right3, upgrade_left4, upgrade_middle2, upgrade_right3);
	}

	public UsableSkill[] getActives() {
		return new UsableSkill[] { active1, active2, active3, active4 };
	}

	public List<Skill> values() {
		if (list.size() == 0) {
			for (Skill skill : Skill.values())
				if (skill.getSkillType().equals(SkillType.hunting))
					list.add(skill);
		}
		return list;
	}

	public enum WeaponType {
		LongSword("장검") {
			@Override
			public boolean isThis(ItemStack item) {
				return item != null && item.getType().name().contains("SWORD") && item.hasItemMeta()
						&& item.getItemMeta().getCustomModelData() != 1011110;
			}
		},
		ShortSword("단검") {
			@Override
			public boolean isThis(ItemStack item) {
				return item != null && item.getType().name().contains("SWORD") && item.hasItemMeta()
						&& item.getItemMeta().getCustomModelData() == 1011110;
			}
		},
		Bow("단궁") {
			@Override
			public boolean isThis(ItemStack item) {
				return item != null && item.getType().equals(Material.BOW);
			}
		},
		Crossbow("석궁") {
			@Override
			public boolean isThis(ItemStack item) {
				return item != null && item.getType().equals(Material.CROSSBOW);
			}
		};

		//
		private final String name;

		private WeaponType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getColorName() {
			return ChatColor.GREEN + this.name;
		}

		public abstract boolean isThis(ItemStack item);

		public static WeaponType getWeaponType(ItemStack item) {
			for (WeaponType weaponType : values())
				if (weaponType.isThis(item))
					return weaponType;
			return null;
		}
	}

	public void setMaxAbsorption(Player player) {
		double value = 0;
		if (passive.hasLearn(player))
			value += passive.getEffect(1);
		player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(value);
	}

	public void setAbsorption(Player player, double value) {
		player.setAbsorptionAmount(value);
	}

	public int getAngry(Player player) {
		Integer angryl = ConfigStore.getPlayerConfig(player, "hunting.skills.angry");
		return angryl != null ? angryl : 0;
	}

	public int getMaxAngry(Player player) {
		int angry = 0;
		if (transform_left2.hasLearn(player))
			angry += transform_left2.getEffect();
		return angry;
	}

	public void setAngry(Player player, int angry) {
		ConfigStore.setPlayerConfig(player, "hunting.skills.angry", angry);
	}

	public double getAngryMultiply(Player player) {
		double multiply = 1d;
		if (transform_left2.hasLearn(player))
			multiply *= transform_left2.getEffect(2);
		if (upgrade_left2_1.hasLearn(player))
			multiply *= upgrade_left2_1.getEffect();
		return multiply;
	}
}
