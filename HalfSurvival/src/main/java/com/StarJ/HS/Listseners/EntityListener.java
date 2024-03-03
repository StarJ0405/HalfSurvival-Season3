package com.StarJ.HS.Listseners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import com.StarJ.HS.Core;
import com.StarJ.HS.Entities.Entities;
import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.Hunting.Hunting.WeaponType;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.ItemBlockDisplay;

public class EntityListener implements Listener {
	@EventHandler
	public void Events(EntitySpawnEvent e) {
		if (e.getEntity() instanceof LivingEntity && !HashMapStore.isSpanwable())
			e.setCancelled(true);
	}

	@EventHandler
	public void Events(ChunkLoadEvent e) {
		for (Entity et : e.getChunk().getEntities())
			if (et instanceof LivingEntity && !(et instanceof Player)) {
				LivingEntity le = (LivingEntity) et;
				if (!(le instanceof Player) && !ConfigStore.hasEntityConfig(le, "type"))
					le.remove();
			} else if (et instanceof BlockDisplay || et instanceof TextDisplay || et instanceof Interaction) {
				if (!ConfigStore.hasEntityConfig(et, "type"))
					et.remove();
			}
	}

	@EventHandler
	public void Events(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		if (entity.getType().equals(EntityType.INTERACTION)) {
			String type = ConfigStore.<String>getEntityConfig(entity, "type");
			Location loc = entity.getLocation();
			switch (type) {
			case "Corpse": {
				Integer expi = ConfigStore.<Integer>getEntityConfig(entity, "exp");
				final int exp = expi != null ? expi : 0;
				if (exp > 0) {
					ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
					orb.setExperience(exp);
				}
				Integer sizei = ConfigStore.<Integer>getEntityConfig(entity, "size");
				final int size = sizei != null ? sizei : 0;
				for (int i = 0; i < size; i++) {
					ItemStack itemstack = ConfigStore.<ItemStack>getEntityConfig(entity, "items." + i);
					if (itemstack != null) {
						Item item = loc.getWorld().dropItem(loc, itemstack);
						item.setOwner(player.getUniqueId());
						item.setGlowing(true);
					}
				}
				Entity block = getEntityFromUUID(entity, "block");
				if (block != null) {
					ConfigStore.removeEntityConfig(block);
					block.remove();
				}
				Entity text = getEntityFromUUID(entity, "text");
				if (text != null) {
					ConfigStore.removeEntityConfig(text);
					text.remove();
				}
				ConfigStore.removeEntityConfig(entity);
				entity.remove();
			}
				break;
			case "FishTrap": {
				Entity fish = getEntityFromUUID(entity, "fish");
				if (fish != null && fish instanceof ItemDisplay) {
					ItemStack item = ((ItemDisplay) fish).getItemStack();
					Item drop = player.getWorld().dropItem(player.getEyeLocation(), item);
					drop.setOwner(player.getUniqueId());
					drop.setGlowing(true);
					ConfigStore.removeEntityConfig(fish);
					fish.remove();
					//
					fish = getEntityFromUUID(entity, "fish2");
					if (fish != null && fish instanceof ItemDisplay) {
						item = ((ItemDisplay) fish).getItemStack();
						drop = player.getWorld().dropItem(player.getEyeLocation(), item);
						drop.setOwner(player.getUniqueId());
						drop.setGlowing(true);
						ConfigStore.removeEntityConfig(fish);
						fish.remove();
					}
					//
					Entity text = getEntityFromUUID(entity, "text");
					if (text != null) {
						ConfigStore.removeEntityConfig(text);
						text.remove();
					}
					//
					for (ItemBlockDisplay ibd : ItemBlockDisplay.values()) {
						Entity itemblock = getEntityFromUUID(entity, ibd.name());
						if (itemblock != null) {
							ConfigStore.removeEntityConfig(itemblock);
							itemblock.remove();
						}
					}
					entity.remove();
				} else
					player.sendMessage(ChatColor.RED + "아직 물고기가 안잡혔다.");
			}
				break;
			}
		}
	}

	public Entity getEntityFromUUID(Entity entity, String key) {
		String strUUID = (ConfigStore.<String>getEntityConfig(entity, key));
		if (strUUID != null) {
			UUID uuid = UUID.fromString(strUUID);
			if (uuid != null)
				return Bukkit.getEntity(uuid);
		}
		return null;
	}

	@EventHandler
	public void Events(EntityDamageByEntityEvent e) {
		Entity etAtt = e.getDamager();
		Entity etVic = e.getEntity();
		if (etAtt instanceof Player) {
			Player att = (Player) etAtt;
			ItemStack[] contents = att.getInventory().getContents();
			for (int i = 0; i < contents.length; i++) {
				ItemStack item = contents[i];
				if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
					ItemMeta meta = item.getItemMeta();
					meta.removeEnchantments();
					item.setItemMeta(meta);
					contents[i] = item;
				}
			}
			int max = Skill.Hunting.getMaxAngry(att);
			if (max > 0) {
				int angry = Skill.Hunting.getAngry(att);
				if (!HashMapStore.isAngryStatus(att) && Skill.Hunting.passive_left1.confirmChance(att)) {
					angry += 1;
					att.sendMessage(Skill.Hunting.passive_left1.getDisplayName() + ChatColor.WHITE + " 발동 "
							+ ChatColor.RED + angry + " / " + max);
					att.playSound(att, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 2f, 2f);
					att.spawnParticle(Particle.REDSTONE, att.getEyeLocation(), 10, 0.5, 0.5, 0.5,
							new DustOptions(Color.RED, 1));
					if (angry >= max)
						angry = max;
					else
						Skill.Hunting.setAngry(att, angry);
				}
				if (angry == max) {
					if (Skill.Hunting.passive_left2.confirmChance(att)) {
						Skill.Hunting.setAngry(att, (int) (max * Skill.Hunting.passive_left2.getEffect()));
						att.sendMessage(Skill.Hunting.passive_left2.getDisplayName() + ChatColor.WHITE + " 발동");
					} else
						Skill.Hunting.setAngry(att, 0);
					HashMapStore.setAngryStatus(att);
				}
			}
			if (HashMapStore.isAngryStatus(att))
				e.setDamage(e.getDamage() * Skill.Hunting.getAngryMultiply(att));
			if (e.getCause().equals(DamageCause.ENTITY_ATTACK) && etVic instanceof LivingEntity
					&& !HashMapStore.isActive4Delay(att) && Skill.Hunting.active4.hasDuration(att)) {
				ItemStack weapon = att.getInventory().getItemInMainHand();
				WeaponType weaponType = WeaponType.getWeaponType(weapon);
				if (weaponType == null) {
					weapon = att.getInventory().getItemInOffHand();
					weaponType = WeaponType.getWeaponType(weapon);
				}
				if (weaponType != null)
					switch (weaponType) {
					case LongSword: {
						Location loc = ((LivingEntity) etVic).getEyeLocation();
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							HashMapStore.setActive4Delay(att);
							loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 20, 1.5, 1.5, 1.5);
							for (Entity et : loc.getWorld().getNearbyEntities(loc, 4, 2, 4))
								if (et instanceof LivingEntity && !(et instanceof Player)) {
									LivingEntity le = (LivingEntity) et;
									int ndt = le.getNoDamageTicks();
									le.setNoDamageTicks(10);
									le.damage(5, att);
									le.setNoDamageTicks(ndt);
								}
						}, 1);
					}
						break;
					case ShortSword: {
						Location loc = ((LivingEntity) etVic).getEyeLocation();
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							HashMapStore.setActive4Delay(att);
							boolean back = true;
							for (Entity et : loc.getWorld().getNearbyEntities(loc, 4, 2, 4))
								if (et != etVic && et instanceof LivingEntity && !(et instanceof Player)) {
									LivingEntity le = (LivingEntity) et;
									int ndt = le.getNoDamageTicks();
									le.setNoDamageTicks(10);
									le.damage(10, att);
									le.setNoDamageTicks(ndt);
									Location end = le.getEyeLocation();
									back = false;
									Vector dir = loc.clone().subtract(end).toVector().normalize().multiply(0.5d);
									for (int i = 0; i < loc.distance(end) * 2; i++) {
										Location now = end.clone().add(dir.clone().multiply(i));
										now.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 5, 0, 0, 0, 0);
									}

									break;
								}
							if (back) {
								LivingEntity le = (LivingEntity) etVic;
								int ndt = le.getNoDamageTicks();
								le.setNoDamageTicks(10);
								le.damage(5, att);
								le.setNoDamageTicks(ndt);
								le.setMetadata(att.getName(), new FixedMetadataValue(Core.getCore(), true));
								Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
									if (le != null && !le.isDead())
										le.removeMetadata(att.getName(), Core.getCore());
								}, 3);
								Location now = le.getEyeLocation();
								now.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, now, 20, 0.5, 0.5, 0.5, 0);
							}

						}, 1);
					}
						break;
					default:
						break;
					}
			}
			att.getInventory().setContents(contents);
		} else if (etAtt instanceof Projectile) {
			if (etAtt.hasMetadata("damage"))
				for (MetadataValue value : etAtt.getMetadata("damage"))
					if (value.getOwningPlugin().equals(Core.getCore())) {
						e.setDamage(value.asDouble());
						break;
					}

		}
		if (e.getEntity() instanceof ItemFrame) {
			if (etAtt instanceof Player) {
				Player att = (Player) etAtt;
				if (!att.isSneaking())
					e.setCancelled(true);
			} else
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void Events(EntityDamageEvent e) {
		Entity et = e.getEntity();
		if (et instanceof Player) {
			Player player = (Player) et;
			ItemStack[] contents = player.getInventory().getContents();
			for (int i = 0; i < contents.length; i++) {
				ItemStack item = contents[i];
				if (item != null && item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
					ItemMeta meta = item.getItemMeta();
					meta.removeEnchantments();
					item.setItemMeta(meta);
					contents[i] = item;
				}
			}
			if (Skill.Hunting.passive.confirmChance(player)) {
				long last = 0l;
				if (player.hasMetadata("passive"))
					for (MetadataValue value : player.getMetadata("passive"))
						if (value.getOwningPlugin().equals(Core.getCore()))
							last = value.asLong();
				if (System.currentTimeMillis() - last >= Skill.Hunting.passive.getEffect(0) * 1000l) {
					player.setMetadata("passive", new FixedMetadataValue(Core.getCore(), System.currentTimeMillis()));
					Skill.Hunting.setAbsorption(player,
							player.getAbsorptionAmount() + Skill.Hunting.passive.getEffect(1));
					player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getEyeLocation(), 1, 0, 0, 0);
					player.getWorld().playSound(player.getEyeLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1f);
					player.sendMessage(Skill.Hunting.passive.getDisplayName() + ChatColor.WHITE + " 발동");
				}
			}
			player.getInventory().setContents(contents);
		} else if (et instanceof Item
				&& (e.getCause().equals(DamageCause.LAVA) || e.getCause().equals(DamageCause.FIRE)))
			e.setCancelled(true);
		else if (et instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) et;
			Entities entities = Entities.valueOf(livingEntity);
			if (entities != null)
				entities.setNameWithHealth(livingEntity, e.getDamage());
		}
	}

	@EventHandler
	public void Evetns(ProjectileHitEvent e) {
		Projectile etPro = e.getEntity();
		Entity etHit = e.getHitEntity();
		if (etHit != null) {
			if (etHit instanceof ItemFrame)
				e.setCancelled(true);
			if (etHit instanceof LivingEntity) {
				LivingEntity leHit = (LivingEntity) etHit;
				if (etPro.hasMetadata("no_player") && etHit instanceof Player)
					e.setCancelled(true);
				if (etPro.hasMetadata("no_remove"))
					e.setCancelled(true);
				if (etPro.hasMetadata("no_delay")) {
					int ndt = leHit.getNoDamageTicks();
					leHit.setNoDamageTicks(10);
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (leHit != null && !leHit.isDead())
							leHit.setNoActionTicks(ndt > 0 ? ndt - 1 : 0);
					}, 1);
				}
			}
		}
		if (etPro != null) {
			if (!e.isCancelled() && etPro.hasMetadata("remove"))
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					etPro.remove();
				}, 2);
			if (!e.isCancelled() && etPro.hasMetadata("movePassenger"))
				if (etHit != null)
					for (Entity passengers : etPro.getPassengers())
						etHit.addPassenger(passengers);
		}
	}
}
