package shining.starj.HalfSurvival.Skills.Hunting;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Skills.Hunting.Hunting.WeaponType;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Active3 extends UsableSkill {

	public Active3(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.hunting, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(WeaponType.LongSword.getColorName() + ChatColor.WHITE + " : X자로 적에게 두번 피해를 준다.");
		lore.add(WeaponType.ShortSword.getColorName() + ChatColor.WHITE + " : 두번 빠르게 찌른다.");
		lore.add(WeaponType.Bow.getColorName() + ChatColor.WHITE + " : 부채꼴로 화살을 5발 발사합니다.");
		lore.add(WeaponType.Crossbow.getColorName() + ChatColor.WHITE + " : 화살을 두번 쏩니다.");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			if (Skill.Hunting.upgrade_left3.confirmChance(player)) {
				setLastUseTime(player, 0);
				HashMapStore.setCooldown(player, this, 0);
				HashMapStore.sendCooldownMessage(player, this);
				player.sendMessage(Skill.Hunting.upgrade_left3.getDisplayName() + ChatColor.WHITE + " 발동");
			}
			ItemStack off = player.getInventory().getItemInOffHand();
			WeaponType type = WeaponType.getWeaponType(off);
			if (!HashMapStore.isAngryStatus(player) && Skill.Hunting.transform_left3.confirmChance(player)) {
				int angry = Skill.Hunting.getAngry(player);
				int max = Skill.Hunting.getMaxAngry(player);
				angry += 1;
				if (angry > max)
					angry = max;
				Skill.Hunting.setAngry(player, angry);
				player.sendMessage(Skill.Hunting.transform_left3.getDisplayName() + ChatColor.WHITE + " 발동 "
						+ ChatColor.RED + angry + " / " + max);
				player.playSound(player, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 2f, 2f);
				player.spawnParticle(Particle.REDSTONE, player.getEyeLocation(), 10, 0.5, 0.5, 0.5,
						new DustOptions(Color.RED, 1));
			}
			switch (type) {
			case LongSword: {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 2f);
					Location loc = player.getEyeLocation().clone().subtract(0, 0.25d, 0);
					Vector dir = loc.getDirection();
					List<LivingEntity> list = new ArrayList<LivingEntity>();
					for (Vector mod : long_sword) {
						Location now = loc.clone().add(getModifiedVector(dir, mod.setY(-mod.getY())));
						player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, now, 1, 0, 0, 0);
						for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
							if (et instanceof LivingEntity && !(et instanceof Player)) {
								LivingEntity livingEntity = (LivingEntity) et;
								if (!list.contains(et)) {
									list.add(livingEntity);
									damage(player, livingEntity, 5d);
								}
							}
					}
					for (Entity et : loc.getWorld().getNearbyEntities(loc.clone().add(dir), 0.75, 0.75, 0.75))
						if (et instanceof LivingEntity && !(et instanceof Player)) {
							LivingEntity livingEntity = (LivingEntity) et;
							if (!list.contains(et)) {
								list.add(livingEntity);
								damage(player, livingEntity, 5d);
							}
						}
				}, 10);
				player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 2f);
				Location loc = player.getEyeLocation().clone().subtract(0, 0.25d, 0);
				Vector dir = loc.getDirection();
				List<LivingEntity> list = new ArrayList<LivingEntity>();
				for (Vector mod : long_sword) {
					Location now = loc.clone().add(getModifiedVector(dir, mod));
					player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, now, 1, 0, 0, 0);
					for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
						if (et instanceof LivingEntity && !(et instanceof Player)) {
							LivingEntity livingEntity = (LivingEntity) et;
							if (!list.contains(et)) {
								list.add(livingEntity);
								damage(player, livingEntity, 5d);
							}
						}
				}
				for (Entity et : loc.getWorld().getNearbyEntities(loc.clone().add(dir), 0.75, 0.75, 0.75))
					if (et instanceof LivingEntity && !(et instanceof Player)) {
						LivingEntity livingEntity = (LivingEntity) et;
						if (!list.contains(et)) {
							list.add(livingEntity);
							damage(player, livingEntity, 5d);
						}
					}
			}
				break;
			case ShortSword: {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
					Location loc = player.getEyeLocation().clone().subtract(0, 0.25d, 0);
					Vector dir = loc.getDirection();
					List<LivingEntity> list = new ArrayList<LivingEntity>();
					for (Vector mod : short_sword) {
						Location now = loc.clone().add(getModifiedVector(dir, mod.setY(-mod.getY())));
						player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 5, 0.1, 0.1, 0.1, 0);
						for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
							if (et instanceof LivingEntity && !(et instanceof Player)) {
								LivingEntity livingEntity = (LivingEntity) et;
								if (!list.contains(et)) {
									list.add(livingEntity);
									damage(player, livingEntity, 5d);
								}
							}
					}
					for (Entity et : loc.getWorld().getNearbyEntities(loc.clone().add(dir), 0.75, 0.75, 0.75))
						if (et instanceof LivingEntity && !(et instanceof Player)) {
							LivingEntity livingEntity = (LivingEntity) et;
							if (!list.contains(et)) {
								list.add(livingEntity);
								damage(player, livingEntity, 5d);
							}
						}
				}, 7);
				player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
				Location loc = player.getEyeLocation().clone().subtract(0, 0.25d, 0);
				Vector dir = loc.getDirection();
				List<LivingEntity> list = new ArrayList<LivingEntity>();
				for (Vector mod : short_sword) {
					Location now = loc.clone().add(getModifiedVector(dir, mod));
					player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 5, 0.1, 0.1, 0.1, 0);
					for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
						if (et instanceof LivingEntity && !(et instanceof Player)) {
							LivingEntity livingEntity = (LivingEntity) et;
							if (!list.contains(et)) {
								list.add(livingEntity);
								damage(player, livingEntity, 5d);
							}
						}
				}
			}
				break;
			case Bow: {
				Vector dir = player.getLocation().getDirection();
				Vector vertical = new Vector(dir.getZ(), 0, dir.getX()).normalize().multiply(0.05d);
				for (int i = -2; i <= 2; i++) {
					Arrow arrow = player.launchProjectile(Arrow.class, dir.clone().add(vertical.clone().multiply(i))
							.setY(0).normalize().setY(dir.getY()).multiply(3));
					arrow.setPickupStatus(PickupStatus.DISALLOWED);
					arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
					Bukkit.getPluginManager()
							.callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW), arrow,
									EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(), false));
				}
			}
				break;
			case Crossbow: {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.setPickupStatus(PickupStatus.DISALLOWED);
					arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
					Bukkit.getPluginManager()
							.callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW), arrow,
									EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(), false));
				}, 7);
				Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setPickupStatus(PickupStatus.DISALLOWED);
				arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
				arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
				Bukkit.getPluginManager().callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW),
						arrow, EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(), false));
			}
				break;
			}
			player.getInventory().setItemInOffHand(player.getInventory().getItemInMainHand());
			player.getInventory().setItemInMainHand(off);
		}
		return true;
	}

	Vector[] long_sword = new Vector[] { new Vector(4, 0, 0), //
			new Vector(3.6, 0.4, 0.4), new Vector(3.6, -0.4, -0.4), //
			new Vector(3.2, 0.8, 0.8), new Vector(3.2, -0.8, -0.8), //
			new Vector(2.8, 1.2, 1.2), new Vector(2.8, -1.2, -1.2), //
			new Vector(2.4, 1.6, 1.6), new Vector(2.4, -1.6, -1.6), //
			new Vector(2.0, 2.0, 2.0), new Vector(2.0, -2.0, -2.0) };
	Vector[] short_sword = new Vector[] { new Vector(2, 0, 0), //
			new Vector(1.6, 0, 0), new Vector(1.2, 0, 0), //
			new Vector(0.8, 0, 0), new Vector(0.4, 0, 0)//
	};
}
