package shining.starj.HalfSurvival.Skills.Farming;

import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.Skill.Need;
import shining.starj.HalfSurvival.Skills.Skill.Type;
import shining.starj.HalfSurvival.Skills.SkillTree;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Skills.UsableSkill.useSlot;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Farming {
	private final List<Skill> list = new ArrayList<Skill>();
	//
	public final Active1 active1 = new Active1("farming_active1", "아드레날린", new Skill[0], 60 * 20, 60 * 5,
			useSlot.shiftLeft);
	//
	public final Skill upgrade1 = new Skill(SkillType.farming, "farming_upgrade1", Type.Upgrade, "폭주",
			new String[] { active1.getDisplayName() + ChatColor.WHITE + " 재사용 대기시간이 50% 감소합니다." }, new String[0],
			new Skill[] { active1 }, new Need[0], new Skill[0], new String[0], new double[0], new double[] { 0.5 });
	public final Skill minigame = new Skill(SkillType.farming, "farming_minigame", Type.Passive, "미니게임",
			new String[] { ChatColor.WHITE + "광물 파괴시 5% 확률로 미니게임이 발생합니다.", ChatColor.WHITE + "쉬프트로 게임 진행 가능",
					ChatColor.GRAY + "성공 횟수당 +1개씩 최대 3개 블럭 지급" },
			new String[0], new Skill[] { upgrade1 }, new Need[0], new Skill[0], new String[0], new double[] { 0.05d });
	//
	public final Active2 active2 = new Active2("farming_active2", "파워 스트라이크", new Skill[] { upgrade1 }, 60 * 2,
			useSlot.left);
	public final Active3 active3 = new Active3("farming_active3", "광물 탐색", new Skill[] { upgrade1 }, 60 * 4,
			useSlot.shiftRight);
	public final Active4 active4 = new Active4("farming_active4", "연금", new Skill[] { upgrade1 }, 60 * 5,
			useSlot.right);
	//
	public final Skill passive = new Skill(SkillType.farming, "farming_passive1", Type.Passive, "자동 수리",
			new String[] { ChatColor.WHITE + "5% 확률로 내구도를 10 회복합니다." }, new String[0],
			new Skill[] { active2, active3, active4 }, new Need[0], new Skill[0], new String[0], new double[] { 0.05d },
			new double[] { 11 });
	//
	public final Skill transform_left2 = new Skill(SkillType.farming, "farming_transform_left2", Type.Transform,
			"행운의 기회",
			new String[] {
					ChatColor.WHITE + "곡괭이로 관련 블럭 파괴시 20% 확률로 " + active2.getDisplayName() + ChatColor.WHITE + " 발동" },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.2d });
	public final Skill transform_middle3 = new Skill(SkillType.farming, "farming_transform_middle3", Type.Transform,
			"위치 파악", new String[] { ChatColor.WHITE + "탐색한 광물의 정확한 위치를 10초간 파악할 수 있습니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[0], new double[] { 10 });
	public final Skill transform_right4 = new Skill(SkillType.farming, "farming_transform_right4", Type.Transform,
			"등가 교환", new String[] { active4.getDisplayName() + ChatColor.WHITE + "이 들고 있는 광물로 변형으로 변경됩니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	//
	public final Skill upgrade_left2_1 = new Skill(SkillType.farming, "farming_upgrade_left2_1", Type.Upgrade,
			ChatColor.WHITE + "파워업", new String[] { ChatColor.WHITE + "파워스트라이크 발동시 범위가 정면으로 3배 늘어납니다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { transform_left2 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 3 });
	public final Skill upgrade_middle3_1 = new Skill(SkillType.farming, "farming_upgrade_middle3_1", Type.Upgrade,
			"정밀 탐색",
			new String[] { ChatColor.WHITE + "탐색한 광물의 종류를 색깔로 구분 가능합니다.",
					ChatColor.WHITE + "탐색 범위가 전방향으로 +3칸씩 증가합니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { transform_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 3 });
	public final Skill upgrade_right4_1 = new Skill(SkillType.farming, "farming_upgrade_right4_1", Type.Upgrade,
			"범위 증가",
			new String[] { active4.getDisplayName() + ChatColor.WHITE + "의 범위가 ±1칸씩 증가합니다.",
					ChatColor.WHITE + "추가 칸들은 각각 25% 확률로 변형됩니다.", ChatColor.RED + "소모되는 아이템의 수가 증가합니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { transform_right4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 1d, 0.25d }, new double[] { 1 });
	//
	public final Skill passive_left1 = new Skill(SkillType.farming, "farming_passive_left1", Type.Passive, "추가 획득",
			new String[] { ChatColor.WHITE + "광물 파괴시 20% 확률로 아이템 1번 더 드랍" },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[] { "passive_middle1", "passive_right1" }, new double[] { 0.2d },
			new double[] { 1 });
	public final Skill passive_middle1 = new Skill(SkillType.farming, "farming_passive_middle1", Type.Passive, "환경 저항",
			new String[] { ChatColor.WHITE + "블럭 파괴시 25% 확률로 2분동안 야간투시와 전달체의 힘 및 화염저항을 얻습니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_right1" }, new double[] { 0.25d },
			new double[] { 120 });
	public final Skill passive_right1 = new Skill(SkillType.farming, "farming_passive_right1", Type.Passive, "호문쿨루스",
			new String[] { ChatColor.WHITE + "광물 파괴시 20% 확률로 광물을 복제합니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_middle1" }, new double[] { 0.2d });
	//
	public final Skill passive_left2 = new Skill(SkillType.farming, "farming_passive_left2", Type.Passive, "또 한번의 기회",
			new String[] { ChatColor.WHITE + "20% 확률로 광물을 다시 한번 캘 수 있습니다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.2d });
	public final Skill passive_middle2 = new Skill(SkillType.farming, "farming_passive_middle2", Type.Passive,
			"신속한 움직임", new String[] { ChatColor.WHITE + "블럭 파괴시 25% 확률로 2분동안 이동속도와 점프력이 증가합니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d }, new double[] { 120d });
	public final Skill passive_right2 = new Skill(SkillType.farming, "farming_passive_right2", Type.Passive, "찾았다 이놈!",
			new String[] { ChatColor.WHITE + "관련 블럭 파괴시 10% 확률로 랜덤 광물로 변형됩니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.1d });
	//
	public final Skill transform_left3 = new Skill(SkillType.farming, "farming_transform_left3", Type.Transform,
			"발굴 탐색",
			new String[] { ChatColor.WHITE + "25% 확률로 탐색한 광물 캡니다.", ChatColor.WHITE + "아이템은 해당 자리에 떨어지므로 잘 찾아주세요!" },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { passive_left2 }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[] { 0.25d });
	public final Skill transform_middle4 = new Skill(SkillType.farming, "farming_transform_middle4", Type.Transform,
			"친구 찾기",
			new String[] {
					active4.getDisplayName() + ChatColor.WHITE + " 발동시 해당 블럭 기준으로 " + active3.getDisplayName()
							+ ChatColor.WHITE + "을 발동합니다.",
					ChatColor.RED + "단, " + active4.getDisplayName() + ChatColor.RED + "로 변형시킨 종류의 블럭만 탐색합니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { passive_middle2 }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	public final Skill transform_right2 = new Skill(SkillType.farming, "farming_transform_right2", Type.Transform,
			"파괴 대신 창조",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "가 더이상 광물을 파괴하지 않습니다.",
					ChatColor.WHITE + "25% 확률로 랜덤 광물로 변형됩니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { passive_right2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.25d });
	//
	public final Skill upgrade_left3 = new Skill(SkillType.farming, "farming_upgrade_left3", Type.Upgrade, "뛰어난 탐색",
			new String[] { ChatColor.WHITE + "광물 발굴시 25% 확률로 " + active3.getDisplayName() + ChatColor.WHITE
					+ "의 재사용대기시간이 25% 감소합니다." },
			new String[] { SkillType.farming.leftSkillType, SkillType.farming.middleSkillType },
			new Skill[] { transform_left3, transform_middle4 }, new Need[0], new Skill[0], new String[0],
			new double[] { 0.25d }, new double[] { 0.25d });
	public final Skill upgrade_middle4 = new Skill(SkillType.farming, "farming_upgrade_middle4", Type.Upgrade, "인싸 광물",
			new String[] {
					active4.getDisplayName() + ChatColor.WHITE + "으로 생성된 광물의 최대 추가 드랍 횟수가 발동시 발견한 광물 수 만큼 증가합니다.",
					active4.getDisplayName() + ChatColor.WHITE + "의 드랍 확률이 100%로 변경됩니다.",
					ChatColor.RED + "최대 5번까지 증가합니다." },
			new String[] { SkillType.farming.middleSkillType, SkillType.farming.rightSkillType },
			new Skill[] { transform_middle4, transform_right2 }, new Need[0], new Skill[0], new String[0]);
	public final Skill upgrade_right2 = new Skill(SkillType.farming, "farming_upgrade_right2", Type.Upgrade, "성장하는 블럭",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "으로 파괴 또는 변형된 광물의 최대 추가 드랍 횟수가 3회 증가합니다.",
					ChatColor.RED + "드랍 확률은 " + active4.getDisplayName() + ChatColor.RED + "의 확률을 따릅니다." },
			new String[] { SkillType.farming.leftSkillType, SkillType.farming.rightSkillType },
			new Skill[] { transform_right2, transform_left3 }, new Need[0], new Skill[0], new String[0], new double[0],
			new double[] { 3 });
	//
	public final Skill passive_left3 = new Skill(SkillType.farming, "farming_passive_left3", Type.Passive, "빈틈 포착",
			new String[] { ChatColor.WHITE + "관련 블럭 좌클릭시 5% 확률로 블럭을 파괴합니다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { transform_left3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d });
	public final Skill passive_middle3 = new Skill(SkillType.farming, "farming_passive_middle3", Type.Passive, "다우징",
			new String[] { ChatColor.WHITE + "광물 좌클릭시 5% 확률로 7칸 범위내 동일 종류 광물을 찾아냅니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { transform_middle4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d }, new double[] { 3 });
	public final Skill passive_right3 = new Skill(SkillType.farming, "farming_passive_right3", Type.Passive, "자가 복제",
			new String[] { ChatColor.WHITE + "광물 파괴시 20% 확률로 주변 1칸 범위내 한블럭에 전염됩니다.",
					ChatColor.RED + "광물은 공기 혹은 관련 블럭으로만 전염됩니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { transform_right2 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.2d }, new double[] { 1, 1 });

	//
	public final Skill upgrade_left2_2 = new Skill(SkillType.farming, "farming_upgrade_left2_2", Type.Upgrade,
			"또 하나의 곡괭이", new String[] { active2.getDisplayName() + ChatColor.WHITE + " 사용시의 내구도 소모를 없애준다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { passive_left3 }, new Need[0], new Skill[0],
			new String[0]);
	public final Skill transform_left1 = new Skill(SkillType.farming, "farming_transform_left1", Type.Transform,
			"중첩되는 폭주",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로 " + active1.getDisplayName() + ChatColor.WHITE
					+ "의 성급함 레벨이 오릅니다.", ChatColor.GRAY + "최대 10 레벨" },
			new String[] { SkillType.farming.leftSkillType, SkillType.farming.middleSkillType },
			new Skill[] { passive_left3, passive_middle3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 9 });
	public final Skill upgrade_middle3_2 = new Skill(SkillType.farming, "farming_upgrade_middle3_2", Type.Upgrade,
			"히든 탐색",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + "으로 탐색시 1% 확률로 돌 블럭 속에 숨겨진 광물을 찾습니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { passive_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.01d });
	public final Skill transform_middle1 = new Skill(SkillType.farming, "farming_transform_middle1", Type.Transform,
			"증가되는 폭주",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로 " + active1.getDisplayName() + ChatColor.WHITE
					+ "의 지속시간이 30초 증가합니다.", ChatColor.GRAY + "최대 5번" },
			new String[] { SkillType.farming.middleSkillType, SkillType.farming.rightSkillType },
			new Skill[] { passive_middle3, passive_right3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 30, 5 });
	public final Skill upgrade_right4_2 = new Skill(SkillType.farming, "farming_upgrade_right4_2", Type.Upgrade,
			"중첩 연단", new String[] { ChatColor.WHITE + "연단으로 생성된 광물 파괴시 5% 확률로 연단 발동" },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { passive_right3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d });
	public final Skill transform_right1 = new Skill(SkillType.farming, "farming_transform_right1", Type.Transform,
			"감소된 휴식시간",
			new String[] { ChatColor.WHITE + "블럭 파괴시 10% 확률로" + active1.getDisplayName() + ChatColor.WHITE
					+ "의 재사용대기시간을 30초 감소시킵니다." + ChatColor.GRAY + "최대 5번" },
			new String[] { SkillType.farming.leftSkillType, SkillType.farming.rightSkillType },
			new Skill[] { passive_right3, passive_left3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[] { 0.1d }, new double[] { 30, 5 });
	//
	public final Skill transform_left4 = new Skill(SkillType.farming, "farming_transform_left4", Type.Transform,
			"이것은 광물인가 폭탄인가",
			new String[] {
					ChatColor.WHITE + "변환 시킨 광물을 파괴시 " + active2.getDisplayName() + ChatColor.WHITE + "가 발동합니다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { upgrade_left2_2 }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	public final Skill transform_middle2 = new Skill(SkillType.farming, "farming_transform_middle2", Type.Transform,
			"탐색 파괴",
			new String[] {
					ChatColor.WHITE + "탐색된 광물을 " + active2.getDisplayName() + ChatColor.WHITE
							+ "으로 파괴시 재사용대기시간이 10% 감소합니다.",
					ChatColor.WHITE + "25% 확률로 " + active3.getDisplayName() + ChatColor.WHITE + "이 발동합니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { upgrade_middle3_2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[] { 0.25d }, new double[] { 0.1d });
	public final Skill transform_right3 = new Skill(SkillType.farming, "farming_transform_right3", Type.Transform,
			"성질 복사",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + "의 범위내 연단된 광물과 같은 종류의 광물들은 연단 옵션을 옮겨받습니다.",
					ChatColor.GRAY + "파괴시 얻는 최소-최대 추가 횟수 복제" },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { upgrade_right4_2 }, new Need[0],
			new Skill[] { active3 }, new String[0]);
	//
	public final Skill upgrade_left4 = new Skill(SkillType.farming, "farming_upgrade_left4", Type.Upgrade, "폭탄광",
			new String[] { transform_left4.getDisplayName() + ChatColor.WHITE + "에 의해 파괴된 광물 수에 비례해 "
					+ active4.getDisplayName() + ChatColor.WHITE + "의 재사용대기시간이 10%씩 감소합니다." },
			new String[] { SkillType.farming.leftSkillType }, new Skill[] { transform_left4 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 0.1d });
	public final Skill upgrade_middle2 = new Skill(SkillType.farming, "farming_upgrade_middle2", Type.Upgrade, "경로 탐색",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "로 발생한 탐색이 탐색된 광물 근처 관련 블럭을 파괴합니다.",
					transform_middle2.getDisplayName() + ChatColor.GRAY + "의 탐사 발동 확률이 3배로 증가합니다.",
					ChatColor.WHITE + "추가로 탐색된 광물 파괴시 " + active3.getDisplayName() + "의 재사용대기시간도 10% 감소합니다." },
			new String[] { SkillType.farming.middleSkillType }, new Skill[] { transform_middle2 }, new Need[0],
			new Skill[0], new String[0], new double[0], new double[] { 3 });
	public final Skill upgrade_right3 = new Skill(SkillType.farming, "farming_upgrade_right3", Type.Upgrade, "전염병",
			new String[] { transform_right3.getDisplayName() + ChatColor.WHITE + " 적용시 1칸 범위내 한 블럭이 해당 광물로 바뀝니다." },
			new String[] { SkillType.farming.rightSkillType }, new Skill[] { transform_right3 }, new Need[0],
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
				if (skill.getSkillType().equals(SkillType.farming))
					list.add(skill);
		}
		return list;
	}
}
