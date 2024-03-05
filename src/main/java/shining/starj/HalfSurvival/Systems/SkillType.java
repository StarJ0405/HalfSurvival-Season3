package shining.starj.HalfSurvival.Systems;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.SkillTree;
import shining.starj.HalfSurvival.Skills.UsableSkill;

import java.util.ArrayList;
import java.util.List;

public enum SkillType {
	mining("광질", Material.IRON_PICKAXE, new String[] { "발굴", "탐색", "연단" }, 400) {
		// 파괴, 탐색, 변환
		@Override
		public boolean isItem(ItemStack item) {
			return item != null && item.getType().name().contains("PICKAXE");
		}

		@Override
		protected SkillTree createSkillTree() {
			return Skill.Mining.getSkillTree();
		}
	},
	farming("농사", Material.IRON_HOE, new String[] { "???", "수확", "!!!" }, 2000) {
		@Override
		public boolean isItem(ItemStack item) {
			return item != null && item.getType().name().contains("HOE");
		}

		@Override
		protected SkillTree createSkillTree() {
			return Skill.Farming.getSkillTree();
		}
	},
	hunting("사냥", Material.IRON_SWORD, new String[] { "분노", "혈기", "인내" }, 200) {
		// 중첩, 피흡, 기다림
		@Override
		public boolean isItem(ItemStack item) {
			return item != null && (item.getType().equals(Material.BOW) || item.getType().equals(Material.CROSSBOW)
					|| item.getType().name().contains("SWORD"));
		}

		@Override
		protected SkillTree createSkillTree() {
			return Skill.Hunting.getSkillTree();
		}
	},
	fishing("낚시", Material.FISHING_ROD, new String[] { "월척", "전문가", "달인" }, 300) {
		// 물고기량 증가, 확률, 빠른 낚시
		@Override
		public boolean isItem(ItemStack item) {
			return item != null && item.getType().equals(Material.FISHING_ROD);
		}

		@Override
		protected SkillTree createSkillTree() {
			return Skill.Fishing.getSkillTree();
		}
	}
	//
	;

	protected final String displayName;
	protected final Material material;
	public final String leftSkillType;
	public final String middleSkillType;
	public final String rightSkillType;
	protected SkillTree skillTree;
	protected final int fix;
	private UsableSkill left;
	private UsableSkill shift_left;
	private UsableSkill right;
	private UsableSkill shift_right;

	private SkillType(String displayName, Material material, String[] subSkillType, int fix) {
		this.displayName = displayName;
		this.material = material;
		this.skillTree = null;
		this.leftSkillType = subSkillType[0];
		this.middleSkillType = subSkillType[1];
		this.rightSkillType = subSkillType[2];
		this.fix = fix;
		this.left = null;
		this.shift_left = null;
		this.right = null;
		this.shift_right = null;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Material getMaterial() {
		return material;
	}

	public abstract boolean isItem(ItemStack item);

	public ItemStack getItemStack(Player player) {
		ItemStack item = new ItemStack(this.material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + this.displayName);
		meta.setLocalizedName(this.name());
		meta.setCustomModelData(1111110);
		meta.addItemFlags(ItemFlag.values());
		List<String> lore = new ArrayList<String>();
		int level = getLevel(player);
		int rank = getRank(player);
		lore.add(ChatColor.GREEN + "레벨 : " + level);
		lore.add(ChatColor.WHITE + "경험치 : "
				+ (level >= getMaxLevel() ? "최대레벨" : getExp(player) + " / " + getNeexExp(level, rank)));
		lore.add(getRankName(rank));
		lore.add(ChatColor.GRAY + "포인트 : " + Skill.getPoint(player, this) + " / " + (level / 3 + 1));
		lore.add(ChatColor.GRAY + this.leftSkillType + " : " + Skill.getPoint(player, this.leftSkillType) + " "
				+ this.middleSkillType + " : " + Skill.getPoint(player, this.middleSkillType) + " "
				+ this.rightSkillType + " : " + Skill.getPoint(player, this.rightSkillType));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public String getRankName(int rank) {
		switch (rank) {
		case 0:
			return ChatColor.LIGHT_PURPLE + "[주 스킬]";
		case 1:
			return ChatColor.DARK_PURPLE + "[보조 스킬]";
		case 2:
			return ChatColor.YELLOW + "[추가 스킬]";
		case 3:
			return ChatColor.WHITE + "[미사용 스킬]";
		default:
			return "";
		}

	}

	public void setLeft(UsableSkill left) {
		if (this.left == null)
			this.left = left;
	}

	public UsableSkill getLeft() {
		return this.left;
	}

	public void setShiftLeft(UsableSkill shift_left) {
		if (this.shift_left == null)
			this.shift_left = shift_left;
	}

	public UsableSkill getShiftLeft() {
		return this.shift_left;
	}

	public void setRight(UsableSkill right) {
		if (this.right == null)
			this.right = right;
	}

	public UsableSkill getRight() {
		return this.right;
	}

	public void setShiftRight(UsableSkill shift_right) {
		if (this.shift_right == null)
			this.shift_right = shift_right;
	}

	public UsableSkill getShiftRight() {
		return this.shift_right;
	}

	public SkillTree getSkillTree() {
		if (this.skillTree == null)
			skillTree = createSkillTree();
		return skillTree;
	}

	protected abstract SkillTree createSkillTree();

	public int getLevel(Player player) {
		Integer level = ConfigStore.<Integer>getPlayerConfig(player, this.name() + ".level");
		return level != null ? (level > getMaxLevel() ? getMaxLevel() : level) : 0;
	}

	public void setLevel(Player player, int level) {
		if (level < 0)
			level = 0;
		else if (level > 100)
			level = 100;
		ConfigStore.<Integer>setPlayerConfig(player, this.name() + ".level", level);
	}

	public int getExp(Player player) {
		Integer exp = ConfigStore.<Integer>getPlayerConfig(player, this.name() + ".exp");
		return exp != null ? exp : 0;
	}

	public void addExp(Player player, int add) {
		int level = getLevel(player);
		int exp = getExp(player) + add;
		int rank = getRank(player, level, exp);
		int need = getNeexExp(level, rank);
		while (exp >= need) {
			if (level < getMaxLevel()) {
				exp -= need;
				level += 1;
				rank = getRank(player, level, exp);
				need = getNeexExp(level, rank);
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
			} else {
				exp = 0;
				level = getMaxLevel();
				break;
			}
		}
		if (level >= getMaxLevel())
			exp = 0;
		setLevel(player, level);
		setExp(player, exp);
		((CraftPlayer) player).spigot().sendMessage(ChatMessageType.ACTION_BAR,
				TextComponent.fromLegacy(ChatColor.GOLD + this.displayName + ChatColor.GREEN + "<" + level + ">"
						+ ChatColor.WHITE + " : " + exp + " / " + need + ChatColor.GRAY + " ("
						+ exp * 10000 / need / 100d + "%)"));
		player.playSound(player.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}

	public void setExp(Player player, int exp) {
		ConfigStore.<Integer>setPlayerConfig(player, this.name() + ".exp", exp);
	}

	public int getRank(Player player) {
		return getRank(player, getLevel(player), getExp(player));
	}

	public int getRank(Player player, int level, int exp) {
		int rank = 0;
		for (SkillType type : values())
			if (!type.equals(this)) {
				int nowLevel = type.getLevel(player);
				if (level < nowLevel)
					rank++;
				else if (level == nowLevel) {
					int nowExp = type.getExp(player);
					if (exp < nowExp)
						rank++;
				}
			}
		return rank;
	}

	private double getRankedExp(int rank) {
		switch (rank) {
		case 1:
			return 1.25d;
		case 2:
			return 1.5;
		case 3:
			return 2d;
		}
		return 1d;
	}

	public int getNeexExp(Player player) {
		return getNeexExp(getLevel(player), getRank(player));
	}

	public int getMaxLevel() {
		return 99;
	}

	public int getNeexExp(int level, int rank) {
		return level >= getMaxLevel() ? Integer.MAX_VALUE
				: (int) ((Math.log(level + 1) * Math.log(level + 1) + 1) * (level / 3 + 1) * this.fix
						* getRankedExp(rank));
	}
}
