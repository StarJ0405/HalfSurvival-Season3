package shining.starj.HalfSurvival.Skills;

import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class UsableSkill extends Skill {
	protected final long cooldown;
	protected final long duration;
	protected final useSlot useSlot;

	public UsableSkill(SkillType type, String key, String displayName, Skill[] preSkills, double cooldown,
			useSlot useSlot) {
		this(type, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	public UsableSkill(SkillType type, String key, String displayName, Skill[] preSkills, double cooldown,
			double duration, useSlot useSlot) {
		super(type, key, Type.Active, displayName, new String[0], new String[0], preSkills, new Need[0], new Skill[0],
				new String[0]);
		this.cooldown = (long) (cooldown * 1000l);
		this.duration = (long) (duration * 1000l);
		this.useSlot = useSlot;
		switch (useSlot) {
		case left:
			type.setLeft(this);
			break;
		case shiftLeft:
			type.setShiftLeft(this);
			break;
		case right:
			type.setRight(this);
			break;
		case shiftRight:
			type.setShiftRight(this);
			break;
		}
	}

	public boolean use(Player player) {
		if (HashMapStore.isNotDelayed(player)) {
			HashMapStore.setDelay(player);
			if (hasLearn(player)) {
				double remain = (getRemainUseTime(player) / 100l) / 10d;
				if (remain <= 0) {
					long cooldown = getCooldown(player);
					if (cooldown > 0) {
						setLastUseTime(player, cooldown / 1000d);
						HashMapStore.setCooldown(player, this, cooldown);
						return true;
					}
				} else
					player.sendMessage(this.displayName + ChatColor.WHITE + "의 재사용대기시간이 " + ChatColor.AQUA + remain
							+ ChatColor.WHITE + "초 남았습니다.");
			}
		}
		return false;
	}

	public long getCooldown(Player player) {
		return cooldown;
	}

	protected long getDuration(Player player) {
		return duration;
	}

	public void setLastUseTime(Player player, double cooldown) {
		ConfigStore.<Long>setPlayerConfig(player, this.type.name() + ".skills." + this.key + ".endCooldown",
				System.currentTimeMillis() + (long) (cooldown * 1000l));
	}

	public long getRemainUseTime(Player player) {
		Long time = ConfigStore.<Long>getPlayerConfig(player,
				this.type.name() + ".skills." + this.key + ".endCooldown");
		return (time != null ? time - System.currentTimeMillis() : 0l);
	}

	public void setDurationTime(Player player, double duration) {
		ConfigStore.<Long>setPlayerConfig(player, this.type.name() + ".skills." + this.key + ".endDuration",
				System.currentTimeMillis() + (long) (duration * 1000l));
	}

	public boolean hasDuration(Player player) {
		return getRemainDurationTime(player) > 0;
	}

	public long getRemainDurationTime(Player player) {
		Long time = ConfigStore.<Long>getPlayerConfig(player,
				this.type.name() + ".skills." + this.key + ".endDuration");
		return (time != null ? time - System.currentTimeMillis() : 0l);
	}

	@Override
	public ItemStack getItemStack(Player player) {
		ItemStack item = super.getItemStack(player);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
		long cooldown = getCooldown(player);
		if (cooldown > 0) {
			double remainUseTime = (getRemainUseTime(player) / 100l) / 10d;
			lore.add(ChatColor.WHITE + "재사용 대기시간 : " + (cooldown / 100l) / 10d + "초" + (hasLearn(player)
					? (remainUseTime <= 0 ? ChatColor.GREEN + " (사용 가능)" : ChatColor.GRAY + " (" + remainUseTime + ")")
					: ""));
		}
		long duration = getDuration(player);
		if (duration > 0) {
			double remainDurationTime = (getRemainDurationTime(player) / 100l) / 10d;
			lore.add(ChatColor.WHITE + "지속시간 : " + (duration / 100l) / 10d + "초"
					+ (hasLearn(player)
							? (remainDurationTime <= 0 ? ChatColor.RED + " (미사용중)" : " (" + remainDurationTime + ")")
							: ""));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public enum useSlot {
		left("좌클릭"), shiftLeft("쉬프트 좌클릭"), right("우클릭"), shiftRight("쉬프트 우클릭");

		private final String lore;

		private useSlot(String lore) {
			this.lore = lore;
		}

		public String getLore() {
			return ChatColor.GREEN + lore + "시 사용 가능";
		}
	}
}
