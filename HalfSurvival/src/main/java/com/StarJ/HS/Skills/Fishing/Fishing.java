package com.StarJ.HS.Skills.Fishing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.Skill.Need;
import com.StarJ.HS.Skills.Skill.Type;
import com.StarJ.HS.Skills.SkillTree;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Skills.UsableSkill.useSlot;
import com.StarJ.HS.Systems.SkillType;

public class Fishing {
	private final List<Skill> list = new ArrayList<Skill>();
	//
	public final Active1 active1 = new Active1("fishing_active1", "통발", new Skill[0], 60, 10, useSlot.shiftRight);
	//
	public final Skill upgrade1 = new Skill(SkillType.fishing, "fishing_upgrade1", Type.Upgrade, "미끼",
			new String[] { active1.getDisplayName() + ChatColor.WHITE + "의 성능이 돌 낚시대와 동일해집니다." }, new String[0],
			new Skill[] { active1 }, new Need[0], new Skill[0], new String[0], new double[0], new double[] { 0.5 });
	public final Skill minigame = new Skill(SkillType.fishing, "fishing_minigame", Type.Passive, "미니게임",
			new String[] { ChatColor.WHITE + "낚시 할때 10% 확률로 미니게임이 발생합니다.", ChatColor.WHITE + "쉬프트로 게임 진행 가능",
					ChatColor.GRAY + "성공시 +3개" },
			new String[0], new Skill[] { upgrade1 }, new Need[0], new Skill[0], new String[0], new double[] { 0.1d },
			new double[] { 3 });
	//
	public final Active2 active2 = new Active2("fishing_active2", "투망", new Skill[] { upgrade1 }, 60 * 2,
			useSlot.right);
	public final Active3 active3 = new Active3("fishing_active3", "행운", new Skill[] { upgrade1 }, 60 * 5,
			useSlot.shiftLeft);
	public final Active4 active4 = new Active4("fishing_active4", "비기", new Skill[] { upgrade1 }, 60 * 5, useSlot.left);
	//
	public final Skill passive = new Skill(SkillType.fishing, "fishing_passive1", Type.Passive, "향상심",
			new String[] { ChatColor.WHITE + "쓰레기가 나올 확률이 내려가고 보물이 나올 확률이 올라갑니다." }, new String[0],
			new Skill[] { active2, active3, active4 }, new Need[0], new Skill[0], new String[0], new double[0],
			new double[] { -0.05, 0.01d });
	//
	public final Skill transform_left2 = new Skill(SkillType.fishing, "fishing_transform_left2", Type.Transform,
			"짜릿한 손맛",
			new String[] { ChatColor.WHITE + "낚시 성공시 짜릿한 손맛 버프가 쌓입니다.",
					ChatColor.WHITE + "5스택시 " + active2.getDisplayName() + ChatColor.WHITE
							+ "을 재사용대기시간없이 한번 사용 가능합니다." },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[0], new double[] { 5 });
	public final Skill transform_middle3 = new Skill(SkillType.fishing, "fishing_transform_middle3", Type.Transform,
			"행운의 감",
			new String[] { ChatColor.WHITE + "낚시 성공시 33% 확률로 행운의 감 버프가 쌓입니다.",
					active3.getDisplayName() + ChatColor.WHITE + " 사용시 스택만큼 행운이 추가로 올라갑니다.", ChatColor.RED + "최대 3스택" },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[] { 1 / 3d }, new double[] { 3, 1 });
	public final Skill transform_right4 = new Skill(SkillType.fishing, "fishing_transform_right4", Type.Transform, "최속",
			new String[] { active4.getDisplayName() + ChatColor.WHITE + "의 낚는 속도가 4배로 빨라진다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { passive }, new Need[0],
			new Skill[] { active4 }, new String[0]);
	//
	public final Skill upgrade_left2_1 = new Skill(SkillType.fishing, "fishing_upgrade_left2_1", Type.Upgrade,
			ChatColor.WHITE + "질 좋은 그물",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "이 낚시대에 영향을 받습니다.",
					ChatColor.RED + "물고기 수확량이 2배 늘어납니다." },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { transform_left2 }, new Need[0],
			new Skill[0], new String[0]);
	public final Skill upgrade_middle3_1 = new Skill(SkillType.fishing, "fishing_upgrade_middle3_1", Type.Upgrade,
			"순간의 행운",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + " 미적용시 25% 확률로 "
					+ transform_middle3.getDisplayName() + ChatColor.WHITE + "의 스택만큼 행운이 적용됩니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { transform_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d });
	public final Skill upgrade_right4_1 = new Skill(SkillType.fishing, "fishing_upgrade_right4_1", Type.Upgrade, "초고속",
			new String[] { active4.getDisplayName() + ChatColor.WHITE + "가 25% 확률로 초고속으로 낚는다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { transform_right4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d });
	//
	public final Skill passive_left1 = new Skill(SkillType.fishing, "fishing_passive_left1", Type.Passive, "추가 획득",
			new String[] { ChatColor.WHITE + "낚시시 33% 확률로 낚인 물고기 마리수 추가",
					active3.getDisplayName() + ChatColor.RED + " 미적용" },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[] { "passive_middle1", "passive_right1" }, new double[] { 1 / 3d },
			new double[] { 1 });
	public final Skill passive_middle1 = new Skill(SkillType.fishing, "fishing_passive_middle1", Type.Passive, "소확행",
			new String[] { ChatColor.WHITE + "낚시시 25% 확률로 행운이 +1 증가합니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_right1" }, new double[] { 0.25d },
			new double[] { 1 });
	public final Skill passive_right1 = new Skill(SkillType.fishing, "fishing_passive_right1", Type.Passive, "복제기",
			new String[] { ChatColor.WHITE + "낚시대 찌 발사시 25% 확률로 찌를 3개 추가로 발사합니다.", ChatColor.RED + "추가 찌 존재시 비 발동",
					ChatColor.RED + "추가 찌는 낚시대를 내릴 시 없앨 수 있습니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[] { "passive_left1", "passive_middle1" }, new double[] { 0.25d },
			new double[] { 3 });
	//
	public final Skill passive_left2 = new Skill(SkillType.fishing, "fishing_passive_left2", Type.Passive, "또 한번의 기회",
			new String[] { ChatColor.WHITE + "33% 확률로 낚시를 이어 할 수 있습니다.", ChatColor.GRAY + "이어진 낚시는 기존보다 2배 빠르게 낚입니다.",
					active2.getDisplayName() + ChatColor.RED + " 및 " + active3.getDisplayName() + ChatColor.RED
							+ " 미적용" },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { upgrade_left2_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 1 / 3d });
	public final Skill passive_middle2 = new Skill(SkillType.fishing, "fishing_passive_middle2", Type.Passive,
			"현날한 손놀림", new String[] { ChatColor.WHITE + "낚시 시 25% 확률로 내구도를 10 회복합니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { upgrade_middle3_1 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d }, new double[] { 10 + 1 });
	public final Skill passive_right2 = new Skill(SkillType.fishing, "fishing_passive_right2", Type.Passive,
			"손은 빛 보다 빠르다", new String[] { ChatColor.WHITE + "물고기가 낚일 시 바로 수확합니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { upgrade_right4_1 }, new Need[0],
			new Skill[0], new String[0], new double[0]);
	//
	public final Skill transform_left3 = new Skill(SkillType.fishing, "fishing_transform_left3", Type.Transform, "어부지리",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 추가로 물고기를 획득합니다.",
					active3.getDisplayName() + ChatColor.RED + " 미적용" },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { passive_left2 }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[] { 0.25d });
	public final Skill transform_middle4 = new Skill(SkillType.fishing, "fishing_transform_middle4", Type.Transform,
			"실력과 행운을 동시에", new String[] { active4.getDisplayName() + ChatColor.WHITE + " 발동시 25% 확률로 행운이 +1 증가합니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { passive_middle2 }, new Need[0],
			new Skill[] { active4 }, new String[0], new double[] { 0.25d }, new double[] { 2 });
	public final Skill transform_right2 = new Skill(SkillType.fishing, "fishing_transform_right2", Type.Transform,
			"찌 뿌리기",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + " 대신 여러개의 낚시찌를 던집니다.",
					ChatColor.GREEN + "낚시대에 영향을 받습니다.",
					passive_right1.getDisplayName() + ChatColor.RED + "에 영향을 미치지 않습니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { passive_right2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[0]);
	//
	public final Skill upgrade_left3 = new Skill(SkillType.fishing, "fishing_upgrade_left3", Type.Upgrade, "더 많은",
			new String[] {
					transform_left3.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 "
							+ transform_left3.getDisplayName() + ChatColor.WHITE + "으로 낚인 물고기가 3배로 증가합니다.",
					transform_middle4.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 기본 물고기 획득량이 3배로 증가합니다." },
			new String[] { SkillType.fishing.leftSkillType, SkillType.fishing.middleSkillType },
			new Skill[] { transform_left3, transform_middle4 }, new Need[0], new Skill[0], new String[0],
			new double[] { 0.25d }, new double[] { 3 });
	public final Skill upgrade_middle4 = new Skill(SkillType.fishing, "fishing_upgrade_middle4", Type.Upgrade,
			"행운은 행운을 낳고",
			new String[] { transform_middle4.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 증가하는 행운의 양이 2배로 증가합니다.",
					transform_right2.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 찌의 개수가 2배 증가합니다." },
			new String[] { SkillType.fishing.middleSkillType, SkillType.fishing.rightSkillType },
			new Skill[] { transform_middle4, transform_right2 }, new Need[0], new Skill[0], new String[0],
			new double[] { 0.25d }, new double[] { 2 });
	public final Skill upgrade_right2 = new Skill(SkillType.fishing, "fishing_upgrade_right2", Type.Upgrade, "빠른 휴식",
			new String[] {
					transform_right2.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 " + active2.getDisplayName()
							+ ChatColor.WHITE + "의 재사용대기시간이 25% 감소합니다.",
					transform_left3.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 " + active3.getDisplayName()
							+ ChatColor.WHITE + " 재사용 대기시간이 25% 감소합니다." },
			new String[] { SkillType.fishing.leftSkillType, SkillType.fishing.rightSkillType },
			new Skill[] { transform_right2, transform_left3 }, new Need[0], new Skill[0], new String[0],
			new double[] { 0.25d }, new double[] { 0.25d });
	//
	public final Skill passive_left3 = new Skill(SkillType.fishing, "fishing_passive_left3", Type.Passive, "숙련가",
			new String[] { ChatColor.WHITE + "25% 확률로 낚시 성공시 물고기를 추가 회득합니다." },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { transform_left3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d });
	public final Skill passive_middle3 = new Skill(SkillType.fishing, "fishing_passive_middle3", Type.Passive, "현란한 미끼",
			new String[] { ChatColor.WHITE + "낚시찌가 있는 상태로 좌클릭시 3% 확률로 물고기 2마리를 낚습니다.",
					ChatColor.RED + "낚시찌당 최대 3번 가능" },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { transform_middle4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.03d }, new double[] { 2, 3 });
	public final Skill passive_right3 = new Skill(SkillType.fishing, "fishing_passive_right3", Type.Passive, "속도는 생명",
			new String[] { ChatColor.WHITE + "낚시시 5% 확률로 전체 쿨타임이 5% 감소합니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { transform_right2 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d }, new double[] { 0.05d });

	//
	public final Skill upgrade_left2_2 = new Skill(SkillType.fishing, "fishing_upgrade_left2_2", Type.Upgrade, "큰 그물",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "의 수확이 2번으로 증가합니다." },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { passive_left3 }, new Need[0], new Skill[0],
			new String[0], new double[0], new double[] { 2 });
	public final Skill transform_left1 = new Skill(SkillType.fishing, "fishing_transform_left1", Type.Transform, "큰 톤발",
			new String[] { active1.getDisplayName() + ChatColor.WHITE + "이 2종류의 물고기를 포획합니다." },
			new String[] { SkillType.fishing.leftSkillType, SkillType.fishing.middleSkillType },
			new Skill[] { passive_left3, passive_middle3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[0], new double[] { 2 });
	public final Skill upgrade_middle3_2 = new Skill(SkillType.fishing, "fishing_upgrade_middle3_2", Type.Upgrade,
			"초 행운", new String[] { active3.getDisplayName() + ChatColor.WHITE + " 발동시 5% 확률로 증가된 행운이 2배로 증가합니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { passive_middle3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.05d }, new double[] { 2 });
	public final Skill transform_middle1 = new Skill(SkillType.fishing, "fishing_transform_middle1", Type.Transform,
			"행운의 통발",
			new String[] { active1.getDisplayName() + ChatColor.WHITE + "에 행운이 적용됩니다.",
					ChatColor.RED + "설치 떄 적용되므로 주의!" },
			new String[] { SkillType.fishing.middleSkillType, SkillType.fishing.rightSkillType },
			new Skill[] { passive_middle3, passive_right3 }, new Need[0], new Skill[] { active1 }, new String[0]);
	public final Skill upgrade_right4_2 = new Skill(SkillType.fishing, "fishing_upgrade_right4_2", Type.Upgrade, "미끼",
			new String[] { ChatColor.WHITE + "낚시시 20% 확률로 미끼 스택가 쌓입니다.",
					ChatColor.WHITE + active4.getDisplayName() + ChatColor.WHITE + " 적용 상태로 낚시시 추가 찌를 날립니다.",
					ChatColor.RED + "최대 5스택까지 쌓입니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { passive_right3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.2d }, new double[] { 5 });
	public final Skill transform_right1 = new Skill(SkillType.fishing, "fishing_transform_right1", Type.Transform,
			"간이 통발", new String[] { active1.getDisplayName() + ChatColor.WHITE + "의 재사용대기시간이 25% 감소됩니다." },
			new String[] { SkillType.fishing.leftSkillType, SkillType.fishing.rightSkillType },
			new Skill[] { passive_right3, passive_left3 }, new Need[0], new Skill[] { active1 }, new String[0],
			new double[0], new double[] { 0.75d });
	//
	public final Skill transform_left4 = new Skill(SkillType.fishing, "fishing_transform_left4", Type.Transform,
			"비기:월척",
			new String[] { active4.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 추가로 물고기를 획득합니다.",
					active3.getDisplayName() + ChatColor.RED + " 미적용" },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { upgrade_left2_2 }, new Need[0],
			new Skill[] { active4 }, new String[0], new double[] { 0.25d });
	public final Skill transform_middle2 = new Skill(SkillType.fishing, "fishing_transform_middle2", Type.Transform,
			"행운의 그물", new String[] { active2.getDisplayName() + ChatColor.WHITE + "에 행운이 적용됩니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { upgrade_middle3_2 }, new Need[0],
			new Skill[] { active2 }, new String[0], new double[0], new double[0]);
	public final Skill transform_right3 = new Skill(SkillType.fishing, "fishing_transform_right3", Type.Transform,
			"자주 오는 행운",
			new String[] { active3.getDisplayName() + ChatColor.WHITE + " 발동시 25% 확률로 재사용대기시간이 25% 감소됩니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { upgrade_right4_2 }, new Need[0],
			new Skill[] { active3 }, new String[0], new double[] { 0.25d }, new double[] { 0.25d });
	//
	// "낚시시 " + active3.getDisplayName()+ ChatColor.WHITE + "의 재사용대기시간이 10% 감소합니다."
	public final Skill upgrade_left4 = new Skill(SkillType.fishing, "fishing_upgrade_left4", Type.Upgrade, "월척 오브 월척",
			new String[] { transform_left4.getDisplayName() + ChatColor.WHITE + " 적용시 25% 확률로 "
					+ transform_left4.getDisplayName() + ChatColor.WHITE + "으로 낚인 물고기가 3배로 증가합니다." },
			new String[] { SkillType.fishing.leftSkillType }, new Skill[] { transform_left4 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d }, new double[] { 3 });
	public final Skill upgrade_middle2 = new Skill(SkillType.fishing, "fishing_upgrade_middle2", Type.Upgrade, "행운의 가챠",
			new String[] { active2.getDisplayName() + ChatColor.WHITE + "이 행운만큼 추가로 획득합니다.",
					ChatColor.RED + "모든 물고기는 1마리씩 획득", ChatColor.RED + "사용시 행운이 0으로 내려갑니다." },
			new String[] { SkillType.fishing.middleSkillType }, new Skill[] { transform_middle2 }, new Need[0],
			new Skill[0], new String[0]);
	public final Skill upgrade_right3 = new Skill(SkillType.fishing, "fishing_upgrade_right3", Type.Upgrade, "가속",
			new String[] { transform_right3.getDisplayName() + ChatColor.WHITE
					+ " 발동시 25% 확률로 모든 사용스킬 재사용대기시간이 50% 감소로 변경됩니다." },
			new String[] { SkillType.fishing.rightSkillType }, new Skill[] { transform_right3 }, new Need[0],
			new Skill[0], new String[0], new double[] { 0.25d }, new double[] { 0.5d });

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
				if (skill.getSkillType().equals(SkillType.fishing))
					list.add(skill);
		}
		return list;
	}

	public final String[] woodenLootTables = new String[] { "gameplay/fishing/wooden/junk",
			"gameplay/fishing/wooden/fish", "gameplay/fishing/wooden/treasure" };
	public final String[] stoneLootTables = new String[] { "gameplay/fishing/stone/junk", "gameplay/fishing/stone/fish",
			"gameplay/fishing/stone/treasure" };
	public final String[] ironLootTables = new String[] { "gameplay/fishing/iron/junk", "gameplay/fishing/iron/fish",
			"gameplay/fishing/iron/treasure" };
	public final String[] goldenLootTables = new String[] { "gameplay/fishing/golden/junk",
			"gameplay/fishing/golden/fish", "gameplay/fishing/golden/treasure" };
	public final String[] diamondLootTables = new String[] { "gameplay/fishing/diamond/junk",
			"gameplay/fishing/diamond/fish", "gameplay/fishing/diamond/treasure" };
	public final String[] netheriteLootTables = new String[] { "gameplay/fishing/netherite/junk",
			"gameplay/fishing/netherite/fish", "gameplay/fishing/netherite/treasure" };

	private int getLootNumber(Player player) {
		Random r = new Random();
		double chance = r.nextDouble();
		boolean passive = this.passive.hasLearn(player);

		double junk = 0.69d + (passive ? this.passive.getEffect(0) : 0);
		double treasure = 0.01d + (passive ? this.passive.getEffect(1) : 0);
		if (chance < 0.2 + junk)
			return 0;
		else if (chance > (1 - treasure))
			return 2;
		else
			return 1;
	}

	private LootTable getLootTable(Player player, ItemStack tool) {
		int model = 0;
		if (tool != null && tool.getType().equals(Material.FISHING_ROD))
			model = tool.getItemMeta().getCustomModelData();
		int num = getLootNumber(player);
		switch (model) {
		case 1111011:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", stoneLootTables[num]));
		case 1111012:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", ironLootTables[num]));
		case 1111013:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", goldenLootTables[num]));
		case 1111014:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", diamondLootTables[num]));
		case 1111015:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", netheriteLootTables[num]));
		default:
			return Bukkit.getLootTable(new NamespacedKey("minecraft", woodenLootTables[num]));
		}
	}

	private LootContext getLootContext(Location base, Player player, float luck) {
		return new LootContext.Builder(base).killer(player).luck(luck).build();
	}

	public ItemStack getFish(ItemStack tool, Location base, Player player) {
		return getFish(tool, base, player, 0f);
	}

	public ItemStack getFish(ItemStack tool, Location base, Player player, float luck) {
		return getFish(getLootTable(player, tool), base, player, luck);
	}

	public ItemStack getFish(String[] keys, Location base, Player player) {
		return getFish(Bukkit.getLootTable(new NamespacedKey("minecraft", keys[getLootNumber(player)])), base, player,
				0f);
	}

	public ItemStack getFish(String[] keys, Location base, Player player, float luck) {
		return getFish(Bukkit.getLootTable(new NamespacedKey("minecraft", keys[getLootNumber(player)])), base, player,
				luck);
	}

	public ItemStack getFish(LootTable table, Location base, Player player) {
		return getFish(table, base, player, 0f);
	}

	public ItemStack getFish(LootTable table, Location base, Player player, float luck) {
		for (ItemStack fish : table.populateLoot(new Random(), getLootContext(base, player, luck)))
			return fish;
		return null;
	}
}
