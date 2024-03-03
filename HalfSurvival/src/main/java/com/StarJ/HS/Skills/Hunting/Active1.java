package com.StarJ.HS.Skills.Hunting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

import com.StarJ.HS.Core;
import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Skills.Hunting.Hunting.WeaponType;
import com.StarJ.HS.Systems.SkillType;

public class Active1 extends UsableSkill {

	public Active1(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.hunting, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		if (Skill.Hunting.upgrade1.hasLearn(player)) {
			lore.add(WeaponType.LongSword.getColorName() + ChatColor.WHITE + " : 전방으로 뛰어 주변을 공격합니다.");
			lore.add(WeaponType.ShortSword.getColorName() + ChatColor.WHITE + " : 직선으로 빠르게 벱니다.");
			lore.add(WeaponType.Bow.getColorName() + ChatColor.WHITE + " : 뛰로 넓게 뛰며 주변에 화살을 흩뿌린다.");
			lore.add(WeaponType.Crossbow.getColorName() + ChatColor.WHITE + " : 후퇴하며 전방에 화살을 연속으로 쏜다.");
		} else {
			lore.add(WeaponType.LongSword.getColorName() + ChatColor.WHITE + " : 전방으로 뜁니다.");
			lore.add(WeaponType.ShortSword.getColorName() + ChatColor.WHITE + " : 직선으로 빠르게 뜁니다.");
			lore.add(WeaponType.Bow.getColorName() + ChatColor.WHITE + " : 뛰로 넓게 뛴다.");
			lore.add(WeaponType.Crossbow.getColorName() + ChatColor.WHITE + " : 뒤로 약진한다.");
		}
		return lore;
	}

	@Override
	public boolean use(Player player) {
		if (super.use(player)) {
			boolean upgrade1 = Skill.Hunting.upgrade1.hasLearn(player);
			ItemStack off = player.getInventory().getItemInOffHand();
			WeaponType type = WeaponType.getWeaponType(off);

			player.playSound(player.getEyeLocation(), Sound.ENTITY_GOAT_LONG_JUMP, 2f, 2f);
			Vector dir = player.getLocation().getDirection();
			dir.setY(0);
			dir.normalize().multiply(2);
			player.setVelocity(new Vector(0, 1, 0));
			switch (type) {
			case LongSword: {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.setVelocity(dir);
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						player.setVelocity(new Vector());
						if (upgrade1) {
							Location now = player.getLocation();
							List<LivingEntity> targets = new ArrayList<LivingEntity>();
							now.getWorld().spawnParticle(Particle.SWEEP_ATTACK, now, 100, 1.5, 1.5, 1.5);
							now.getWorld().playSound(now, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);
							for (Entity et : now.getWorld().getNearbyEntities(now, 3.5, 3.5, 3.5))
								if (!targets.contains(et) && et instanceof LivingEntity && !(et instanceof Player)) {
									LivingEntity livingEntity = (LivingEntity) et;
									targets.add(livingEntity);
									damage(player, livingEntity, 5d);
								}
						}
					}, 6);
				}, 2);
			}
				break;
			case ShortSword: {
				dir.multiply(1.5d);
				Location start = player.getLocation();
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.setVelocity(dir);
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						player.setVelocity(new Vector());
						if (upgrade1) {
							Location end = player.getLocation();
							Vector dif = end.clone().subtract(start).toVector().normalize();
							List<LivingEntity> targets = new ArrayList<LivingEntity>();
							for (int i = 0; i < Math.ceil(start.distance(end)); i++) {
								Location now = start.clone().add(dif.clone().multiply(i)).add(0, 0.25, 0);
								now.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 5, 0.1, 0.1, 0.1, 0);
								now.getWorld().playSound(now, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5f, 2f);
								for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
									if (!targets.contains(et) && et instanceof LivingEntity
											&& !(et instanceof Player)) {
										LivingEntity livingEntity = (LivingEntity) et;
										targets.add(livingEntity);
										damage(player, livingEntity, 5d);
									}
							}
						}
					}, 10);
				}, 2);
			}
				break;
			case Bow: {
				dir.multiply(0.5d);
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.setVelocity(dir.multiply(-2));
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						player.setVelocity(new Vector());
						if (upgrade1) {
							final int size = 1;
							for (int x = -size; x <= size; x++)
								for (int y = -size; y <= size; y++)
									for (int z = -size; z <= size; z++) {
										Arrow arrow = player.launchProjectile(Arrow.class);
										Vector vel = arrow.getVelocity();
										if (x != 0 || y != 0 || z != 0)
											vel.add(new Vector(x, y, z).normalize().multiply(0.5d));
										arrow.setVelocity(vel);
										arrow.setMetadata("damage", new FixedMetadataValue(Core.getCore(), 5));
										arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
										arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
										arrow.setPickupStatus(PickupStatus.DISALLOWED);
										Bukkit.getPluginManager()
												.callEvent(new EntityShootBowEvent(player, off,
														new ItemStack(Material.ARROW), arrow, EquipmentSlot.OFF_HAND,
														(float) vel.length(), false));
									}
						}
					}, 6);
				}, 2);

			}
				break;
			case Crossbow: {
//				CrossbowMeta meta = (CrossbowMeta) off.getItemMeta();
//				ItemStack arrow = new ItemStack(Material.ARROW);
//				meta.addChargedProjectile(arrow);
//				meta.addChargedProjectile(arrow);
//				meta.addChargedProjectile(arrow);
//				off.setItemMeta(meta);
				dir.multiply(0.5d);
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.setVelocity(dir.multiply(-1));
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						player.setVelocity(new Vector());
						if (upgrade1) {
							Arrow arrow = player.launchProjectile(Arrow.class);
							arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
							arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
							arrow.setVelocity(arrow.getVelocity().multiply(1.5d));
							arrow.setPickupStatus(PickupStatus.DISALLOWED);
							Bukkit.getPluginManager()
									.callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW),
											arrow, EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(),
											false));
						}
					}, 10);
				}, 2);

			}
				break;
			}
			player.getInventory().setItemInOffHand(player.getInventory().getItemInMainHand());
			player.getInventory().setItemInMainHand(off);
		}
		return true;
	}
}
