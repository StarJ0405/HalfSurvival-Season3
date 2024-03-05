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
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Active2 extends UsableSkill {

	public Active2(String key, String displayName, Skill[] preSkills, double cooldown, double duration,
			useSlot useSlot) {
		super(SkillType.hunting, key, displayName, preSkills, cooldown, duration, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		lore.add(WeaponType.LongSword.getColorName() + ChatColor.WHITE + " : 검기를 날립니다. 2번까지 사용 가능합니다.");
		lore.add(WeaponType.ShortSword.getColorName() + ChatColor.WHITE + " : 단검을 던지며 재사용시 단검을 회수를 하며 피해를줍니다.");
		lore.add(WeaponType.Bow.getColorName() + ChatColor.WHITE + " : 화살 뭉덩이를 쏘며 재사용시 해당 화살 기준으로 화살들이 쁌어져나옵니다.");
		lore.add(WeaponType.Crossbow.getColorName() + ChatColor.WHITE + " : 저격모드가 실행되며 재사용시 강력한 화살을 발사한다.");
		return lore;
	}

	class swordArrow extends BukkitRunnable {
		private final int maxTime;
		private final Vector dir;
		private final Location loc;
		private final List<LivingEntity> list;
		private final Player att;
		private int time;

		public swordArrow(int maxTime, Vector dir, Location loc, Player att) {
			this.maxTime = maxTime;
			this.dir = dir;
			this.loc = loc;
			this.list = new ArrayList<LivingEntity>();
			this.att = att;
			this.time = 0;
		}

		@Override
		public void run() {
			if (this.time == 0)
				att.playSound(att, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2f, 0.5f);
			this.time++;
			this.loc.add(this.dir);
			for (Vector mod : longSword) {
				Location now = this.loc.clone().add(getModifiedVector(this.dir, mod));
				now.getWorld().spawnParticle(Particle.SWEEP_ATTACK, now, 1, 0, 0, 0);
				for (Entity et : now.getWorld().getNearbyEntities(now, 0.75d, 0.75d, 0.75d))
					if (et instanceof LivingEntity && !(et instanceof Player)) {
						LivingEntity livingEntity = (LivingEntity) et;
						if (!list.contains(livingEntity)) {
							list.add(livingEntity);
							damage(att, livingEntity, 5d);
						}
					}
			}
			if (this.time > this.maxTime) {
				this.cancel();
				return;
			}
		}

	}

	Vector[] longSword = new Vector[] { new Vector(0, 0, 0), //
			new Vector(0, 0, 1), new Vector(0, 0, -1), //
			new Vector(0, 0, 2), new Vector(0, 0, -2) };

	@Override
	public boolean use(Player player) {
		if (hasDuration(player) && HashMapStore.isNotDelayed(player)) {
			if (!HashMapStore.isAngryStatus(player) && Skill.Hunting.transform_left2.confirmChance(player)) {
				int angry = Skill.Hunting.getAngry(player);
				int max = Skill.Hunting.getMaxAngry(player);
				angry += 1;
				if (angry > max)
					angry = max;
				Skill.Hunting.setAngry(player, angry);
				player.sendMessage(Skill.Hunting.transform_left2.getDisplayName() + ChatColor.WHITE + " 발동 "
						+ ChatColor.RED + angry + " / " + max);
				player.playSound(player, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 2f, 2f);
				player.spawnParticle(Particle.REDSTONE, player.getEyeLocation(), 10, 0.5, 0.5, 0.5,
						new DustOptions(Color.RED, 1));
			}
			HashMapStore.setDelay(player);
			ItemStack off = player.getInventory().getItemInOffHand();
			WeaponType type = WeaponType.getWeaponType(off);
			boolean upgrade_left2_2 = HashMapStore.isAngryStatus(player)
					&& Skill.Hunting.upgrade_left2_2.confirmChance(player);
			switch (type) {
			case LongSword: {
				Location loc = player.getEyeLocation();
				Vector dir = loc.getDirection();
				new swordArrow(3 * 20, dir, loc, player).runTaskTimer(Core.getCore(), 0, 1);
				if (upgrade_left2_2)
					new swordArrow(3 * 20, dir, loc.clone().subtract(0, 0.5, 0), player).runTaskTimer(Core.getCore(), 5,
							1);
			}
				break;
			case ShortSword: {
				ItemDisplay[] itemDisplays = HashMapStore.getActive2(player);
				if (itemDisplays != null)
					for (ItemDisplay itemDisplay : itemDisplays)
						if (itemDisplay != null) {
							if (itemDisplay.getVehicle() != null) {
								Entity vehicle = itemDisplay.getVehicle();
								if (vehicle instanceof Arrow)
									vehicle.remove();
							}
							Location start = itemDisplay.getLocation();
							Location end = player.getEyeLocation();
							Vector dir = end.clone().subtract(start).toVector().normalize();
							List<LivingEntity> list = new ArrayList<LivingEntity>();
							for (int i = 0; i < end.distance(start); i++) {
								Location now = start.clone().add(dir.clone().multiply(i));
								now.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 5, 0.1, 0.1, 0.1, 0);
								now.getWorld().playSound(now, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
								for (Entity et : now.getWorld().getNearbyEntities(now, 0.75, 0.75, 0.75))
									if (et instanceof LivingEntity) {
										LivingEntity livingEntity = (LivingEntity) et;
										if (!list.contains(livingEntity) && !(et instanceof Player)) {
											list.add(livingEntity);
											damage(player, livingEntity, 5d);
										}
									}
							}
							itemDisplay.remove();
						}
			}
				break;
			case Bow: {
				ItemDisplay[] itemDisplays = HashMapStore.getActive2(player);
				if (itemDisplays != null)
					for (ItemDisplay itemDisplay : itemDisplays)
						if (itemDisplay != null) {
							if (itemDisplay.getVehicle() != null) {
								Entity vehicle = itemDisplay.getVehicle();
								if (vehicle instanceof Arrow)
									vehicle.remove();
							}
							Location loc = itemDisplay.getLocation();
							for (int i = 0; i < 10; i++) {
								Arrow arrow = loc.getWorld().spawnArrow(loc, new Vector(0, 1, 0), 0.5f, 4);
								arrow.setShooter(player);
								arrow.setPickupStatus(PickupStatus.DISALLOWED);
								arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
								arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
								Bukkit.getPluginManager()
										.callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW),
												arrow, EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(),
												false));
							}
							itemDisplay.remove();
						}
			}
				break;
			case Crossbow: {
				int max = 1;
				if (upgrade_left2_2)
					max = 2;
				for (int i = 0; i < max; i++) {
					player.removePotionEffect(PotionEffectType.SLOW);
					player.removePotionEffect(PotionEffectType.JUMP);
					Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(),
							player.getLocation().getDirection(), 4f, i != 0 ? 7f : 0f);
					arrow.setPickupStatus(PickupStatus.DISALLOWED);
					arrow.setVelocity(arrow.getVelocity().multiply(5));
					arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
					Bukkit.getPluginManager()
							.callEvent(new EntityShootBowEvent(player, off, new ItemStack(Material.ARROW), arrow,
									EquipmentSlot.OFF_HAND, (float) arrow.getVelocity().length(), false));
				}
			}
				break;
			}
			setDurationTime(player, 0);
			HashMapStore.setDuration(player, this, 0);
			player.getInventory().setItemInOffHand(player.getInventory().getItemInMainHand());
			player.getInventory().setItemInMainHand(off);
		} else if (super.use(player)) {
			if (!HashMapStore.isAngryStatus(player) && Skill.Hunting.transform_left2.confirmChance(player)) {
				int angry = Skill.Hunting.getAngry(player);
				int max = Skill.Hunting.getMaxAngry(player);
				angry += 1;
				if (angry > max)
					angry = max;
				Skill.Hunting.setAngry(player, angry);
				player.sendMessage(Skill.Hunting.transform_left2.getDisplayName() + ChatColor.WHITE + " 발동 "
						+ ChatColor.RED + angry + " / " + max);
				player.playSound(player, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 2f, 2f);
				player.spawnParticle(Particle.REDSTONE, player.getEyeLocation(), 10, 0.5, 0.5, 0.5,
						new DustOptions(Color.RED, 1));
			}
			ItemStack off = player.getInventory().getItemInOffHand();
			WeaponType type = WeaponType.getWeaponType(off);
			long duration = getDuration(player);
			if (duration > 0) {
				setDurationTime(player, duration / 1000d);
				HashMapStore.setDuration(player, this, duration);
			}
			boolean upgrade_left2_2 = HashMapStore.isAngryStatus(player)
					&& Skill.Hunting.upgrade_left2_2.confirmChance(player);
			switch (type) {
			case LongSword: {
				Location loc = player.getEyeLocation();
				Vector dir = loc.getDirection();
				new swordArrow(3 * 20, dir, loc, player).runTaskTimer(Core.getCore(), 0, 1);
				if (upgrade_left2_2)
					new swordArrow(3 * 20, dir, loc.clone().subtract(0, 0.5, 0), player).runTaskTimer(Core.getCore(), 5,
							1);
			}
				break;
			case ShortSword: {
				player.playSound(player, Sound.ITEM_TRIDENT_THROW, 0.5f, 2f);
				if (upgrade_left2_2) {
					ItemDisplay[] itemDisplays = new ItemDisplay[2];
					for (int i = 0; i < itemDisplays.length; i++) {
						ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(),
								EntityType.ITEM_DISPLAY);
						Transformation transformation = itemDisplay.getTransformation();
						transformation.getLeftRotation()
								.set(Direction.getQuaterniond(player.getLocation().getDirection()));
						itemDisplay.setTransformation(transformation);
						itemDisplay.setItemStack(off.clone());

						Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(),
								player.getLocation().getDirection(), 4f, i != 0 ? 15f : 0f);
						arrow.addPassenger(itemDisplay);
						arrow.setMetadata("remove", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("movePassenger", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("damage", new FixedMetadataValue(Core.getCore(), 0));
						itemDisplays[i] = itemDisplay;
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							if (itemDisplay != null)
								itemDisplay.remove();
							if (arrow != null)
								arrow.remove();
						}, getDuration(player) * 20 / 1000 + 2);
					}
					HashMapStore.setActive2(player, itemDisplays);
				} else {
					ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(),
							EntityType.ITEM_DISPLAY);
					Transformation transformation = itemDisplay.getTransformation();
					transformation.getLeftRotation().set(Direction.getQuaterniond(player.getLocation().getDirection()));
					itemDisplay.setTransformation(transformation);
					itemDisplay.setItemStack(off.clone());
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.addPassenger(itemDisplay);
					arrow.setMetadata("remove", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("movePassenger", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("damage", new FixedMetadataValue(Core.getCore(), 0));
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (itemDisplay != null)
							itemDisplay.remove();
						if (arrow != null)
							arrow.remove();
					}, getDuration(player) * 20 / 1000 + 2);
					HashMapStore.setActive2(player, new ItemDisplay[] { itemDisplay });
				}
			}
				break;
			case Bow: {
				if (upgrade_left2_2) {
					ItemDisplay[] itemDisplays = new ItemDisplay[2];
					for (int i = 0; i < itemDisplays.length; i++) {
						ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(),
								EntityType.ITEM_DISPLAY);
						Transformation transformation = itemDisplay.getTransformation();
						transformation.getLeftRotation()
								.set(Direction.getQuaterniond(player.getLocation().getDirection()));
						itemDisplay.setTransformation(transformation);
						itemDisplay.setItemStack(new ItemStack(Material.ARROW, 64));
						Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(),
								player.getLocation().getDirection(), 4f, i != 0 ? 7f : 0f);
						arrow.addPassenger(itemDisplay);
						arrow.setPickupStatus(PickupStatus.DISALLOWED);
						arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("no_remove", new FixedMetadataValue(Core.getCore(), true));
						arrow.setMetadata("active2", new FixedMetadataValue(Core.getCore(), true));
						itemDisplays[i] = itemDisplay;
						if (i != 0)
							arrow.getVelocity().add(getRandomVelocity(0.5d));
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							if (itemDisplay != null)
								itemDisplay.remove();
							if (arrow != null)
								arrow.remove();
						}, getDuration(player) * 20 / 1000 + 2);
					}
					HashMapStore.setActive2(player, itemDisplays);
				} else {
					ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(),
							EntityType.ITEM_DISPLAY);
					Transformation transformation = itemDisplay.getTransformation();
					transformation.getLeftRotation().set(Direction.getQuaterniond(player.getLocation().getDirection()));
					itemDisplay.setTransformation(transformation);
					itemDisplay.setItemStack(new ItemStack(Material.ARROW, 64));
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.addPassenger(itemDisplay);
					arrow.setPickupStatus(PickupStatus.DISALLOWED);
					arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("no_remove", new FixedMetadataValue(Core.getCore(), true));
					arrow.setMetadata("active2", new FixedMetadataValue(Core.getCore(), true));
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (itemDisplay != null)
							itemDisplay.remove();
						if (arrow != null)
							arrow.remove();
					}, getDuration(player) * 20 / 1000 + 2);
					HashMapStore.setActive2(player, new ItemDisplay[] { itemDisplay });
				}
			}
				break;
			case Crossbow: {
				int potionTime = (int) (getDuration(player) * 20 / 1000);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, potionTime, 255, true, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, potionTime, 999, true, false, false));
			}
				break;
			}
		}
		return true;
	}

	public Vector getRandomVelocity(double power) {
		Random r = new Random();
		return new Vector(-0.5 + r.nextDouble(), -0.5 + r.nextDouble(), -0.5 + r.nextDouble()).normalize()
				.multiply(power);
	}

	public enum Direction {
		North(0, -1, -90), NorthEast(1, -1, -135), East(1, 0, 180), EastSouth(1, 1, 135), South(0, 1, 90),
		SouthWest(-1, 1, 45), West(-1, 0, 0), WestNorth(-1, -1, -45);

		private final Vector vector;
		private Quaterniond quaterniond;

		private Direction(double posX, double posZ, double angle) {
			this.vector = new Vector(posX, 0, posZ).normalize();
			this.quaterniond = new Quaterniond(0, 0, 0, 1d).rotateAxis(angle * Math.PI / 180d, new Vector3d(0, 1, 0));
		}

		public static Quaterniond getQuaterniond(Vector dir) {
			boolean under = dir.getY() < 0;
			dir = dir.clone().setY(0);
			for (Direction direction : values()) {
				if (direction.vector.angle(dir) / Math.PI * 180 <= 22.5d)
					return under ? new Quaterniond(direction.quaterniond).rotateAxis(Math.PI, dir.toVector3d())
							: direction.quaterniond;
			}
			return new Quaterniond(0, 0, 0, 1);
		}
	}
}
