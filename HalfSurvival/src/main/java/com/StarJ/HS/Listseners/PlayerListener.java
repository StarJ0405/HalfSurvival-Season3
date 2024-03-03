package com.StarJ.HS.Listseners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftFishHook;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.StarJ.HS.Core;
import com.StarJ.HS.Entities.Pets.CatPet;
import com.StarJ.HS.Entities.Pets.DogPet;
import com.StarJ.HS.Entities.Pets.Pets;
import com.StarJ.HS.Items.BowItems;
import com.StarJ.HS.Items.DurableItems;
import com.StarJ.HS.Items.Items;
import com.StarJ.HS.Items.SwordItems;
import com.StarJ.HS.Items.UsableItems;
import com.StarJ.HS.Items.UsableItem.Pocket;
import com.StarJ.HS.Items.UsableItem.Pocket.pocketType;
import com.StarJ.HS.Items.UsableItem.Pocket.size;
import com.StarJ.HS.Skills.Minigame;
import com.StarJ.HS.Skills.Skill;
import com.StarJ.HS.Skills.UsableSkill;
import com.StarJ.HS.Skills.Hunting.Hunting.WeaponType;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.ConfigStore.PetInfo;
import com.StarJ.HS.Systems.GUIs;
import com.StarJ.HS.Systems.HashMapStore;
import com.StarJ.HS.Systems.HashMapStore.LocationTask;
import com.StarJ.HS.Systems.SkillType;

public class PlayerListener implements Listener {
	@EventHandler
	public void Events(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		for (Skill skill : Skill.values())
			if (skill instanceof UsableSkill) {
				UsableSkill usable = (UsableSkill) skill;
				HashMapStore.setCooldown(player, skill, usable.getRemainUseTime(player));
				HashMapStore.setDuration(player, skill, usable.getRemainDurationTime(player));
			}
		if (Skill.Mining.active1.hasDuration(player)) {
			int level = player.hasPotionEffect(PotionEffectType.FAST_DIGGING)
					? player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier()
					: 0;
			int duration = (int) (Skill.Mining.active1.getRemainDurationTime(player) * 20 / 1000);
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
			if (duration > 0)
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.FAST_DIGGING, duration, level, true, false, false));
		}
		if (ConfigStore.hasPlayerConfig(player, "pets.status")) {
			PetInfo info = ConfigStore.getPlayerPetInfo(player);
			if (info != null) {
				Pets pets = info.getPets();
				LivingEntity livingEntity = pets.spawnEntity(player.getLocation());
				pets.setOwner(player, livingEntity);
				if (pets instanceof DogPet)
					((DogPet) pets).setColor(livingEntity, info.getColor());
				else if (pets instanceof CatPet)
					((CatPet) pets).setColor(livingEntity, info.getColor(), info.getType());
				ConfigStore.setPlayerPet(player, livingEntity);
			}
		}
		Skill.Hunting.setMaxAbsorption(player);
		player.removeMetadata("mtn", Core.getCore());
	}

	@EventHandler
	public void Events(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		HashMapStore.resetCooldown(player);
		HashMapStore.resetDuration(player);
		LivingEntity pet = ConfigStore.getPlayerPet(player);
		if (pet != null)
			pet.remove();

	}

	@EventHandler
	public void Evenst(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		Material material = block.getType();
		switch (material) {
		case ENCHANTING_TABLE:
			e.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE));
			break;
		case ANVIL:
		case CHIPPED_ANVIL:
		case DAMAGED_ANVIL:
			e.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE));
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void Events(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		Material material = block.getType();
		ItemStack main = player.getInventory().getItemInMainHand();
		boolean preventBreak = HashMapStore.isPreventBreak(player);
		Random r = new Random();
		switch (material) {
		case NETHER_PORTAL:
			e.setCancelled(true);
			Location from = block.getLocation();
			a: for (int x = -1; x <= 1; x++)
				for (int y = -1; y <= 1; y++)
					for (int z = -1; z <= 1; z++) {
						Location now = from.clone().add(x, y, z);
						if (now.getBlock().getType().equals(Material.NETHER_PORTAL)) {
							from = now;
							break a;
						}
					}
			while (from.clone().add(0, -1, 0).getBlock().getType().equals(material))
				from = from.add(0, -1, 0);
			while (from.clone().add(0, 0, 1).getBlock().getType().equals(material))
				from = from.add(0, 0, 1);
			while (from.clone().add(1, 0, 0).getBlock().getType().equals(material))
				from = from.add(1, 0, 0);
			ConfigStore.removePortal(from);
			final Block portal = from.getBlock();
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				Block now = portal;
				now.breakNaturally();
				do {
					Block nowZ = now.getLocation().getBlock();
					do {
						Block nowX = nowZ.getLocation().getBlock();
						do {
							nowX = nowX.getLocation().add(-1, 0, 0).getBlock();
							nowX.breakNaturally();
						} while (nowX.getLocation().add(-1, 0, 0).getBlock().getType().equals(material));
						nowZ = nowZ.getLocation().add(0, 0, -1).getBlock();
						nowZ.breakNaturally();
					} while (nowZ.getLocation().add(0, 0, -1).getBlock().getType().equals(material));
					now = now.getLocation().add(0, 1, 0).getBlock();
					now.breakNaturally();
				} while (now.getLocation().add(0, 1, 0).getBlock().getType().equals(material));
			}, 1);
			return;
		case ENCHANTING_TABLE:
		case ANVIL:
		case CHIPPED_ANVIL:
		case DAMAGED_ANVIL:
			e.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE));
			return;
		case DEEPSLATE_DIAMOND_ORE:
		case DIAMOND_ORE:

		case DEEPSLATE_EMERALD_ORE:
		case EMERALD_ORE:
			if (r.nextDouble() < 0.01d) {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					block.setType(Material.ANCIENT_DEBRIS, true);
				}, 1);
				player.spawnParticle(Particle.CRIT_MAGIC, block.getLocation().clone().add(0.5, 1, 0.5), 100, 0.5f, 0.5f,
						0.5f, 0.1);
				player.playSound(block.getLocation(), Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 0.5f, 2f);
			}
		case COAL_ORE:
		case DEEPSLATE_COAL_ORE:

		case COPPER_ORE:
		case DEEPSLATE_COPPER_ORE:

		case DEEPSLATE_GOLD_ORE:
		case GOLD_ORE:

		case DEEPSLATE_IRON_ORE:
		case IRON_ORE:

		case DEEPSLATE_LAPIS_ORE:
		case LAPIS_ORE:

		case DEEPSLATE_REDSTONE_ORE:
		case REDSTONE_ORE: {
			if (r.nextDouble() < 0.05d) {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					double chance = r.nextDouble();
					if (chance < 0.49d)
						block.setType(Material.NETHER_GOLD_ORE, true);
					else if (chance < 0.49d + 0.49d)
						block.setType(Material.NETHER_QUARTZ_ORE, true);
					else
						block.setType(Material.ANCIENT_DEBRIS, true);
				}, 1);
				player.spawnParticle(Particle.CRIT_MAGIC, block.getLocation().clone().add(0.5, 1, 0.5), 100, 0.5f, 0.5f,
						0.5f, 0.1);
				player.playSound(block.getLocation(), Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON, 0.5f, 2f);
				Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/convenient"));
				if (adv != null)
					player.getAdvancementProgress(adv).awardCriteria("trigger");
			}
		}
		case NETHER_GOLD_ORE:
		case NETHER_QUARTZ_ORE:

		case ANCIENT_DEBRIS:
			if (!player.getGameMode().equals(GameMode.CREATIVE))
				SkillType.mining.addExp(player, 1);
			if (!preventBreak && !HashMapStore.getMinigame(player).isRunning()
					&& Skill.Mining.minigame.confirmChance(player))
				HashMapStore.getMinigame(player).miningMinigame.start(material);
			if (Skill.Mining.passive_left1.confirmChance(player))
				for (ItemStack drop : block.getDrops(main))
					block.getWorld().dropItem(block.getLocation(), drop);
			if (Skill.Mining.passive_left2.confirmChance(player))
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					block.setType(material, true);
				}, 1);
			if (!preventBreak && Skill.Mining.upgrade_left3.confirmChance(player)) {
				double percent = Skill.Mining.upgrade_left3.getEffect();
				long remain = Skill.Mining.active3.getRemainUseTime(player);
				if (remain > 0) {
					remain -= (long) (Skill.Mining.active3.getCooldown(player) * percent);
					Skill.Mining.active3.setLastUseTime(player, remain / 1000d);
					HashMapStore.setCooldown(player, Skill.Mining.active3, remain);
					if (remain > 0)
						player.sendMessage(Skill.Mining.active3.getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
								+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
								+ (String.format("%.1f", remain / 1000d)) + ")");
					else
						HashMapStore.sendCooldownMessage(player, Skill.Mining.active3);
				}
			}
			if (Skill.Mining.passive_right1.confirmChance(player)) {
				Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(material));
				item.setOwner(player.getUniqueId());
				item.setPersistent(true);
			}
			if (Skill.Mining.passive_right3.confirmChance(player)) {
				int range = (int) Skill.Mining.passive_right3.getEffect(0);
				int size = (int) Skill.Mining.passive_right3.getEffect(1);
				a: for (int x = -range; x <= range; x++)
					for (int y = -range; y <= range; y++)
						for (int z = -range; z <= range; z++)
							if (x != 0 && y != 0 && z != 0 && size > 0) {
								Location now = block.getLocation().clone().add(x, y, z);
								Material nowMaterial = now.getBlock().getType();
								if (nowMaterial.equals(Material.AIR)
										|| Skill.Mining.active2.canBreak(nowMaterial) == 1) {
									size--;
									now.getBlock().setType(material, true);
									player.spawnParticle(Particle.CRIT, now, 100, 0.5, 0.5, 0.5);
									player.playSound(now, Sound.BLOCK_END_PORTAL_SPAWN, 0.5f, 2f);
									break a;
								}
							}
			}
			break;
		default:
			break;
		}

		if (SkillType.mining.isItem(main) && !preventBreak) {
			if (Skill.Mining.passive.confirmChance(player)) {
				short add = (short) Skill.Mining.passive.getEffect();
				short durability = main.getDurability();
				durability = (short) (durability > add ? durability - add : 0);
				main.setDurability(durability);
				player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 2f);
			}
			if (Skill.Mining.active2.canBreak(material) > 0 && !player.isSneaking()
					&& Skill.Mining.transform_left2.confirmChance(player)) {
				HashMapStore.setPreventBreak(player, true);
				Skill.Mining.active2.act(block, player);
				HashMapStore.setPreventBreak(player, false);
			}
			if (Skill.Mining.active1.hasDuration(player) && player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
				int level = player.hasPotionEffect(PotionEffectType.FAST_DIGGING)
						? player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier()
						: 0;
				if (level < Skill.Mining.transform_left1.getEffect()
						&& Skill.Mining.transform_left1.confirmChance(player)) {
					player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1f, 2f);
					int duration = player.getPotionEffect(PotionEffectType.FAST_DIGGING).getDuration();
					player.removePotionEffect(PotionEffectType.FAST_DIGGING);
					player.addPotionEffect(
							new PotionEffect(PotionEffectType.FAST_DIGGING, duration, level + 1, true, false, false));
					player.sendMessage(
							Skill.Mining.transform_left1.getDisplayName() + ChatColor.WHITE + "의 효과로 파괴속도가 증가합니다.");
				}
				int transform_middle1 = HashMapStore.getMiningTransformMiddle1(player);
				if (transform_middle1 > 0 && Skill.Mining.transform_middle1.confirmChance(player)) {
					player.playSound(player, Sound.ENTITY_GENERIC_DRINK, 1f, 1f);
					double duration = (Skill.Mining.active1.getRemainDurationTime(player) / 1000d)
							+ Skill.Mining.transform_middle1.getEffect(0);
					HashMapStore.setDuration(player, Skill.Mining.active1, (long) (duration * 1000l));
					Skill.Mining.active1.setDurationTime(player, duration);
					player.removePotionEffect(PotionEffectType.FAST_DIGGING);
					if (duration > 0)
						player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (duration * 20),
								level, true, false, false));
					transform_middle1 -= 1;
					HashMapStore.setMiningTransformMiddle1(player, transform_middle1);
					player.sendMessage(Skill.Mining.transform_middle1.getDisplayName() + ChatColor.WHITE + "의 횟수가 "
							+ transform_middle1 + "회 남았습니다." + ChatColor.GRAY + "(" + String.format("%.1f", duration)
							+ "s)");
				}
				int transform_right1 = HashMapStore.getMiningTransformRight1(player);
				if (transform_right1 > 0 && Skill.Mining.transform_right1.confirmChance(player)) {
					long cooldown = Skill.Mining.active1.getRemainUseTime(player);
					cooldown -= 30 * 1000l;
					Skill.Mining.active1.setLastUseTime(player, cooldown / 1000d);
					HashMapStore.setCooldown(player, Skill.Mining.active1, cooldown);
					player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1f, 1f);
					transform_right1 -= 1;
					HashMapStore.setMiningTransformRight1(player, transform_right1);
					player.sendMessage(Skill.Mining.transform_right1.getDisplayName() + ChatColor.WHITE + "의 횟수가 "
							+ transform_right1 + "회 남았습니다." + ChatColor.GRAY + "("
							+ String.format("%.1f", cooldown / 1000d) + "s)");
				}
			}
			if (Skill.Mining.passive_right2.confirmChance(player) && Skill.Mining.active2.canBreak(material) > 1)
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					player.playSound(player, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f);
					player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, block.getLocation().clone().add(0.5, 0.5, 0.5),
							25, 0.5, 0.5, 0.5, 0.1);
					block.setType(Skill.Mining.active4.getRandomBlock(), true);
				}, 1);
		}
		if (!preventBreak) {
			if (Skill.Mining.passive_middle1.confirmChance(player)) {
				int duration = (int) (Skill.Mining.passive_middle1.getEffect() * 20);
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0, true, false, false));
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, 0, true, false, false));
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.CONDUIT_POWER, duration, 0, true, false, false));
				player.playSound(player, Sound.BLOCK_LAVA_EXTINGUISH, 2f, 1f);
			}
			if (Skill.Mining.passive_middle2.confirmChance(player)) {
				int duration = (int) (Skill.Mining.passive_middle2.getEffect() * 20);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 4, true, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 1, true, false, false));
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, 2, true, false, false));
				player.playSound(player, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 0.5f, 1f);
			}
		}
		if (ConfigStore.hasBlockConfig(block, "type")) {
			String type = ConfigStore.getBlockConfig(block, "type");
			if (type.equals(Skill.Mining.active4.getKey()) && !preventBreak) {
				Skill.Mining.active4.Check(player, block, main);
				if (Skill.Mining.transform_left4.confirmChance(player)) {
					HashMapStore.setPreventBreak(player, true);
					Skill.Mining.active2.act(block, player);
					HashMapStore.setPreventBreak(player, false);
				}
				if (Skill.Mining.upgrade_right4_2.confirmChance(player))
					Skill.Mining.active4.act(player, block);
			}
			ConfigStore.removeBlockConfig(block);
		}
		List<Entity> list = new ArrayList<Entity>(
				block.getWorld().getNearbyEntities(block.getLocation(), 0.1, 0.1, 0.1, new Predicate<Entity>() {
					@Override
					public boolean test(Entity t) {
						return t instanceof BlockDisplay;
					}
				}));
		for (Entity et : list)
			et.remove();
		if (Skill.Mining.transform_middle2.hasLearn(player) && preventBreak && list.size() > 0) {
			long remain = Skill.Mining.active2.getRemainUseTime(player);
			if (remain > 0) {
				double percent = Skill.Mining.transform_middle2.getEffect();
				remain -= Skill.Mining.active2.getCooldown(player) * percent;
				Skill.Mining.active2.setLastUseTime(player, remain / 1000d);
				HashMapStore.setCooldown(player, Skill.Mining.active2, remain);
				if (remain > 0)
					player.sendMessage(Skill.Mining.active2.getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
							+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
							+ (String.format("%.1f", remain / 1000d)) + ")");
				else
					HashMapStore.sendCooldownMessage(player, Skill.Mining.active2);
			}
			if (HashMapStore.isTransformMiddle2(player)) {
				double multiply = Skill.Mining.upgrade_middle2.hasLearn(player)
						? Skill.Mining.upgrade_middle2.getEffect()
						: 1;
				if (Skill.Mining.transform_middle2.checkChance(player, 0, new double[0], new double[] { multiply })) {
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						Skill.Mining.active3.find(player, block.getLocation());
					}, 10);
					HashMapStore.setTransformMiddle2(player, false);
				}
			}
		}
	}

	@EventHandler
	public void Events(PlayerAnimationEvent e) {
		Player player = e.getPlayer();
		if (HashMapStore.isBreaking(player) && e.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
			Block lastBlock = HashMapStore.getLastBlock(player);
			if (lastBlock != null)
				if (Skill.Mining.passive_left3.confirmChance(player)) {
					player.breakBlock(lastBlock);
					HashMapStore.removeBreaking(player);
				} else if (Skill.Mining.active2.canBreak(lastBlock.getType()) >= 2
						&& Skill.Mining.passive_middle3.confirmChance(player)) {
					Skill.Mining.active3.find(player, lastBlock.getLocation(), lastBlock.getType(),
							(int) Skill.Mining.passive_middle3.getEffect());
					HashMapStore.removeBreaking(player);
				} else
					HashMapStore.setBreaking(player, lastBlock);
		}

	}

	@EventHandler
	public void Events(BlockDamageEvent e) {
		Player player = e.getPlayer();
		ItemStack main = player.getInventory().getItemInMainHand();
		Block block = e.getBlock();
		if (SkillType.mining.isItem(main) && Skill.Mining.active2.canBreak(block.getType()) > 0)
			if (Skill.Mining.passive_left3.hasLearn(player) || Skill.Mining.passive_middle3.hasLearn(player))
				HashMapStore.setBreaking(player, block);
	}

	@EventHandler
	public void Events(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
		Minigame minigame = HashMapStore.getMinigame(player);
		if (e.isSneaking())
			if (minigame.miningMinigame.isRunning() && Skill.Mining.minigame.hasLearn(player))
				HashMapStore.getMinigame(player).miningMinigame.Sneak();
			else if (minigame.fishingMinigame.isRunning() && Skill.Fishing.minigame.hasLearn(player))
				HashMapStore.getMinigame(player).fishingMinigame.Sneak();
// 		data get entity @p[name=Star_J] SelectedItem
//		if (player.getName().equalsIgnoreCase("Star_J")) {
//			ItemStack main = player.getInventory().getItemInMainHand();
//			if (main != null && main.getType().equals(Material.FISHING_ROD)) {
//				FishHook hook = player.launchProjectile(FishHook.class, player.getLocation().getDirection());
//				Bukkit.getPluginManager().callEvent(new PlayerFishEvent(player, null, hook, State.FISHING));
//			}
//		}

	}

	@EventHandler
	public void Events(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		if (e.getHand().equals(EquipmentSlot.HAND)) {
			Items i = Items.valueOf(player.getInventory().getItemInMainHand());
			if (i instanceof UsableItems)
				e.setCancelled(!((UsableItems) i).Use(player, entity));
			else if (i != null)
				e.setCancelled(!i.isInteract());
		} else if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
			ItemStack item = player.getInventory().getItemInOffHand();
			Items i = Items.valueOf(item);
			if (i instanceof UsableItems)
				e.setCancelled(!((UsableItems) i).Use(player, entity));
			else if (i != null)
				e.setCancelled(!i.isInteract());
		}
	}

	@EventHandler
	public void Events(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Action action = e.getAction();
		Block block = e.getClickedBlock();
		if (action.equals(Action.PHYSICAL)) {
			if (block != null && block.getType().equals(Material.FARMLAND))
				e.setCancelled(true);
		} else {
			if (block != null && block.getType().name().contains("TRAPDOOR")
					&& block.getLocation().clone().subtract(0, 1, 0).getBlock().getType().name().contains("LANTERN"))
				e.setCancelled(true);
			else if (block != null && block.getType().equals(Material.NETHER_PORTAL)) {
				if (player.getGameMode().equals(GameMode.CREATIVE) && e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					return;
				e.setCancelled(true);
				Location loc = block.getLocation();
				a: for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						for (int z = -1; z <= 1; z++) {
							Location now = loc.clone().add(x, y, z);
							if (now.getBlock().getType().equals(Material.NETHER_PORTAL)) {
								loc = now;
								break a;
							}
						}
				while (loc.clone().add(0, -1, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
					loc = loc.add(0, -1, 0);
				while (loc.clone().add(0, 0, 1).getBlock().getType().equals(Material.NETHER_PORTAL))
					loc = loc.add(0, 0, 1);
				while (loc.clone().add(1, 0, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
					loc = loc.add(1, 0, 0);
				ConfigStore.addPortal(loc);
				GUIs.portal.openInv(player);
			} else if (block != null && block.getType().equals(Material.ENCHANTING_TABLE)) {
				if (player.getGameMode().equals(GameMode.CREATIVE) && e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					return;
				e.setCancelled(true);
				GUIs.skillType.openInv(player);
			} else if (block != null
					&& (block.getType().equals(Material.ANVIL) || block.getType().equals(Material.CHIPPED_ANVIL)
							|| block.getType().equals(Material.DAMAGED_ANVIL))) {
				if (player.getGameMode().equals(GameMode.CREATIVE) && e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					return;
				e.setCancelled(true);
				if (player.isSneaking() && player.getInventory().getItemInMainHand() != null
						&& !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
					ItemStack main = player.getInventory().getItemInMainHand();
					String name = main.getType().name();
					if (main.hasItemMeta() && main.getItemMeta().hasDisplayName())
						name = main.getItemMeta().getDisplayName();
					long price = GUIs.getPrice(main);
					int amount = main.getAmount();
					player.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + "의 가격은 " + ChatColor.YELLOW + price
							+ ChatColor.WHITE + "원 "
							+ (amount > 1 ? "(개당 : " + ChatColor.YELLOW + "" + price / amount + ChatColor.WHITE + ")"
									: ""));
				} else
					GUIs.sell.openInv(player);
			} else {
				if (e.getHand().equals(EquipmentSlot.HAND)) {
					ItemStack main = player.getInventory().getItemInMainHand();
					Items i = Items.valueOf(main);
					if (i instanceof UsableItems)
						e.setCancelled(!((UsableItems) i).Use(player, block));
					else if (i != null)
						e.setCancelled(!i.isInteract());
					if (main != null) {
						if (main.getType().equals(Material.FISHING_ROD)
								&& (e.getAction().equals(Action.LEFT_CLICK_BLOCK)
										|| e.getAction().equals(Action.LEFT_CLICK_AIR))
								&& HashMapStore.getFishHook(player) != null) {
							Entity hook = HashMapStore.getFishHook(player);
							for (int j = 0; j < Skill.Fishing.passive_middle3.getEffect(1); j++)
								if (hook.hasMetadata("passive_middle3_" + j)
										&& Skill.Fishing.passive_middle3.confirmChance(player)) {
									Location base = hook.getLocation();
									ItemStack drop = Skill.Fishing.getFish(main, base, player);
									drop.setAmount((int) Skill.Fishing.passive_middle3.getEffect());
									Entity extra = player.getWorld().dropItem(base, drop);
									Vector dir = player.getEyeLocation().subtract(hook.getLocation()).toVector()
											.normalize();
									dir.multiply(0.5d);
									extra.setVelocity(dir);
									hook.removeMetadata("passive_middle3_" + j, Core.getCore());
									player.playSound(player, Sound.ENTITY_FISHING_BOBBER_SPLASH, 0.5f, 2f);
									break;
								}
						}
					}
				} else if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
					ItemStack item = player.getInventory().getItemInOffHand();
					Items i = Items.valueOf(item);
					if (i instanceof UsableItems)
						e.setCancelled(!((UsableItems) i).Use(player, block));
					else if (i != null)
						e.setCancelled(!i.isInteract());
				}

				for (SkillType st : SkillType.values())
					if (st.isItem(player.getInventory().getItemInOffHand())) {
						if (!st.isItem(player.getInventory().getItemInMainHand()))
							if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
								if (e.getHand().equals(EquipmentSlot.OFF_HAND))
									if (player.isSneaking())
										st.getShiftRight().use(player);
									else
										st.getRight().use(player);
							} else {
								if (player.isSneaking())
									st.getShiftLeft().use(player);
								else
									st.getLeft().use(player);
							}
						e.setCancelled(true);
					}
			}
		}
		ItemStack[] items = player.getInventory().getContents();
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (item != null)
				if (item.getType().equals(Material.ENCHANTED_BOOK))
					items[i] = null;
				else
					for (Enchantment ench : item.getEnchantments().keySet())
						item.removeEnchantment(ench);
		}
		player.getInventory().setContents(items);
	}

	@EventHandler
	public void Events(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
			player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		}, 1);
	}

	@EventHandler
	public void Events(PlayerDeathEvent e) {
		Player player = e.getEntity();
		player.setAbsorptionAmount(0);
		switch (player.getGameMode()) {
		case CREATIVE:
		case SPECTATOR:
			e.setKeepLevel(true);
			e.setKeepInventory(true);
			e.setDroppedExp(0);
			e.getDrops().clear();
			break;
		default:
			List<ItemStack> items = e.getDrops();
			int exp = e.getDroppedExp();
			if (items.size() > 0 || exp > 0) {
				HashMapStore.setSpanwable(true);
				Location loc = player.getLocation();
				//
				BlockDisplay blockDisplay = (BlockDisplay) loc.getWorld().spawnEntity(loc.clone().add(-0.5, 0, -0.5),
						EntityType.BLOCK_DISPLAY);
				blockDisplay.setBillboard(Billboard.FIXED);
				blockDisplay.setBlock(Material.CHEST.createBlockData());
				ConfigStore.setEntityConfig(blockDisplay, "type", "Corpse_block");
				//
				TextDisplay textDisplay = (TextDisplay) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0),
						EntityType.TEXT_DISPLAY);
				textDisplay.setBillboard(Billboard.VERTICAL);
				textDisplay.setAlignment(TextAlignment.CENTER);
				textDisplay.setDefaultBackground(false);
				textDisplay.setText(ChatColor.RED + player.getName() + ChatColor.WHITE + "님의 시체");
				ConfigStore.setEntityConfig(textDisplay, "type", "Corpse_text");
				//
				Interaction interaction = (Interaction) loc.getWorld().spawnEntity(loc, EntityType.INTERACTION);
				interaction.setMetadata("type", new FixedMetadataValue(Core.getCore(), "Corpse"));
				interaction.setInteractionHeight(1);
				interaction.setInteractionWidth(1);
				interaction.setResponsive(true);
				ConfigStore.<String>setEntityConfig(interaction, "type", "Corpse");
				ConfigStore.<Integer>setEntityConfig(interaction, "exp", exp);
				ConfigStore.<Integer>setEntityConfig(interaction, "size", items.size());
				for (int i = 0; i < items.size(); i++)
					ConfigStore.<ItemStack>setEntityConfig(interaction, "items." + i, items.get(i));
				ConfigStore.<String>setEntityConfig(interaction, "block", blockDisplay.getUniqueId().toString());
				ConfigStore.<String>setEntityConfig(interaction, "text", textDisplay.getUniqueId().toString());
				//
				HashMapStore.setSpanwable(false);
			}
			e.setDroppedExp(0);
			e.getDrops().clear();
			break;
		}
	}

	@EventHandler
	public void Events(EntityPickupItemEvent e) {
		LivingEntity le = e.getEntity();
		Item Item = e.getItem();
		ItemStack item = Item.getItemStack();
		if (item.getType().equals(Material.ENCHANTED_BOOK)) {
			Item.remove();
			e.setCancelled(true);
		} else if (le instanceof Player) {
			Player player = (Player) le;
			a: for (pocketType pocket : pocketType.values())
				if (player.getInventory().contains(pocket.getType()) && pocket.isAllowed(item)) {
					ItemStack[] contents = player.getInventory().getContents();
					for (int i = 0; i < contents.length; i++)
						if (contents[i] != null && contents[i].getType().equals(pocket.getType())) {
							ItemStack shulker = contents[i];
							if (shulker.getItemMeta() instanceof BlockStateMeta) {
								BlockStateMeta meta = (BlockStateMeta) shulker.getItemMeta();
								if (meta.getBlockState() instanceof ShulkerBox) {
									ShulkerBox box = (ShulkerBox) meta.getBlockState();
									int empty = box.getInventory().firstEmpty();
									size size = Pocket.size.getSize(shulker);
									if (size == null)
										size = Pocket.size.SMALL;
									if (empty != -1 && empty < size.getSize()) {
										box.getInventory().addItem(item);
										meta.setBlockState(box);
										final int fi = i;
										Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
											Items items = Items.valueOf(shulker);
											if (items != null) {
												int model = 0;
												for (ItemStack test : box.getInventory().getContents())
													if (test != null)
														model++;
												meta.setCustomModelData(items.getModel() + model);
											}
											shulker.setItemMeta(meta);
											contents[fi] = shulker;
											player.getInventory().setContents(contents);
										}, 1);
										e.setCancelled(true);
										Item.remove();
										break a;
									} else if (box.getInventory().containsAtLeast(item, 1)) {
										ItemStack[] boxContents = box.getInventory().getContents();
										for (int j = 0; j < boxContents.length; j++)
											if (boxContents[j] != null && boxContents[j].isSimilar(item)) {
												int max = item.getType().getMaxStackSize();
												ItemStack boxItem = boxContents[j];
												int amount = boxItem.getAmount();
												if (amount < max) {
													amount += item.getAmount();
													boxItem.setAmount(amount > max ? max : amount);
													amount %= max;
													boxContents[j] = boxItem;
													box.getInventory().setContents(boxContents);
													meta.setBlockState(box);
													final int fi = i;
													Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
														Items items = Items.valueOf(shulker);
														if (items != null) {
															int model = 0;
															for (ItemStack test : box.getInventory().getContents())
																if (test != null)
																	model++;
															meta.setCustomModelData(items.getModel() + model);
														}
														shulker.setItemMeta(meta);
														contents[fi] = shulker;
														player.getInventory().setContents(contents);
													}, 1);
													if (boxItem.getAmount() == max && amount > 0) {
														item.setAmount(amount);
														Item.setItemStack(item);
														e.setCancelled(true);
														return;
													} else {
														Item.remove();
														e.setCancelled(true);
														return;
													}
												}
											}

									}
								}
							}
						}
				}

		}
	}

	@EventHandler
	public void Events(PlayerItemDamageEvent e) {
		ItemStack item = e.getItem();
		Items i = Items.valueOf(item);
		if (i != null && i instanceof DurableItems) {
			((DurableItems) i).modifyItemStack(item, -e.getDamage());
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void Events(PlayerFishEvent e) {
		Player player = e.getPlayer();
		State state = e.getState();
		ItemStack main = player.getInventory().getItemInMainHand();
		boolean hand = main != null && main.getType().equals(Material.FISHING_ROD) ? true : false;
		switch (state) {
		case BITE:
			if (Skill.Fishing.passive_right2.confirmChance(player)) {
				ItemStack rod = hand ? player.getInventory().getItemInMainHand()
						: player.getInventory().getItemInOffHand();
				FishHook hook = e.getHook();
				Location base = hook.getLocation();
				Item drop = (Item) base.getWorld().spawnEntity(base, EntityType.DROPPED_ITEM);
				drop.setItemStack(Skill.Fishing.getFish(rod, base, player,
						(float) player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue()));
				Vector dir = player.getEyeLocation().subtract(base).toVector().normalize();
				dir.multiply(0.5d);
				drop.setVelocity(dir);
				FishHook original = HashMapStore.getFishHook(player);
				PlayerFishEvent fishEvent = new PlayerFishEvent(player, drop, hook, e.getHand(), State.CAUGHT_FISH);
				if (!hook.hasMetadata("extra"))
					Bukkit.getPluginManager().callEvent(new PlayerItemDamageEvent(player, rod, 1));
				HashMapStore.removeFishDelay(player);
				Bukkit.getPluginManager().callEvent(fishEvent);
				if (!fishEvent.isCancelled()) {
					hook.remove();
					HashMapStore.setFishDelay(player);
				}
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					if (original != null && !original.isDead()) {
						((CraftPlayer) player).getHandle().ck = ((CraftFishHook) original).getHandle();
						HashMapStore.setFishHook(player, original);
					}
				}, 1);
			}
			break;
		case CAUGHT_FISH: {
			if (HashMapStore.isFishDelay(player)) {
				e.setCancelled(true);
				return;
			}
			ItemStack rod = hand ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
			SkillType.fishing.addExp(player, 1);
			Entity caught = e.getCaught();
			FishHook hook = e.getHook();
			Location base = hook.getLocation();
			if (caught instanceof Item) {
				Item drop = (Item) caught;
				ItemStack dropItem = drop.getItemStack();
				if (!HashMapStore.getMinigame(player).isRunning() && Skill.Fishing.minigame.confirmChance(player))
					HashMapStore.getMinigame(player).fishingMinigame.start(dropItem.clone());
				if (Skill.Fishing.passive_left1.confirmChance(player)) {
					dropItem.setAmount((int) (dropItem.getAmount() + Skill.Fishing.passive_left1.getEffect()));
					drop.setItemStack(dropItem);
				}
			}
			Boolean active3b = ConfigStore.<Boolean>getPlayerConfig(player, "fishing.skills.active3.status");
			boolean active3 = active3b != null ? active3b : false;
			if (!active3 && Skill.Fishing.upgrade_middle3_1.confirmChance(player)) {
				Integer stacki = ConfigStore.getPlayerConfig(player, "fishing.skills.transform_middle3.stack");
				int luck = stacki != null ? stacki : 0;
				player.getAttribute(Attribute.GENERIC_LUCK)
						.setBaseValue(player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue() + luck);
				player.sendMessage(Skill.Fishing.upgrade_middle3_1.getDisplayName() + ChatColor.WHITE + " 발동");
			}
			Boolean transform_middle4b = ConfigStore.<Boolean>getPlayerConfig(player,
					"fishing.skills.transform_middle4.status");
			boolean transform_middle4 = transform_middle4b != null ? transform_middle4b : false;
			if (transform_middle4 && caught instanceof Item && Skill.Fishing.upgrade_left3.confirmChance(player)) {
				Item drop = (Item) caught;
				ItemStack dropItem = drop.getItemStack();
				dropItem.setAmount((int) (dropItem.getAmount() * Skill.Fishing.upgrade_left3.getEffect()));
				drop.setItemStack(dropItem);
				player.sendMessage(Skill.Fishing.upgrade_left3.getDisplayName() + ChatColor.WHITE + " 발동");
			}
			ConfigStore.<Boolean>setPlayerConfig(player, "fishing.skills.transform_middle4.status", null);
			if (active3 && Skill.Fishing.transform_left3.confirmChance(player)) {
				ItemStack drop = Skill.Fishing.getFish(rod, base, player);
				int amount = drop.getAmount();
				if (Skill.Fishing.passive_left1.confirmChance(player))
					amount += (int) Skill.Fishing.passive_left1.getEffect();
				if (Skill.Fishing.upgrade_left3.confirmChance(player))
					amount *= (int) Skill.Fishing.upgrade_left3.getEffect();
				drop.setAmount(amount);
				Entity extra = player.getWorld().dropItem(base, drop);
				Vector dir = player.getEyeLocation().subtract(caught.getLocation()).toVector().normalize();
				dir.multiply(0.5d);
				extra.setVelocity(dir);
				if (Skill.Fishing.upgrade_right2.confirmChance(player)) {
					double percent = Skill.Fishing.upgrade_right2.getEffect();
					long remain = Skill.Fishing.active3.getRemainUseTime(player);
					if (remain > 0) {
						remain -= (long) (Skill.Fishing.active3.getCooldown(player) * percent);
						Skill.Fishing.active3.setLastUseTime(player, remain / 1000d);
						HashMapStore.setCooldown(player, Skill.Fishing.active3, remain);
						if (remain > 0)
							player.sendMessage(Skill.Fishing.active3.getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
									+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
									+ (String.format("%.1f", remain / 1000d)) + ")");
						else
							HashMapStore.sendCooldownMessage(player, Skill.Fishing.active3);
					}
				}
			}
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				int luck = 0;
				if (Skill.Fishing.passive_middle1.confirmChance(player)) {
					luck += (int) Skill.Fishing.passive_middle1.getEffect();
					player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1f, 1f);
					player.sendMessage(
							Skill.Fishing.passive_middle1.getDisplayName() + ChatColor.WHITE + " 발동으로 행운이 +1 증가합니다.");
				}
				player.getAttribute(Attribute.GENERIC_LUCK).setBaseValue(luck);
				ConfigStore.setPlayerConfig(player, "fishing.skills.active3.status", null);
			}, 1);
			if (Skill.Fishing.passive.confirmChance(player)) {
				ItemMeta meta = rod.getItemMeta();
				if (meta.getPersistentDataContainer().has(new NamespacedKey("minecraft", "passive"))) {
					meta.getPersistentDataContainer().remove(new NamespacedKey("minecraft", "passive"));
					rod.setItemMeta(meta);
					if (hand)
						player.getInventory().setItemInMainHand(rod);
					else
						player.getInventory().setItemInOffHand(rod);
				}
			}
			if (Skill.Fishing.transform_left2.confirmChance(player)) {
				Integer stacki = ConfigStore.getPlayerConfig(player, "fishing.skills.transform_left2.stack");
				int stack = stacki != null ? stacki : 0;
				int max = (int) Skill.Fishing.transform_left2.getEffect();
				stack++;
				if (stack > max)
					stack = max;
				ConfigStore.setPlayerConfig(player, "fishing.skills.transform_left2.stack", stack);
				player.sendMessage(Skill.Fishing.transform_left2.getDisplayName() + ChatColor.WHITE + " 스택 " + stack
						+ " / " + max);
			}
			if (Skill.Fishing.transform_middle3.confirmChance(player)) {
				Integer stacki = ConfigStore.getPlayerConfig(player, "fishing.skills.transform_middle3.stack");
				int stack = stacki != null ? stacki : 0;
				int max = (int) Skill.Fishing.transform_middle3.getEffect();
				stack++;
				if (stack > max)
					stack = max;
				ConfigStore.setPlayerConfig(player, "fishing.skills.transform_middle3.stack", stack);
				player.sendMessage(Skill.Fishing.transform_middle3.getDisplayName() + ChatColor.WHITE + " 스택 " + stack
						+ " / " + max);
			}
			if (Skill.Fishing.passive_left3.confirmChance(player)) {
				ItemStack drop = Skill.Fishing.getFish(rod, base, player);
				if (Skill.Fishing.passive_left1.confirmChance(player))
					drop.setAmount((int) (drop.getAmount() + Skill.Fishing.passive_left1.getEffect()));
				Entity extra = player.getWorld().dropItem(base, drop);
				Vector dir = player.getEyeLocation().subtract(caught.getLocation()).toVector().normalize();
				dir.multiply(0.5d);
				extra.setVelocity(dir);
			}
			if (ConfigStore.hasPlayerConfig(player, "Active.skills." + Skill.Fishing.active4.getKey() + ".status")) {
				ConfigStore.setPlayerConfig(player, "Active.skills." + Skill.Fishing.active4.getKey() + ".status",
						null);
				ConfigStore.<Boolean>setPlayerConfig(player, "fishing.skills.upgrade_right4_1.status", null);
				if (Skill.Fishing.transform_left4.confirmChance(player)) {
					ItemStack drop = Skill.Fishing.getFish(rod, base, player);
					int amount = drop.getAmount();
					if (Skill.Fishing.passive_left1.confirmChance(player))
						amount += (int) Skill.Fishing.passive_left1.getEffect();
					if (Skill.Fishing.upgrade_left4.confirmChance(player))
						amount *= (int) Skill.Fishing.upgrade_left4.getEffect();
					drop.setAmount(amount);
					Entity extra = player.getWorld().dropItem(base, drop);
					Vector dir = player.getEyeLocation().subtract(caught.getLocation()).toVector().normalize();
					dir.multiply(0.5d);
					extra.setVelocity(dir);
				}
			}
			if (Skill.Fishing.passive_left2.confirmChance(player)) {
				e.setCancelled(true);
				player.sendMessage(Skill.Fishing.passive_left2.getDisplayName() + ChatColor.WHITE + " 발동");
				hook.setWaitTime(50, 300);
				if (caught instanceof Item) {
					Item drop = (Item) caught;
					caught = player.getWorld().dropItem(hook.getLocation(), drop.getItemStack());
					Vector dir = player.getEyeLocation().subtract(caught.getLocation()).toVector().normalize();
					dir.multiply(0.5d);
					caught.setVelocity(dir);
					caught.setGlowing(true);
				}
				HashMapStore.setFishDelay(player);
			}
			if (Skill.Fishing.passive_middle2.confirmChance(player))
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					Items i = Items.valueOf(rod);
					if (i instanceof DurableItems)
						((DurableItems) i).modifyItemStack(rod, (int) Skill.Fishing.passive_middle2.getEffect());
					else {
						short add = (short) Skill.Fishing.passive.getEffect();
						short durability = rod.getDurability();
						durability = (short) (durability > add ? durability - add : 0);
						rod.setDurability(durability);
					}
					if (hand)
						player.getInventory().setItemInMainHand(rod);
					else
						player.getInventory().setItemInOffHand(rod);
					player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 2f);
				}, 1);
			if (Skill.Fishing.passive_right3.confirmChance(player)) {
				player.sendMessage(Skill.Fishing.passive_right3.getDisplayName() + ChatColor.WHITE + " 발동");
				for (UsableSkill active : Skill.Fishing.getActives()) {
					double percent = Skill.Fishing.passive_right3.getEffect();
					long remain = active.getRemainUseTime(player);
					if (remain > 0) {
						remain -= (long) (active.getCooldown(player) * percent);
						active.setLastUseTime(player, remain / 1000d);
						HashMapStore.setCooldown(player, active, remain);
						if (remain > 0)
							player.sendMessage(active.getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
									+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
									+ (String.format("%.1f", remain / 1000d)) + ")");
						else
							HashMapStore.sendCooldownMessage(player, active);
					}
				}
			}
			if (Skill.Fishing.upgrade_right4_2.confirmChance(player)) {
				int stack = HashMapStore.getBaitStack(player);
				stack++;
				int max = (int) Skill.Fishing.upgrade_right4_2.getEffect();
				if (stack > max)
					stack = max;
				HashMapStore.setBaitStack(player, stack);
				player.sendMessage(Skill.Fishing.upgrade_right4_2.getDisplayName() + ChatColor.WHITE + " 스택 " + stack
						+ " / " + max);
			}
			HashMapStore.setFishHook(player, null);
		}
			break;
		case FISHING: {
			FishHook hook = e.getHook(); // 100 600
			if (ConfigStore.hasPlayerConfig(player, "Active.skills." + Skill.Fishing.active4.getKey() + ".status")) {
				if (ConfigStore.hasPlayerConfig(player, "fishing.skills.upgrade_right4_1.status"))
					hook.setWaitTime(0, 1);
				else if (Skill.Fishing.transform_right4.confirmChance(player))
					hook.setWaitTime(25, 150);
				else
					hook.setWaitTime(50, 300);
				int stack = HashMapStore.getBaitStack(player);
				HashMapStore.setBaitStack(player, 0);
				Random r = new Random();
				for (int i = 0; i < stack; i++) {
					Vector dir = player.getLocation().getDirection().clone();
					dir.add(new Vector(r.nextDouble() - 0.5d, r.nextDouble() - 0.5d, r.nextDouble() - 0.5d).normalize()
							.multiply(0.25d));
					FishHook extra_hook = player.launchProjectile(FishHook.class, dir);
					Bukkit.getPluginManager()
							.callEvent(new PlayerFishEvent(player, null, extra_hook, e.getHand(), State.FISHING));
					extra_hook.setCustomName(ChatColor.GOLD + player.getName() + ChatColor.RED + "의 추가 찌");
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (extra_hook.isOnGround()
								|| !extra_hook.getLocation().getBlock().getType().equals(Material.WATER)) {
							extra_hook.getWorld().spawnParticle(Particle.ASH, extra_hook.getLocation(), 10, 0.1, 0.1,
									0.1);
							extra_hook.remove();
							Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
								FishHook original = HashMapStore.getFishHook(player);
								if (original != null && !original.isDead())
									((CraftPlayer) player).getHandle().ck = ((CraftFishHook) original).getHandle();
							}, 1);
						}
					}, 40);
				}
			}
			if (Skill.Fishing.passive_middle3.hasLearn(player))
				for (int i = 0; i < Skill.Fishing.passive_middle3.getEffect(1); i++)
					hook.setMetadata("passive_middle3_" + i, new FixedMetadataValue(Core.getCore(), true));
			ItemStack rod = hand ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
			if (Skill.Fishing.passive.confirmChance(player)) {
				ItemMeta rodMeta = rod.getItemMeta();
				rodMeta.getPersistentDataContainer().set(new NamespacedKey("minecraft", "passive"),
						PersistentDataType.BOOLEAN, true);
				rod.setItemMeta(rodMeta);
				if (hand)
					player.getInventory().setItemInMainHand(rod);
				else
					player.getInventory().setItemInOffHand(rod);
			} else {
				ItemMeta meta = rod.getItemMeta();
				if (meta.getPersistentDataContainer().has(new NamespacedKey("minecraft", "passive"))) {
					meta.getPersistentDataContainer().remove(new NamespacedKey("minecraft", "passive"));
					rod.setItemMeta(meta);
					if (hand)
						player.getInventory().setItemInMainHand(rod);
					else
						player.getInventory().setItemInOffHand(rod);
				}
			}
			if (!hook.hasMetadata("extra") && Skill.Fishing.passive_right1.confirmChance(player)
					&& HashMapStore.getExtraFishHooks(player) == null) {
				FishHook[] hooks = new FishHook[(int) Skill.Fishing.passive_right1.getEffect()];
				HashMapStore.setExtraFishHooks(player, hooks);
				Random r = new Random();
				for (int i = 0; i < hooks.length; i++) {
					Vector dir = player.getLocation().getDirection().clone();
					dir.add(new Vector(r.nextDouble() - 0.5d, r.nextDouble() - 0.5d, r.nextDouble() - 0.5d).normalize()
							.multiply(0.25d));
					FishHook extra_hook = player.launchProjectile(FishHook.class, dir);
					hooks[i] = extra_hook;
					Bukkit.getPluginManager()
							.callEvent(new PlayerFishEvent(player, null, extra_hook, e.getHand(), State.FISHING));
					extra_hook.setCustomName(ChatColor.GOLD + player.getName() + ChatColor.RED + "의 추가 찌");
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (extra_hook.isOnGround()
								|| !extra_hook.getLocation().getBlock().getType().equals(Material.WATER)) {
							extra_hook.getWorld().spawnParticle(Particle.ASH, extra_hook.getLocation(), 10, 0.1, 0.1,
									0.1);
							extra_hook.remove();
							Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
								FishHook original = HashMapStore.getFishHook(player);
								if (original != null && !original.isDead())
									((CraftPlayer) player).getHandle().ck = ((CraftFishHook) original).getHandle();

							}, 1);
						}
					}, 40);
				}
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					if (hook != null && !hook.isDead())
						((CraftPlayer) player).getHandle().ck = ((CraftFishHook) hook).getHandle();
				}, 1);
			}
			HashMapStore.setFishHook(player, hook);
			hook.setCustomName(ChatColor.GOLD + player.getName() + ChatColor.WHITE + "의 찌");
			hook.setCustomNameVisible(true);
		}
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void Events(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		Items i = Items.valueOf(item);
		if (i != null) {
			if (i.getKey().equals(Items.coca_cola.getKey())) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 5, 3, false, true, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 0, false, true, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 10, 0, false, true, true));
				e.setItem(new ItemStack(Material.AIR));
				Advancement adv = Bukkit
						.getAdvancement(new NamespacedKey("halfsurvival", "encyclopedia/hidden/drink_coca_cola"));
				if (adv != null)
					player.getAdvancementProgress(adv).awardCriteria("trigger");
			}
		}
	}

	@EventHandler
	public void Events(PlayerItemHeldEvent e) {
		Player player = e.getPlayer();
		ItemStack main = player.getInventory().getItem(e.getNewSlot());
		Items i = Items.valueOf(main);
		if (i instanceof SwordItems) {
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(0);
			player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
		} else
			for (Attribute attribute : new Attribute[] { Attribute.GENERIC_ATTACK_SPEED,
					Attribute.GENERIC_ATTACK_DAMAGE }) {
				AttributeInstance instance = player.getAttribute(attribute);
				instance.setBaseValue(instance.getDefaultValue());
			}
	}

	@EventHandler
	public void Events(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		LocationTask locationTask = HashMapStore.getChangePortal(player);
		if (locationTask != null && !locationTask.isCancelled()) {
			ConfigStore.setPortalName(locationTask.loc, e.getMessage());
			locationTask.cancel();
			player.sendMessage(ChatColor.GREEN + ConfigStore.LocationToString(locationTask.loc) + ChatColor.WHITE
					+ "의 이름이 '" + ChatColor.RED + e.getMessage() + ChatColor.WHITE + "'으로 변경되었습니다.");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Events(EntityTargetLivingEntityEvent e) {
		Entity et = e.getEntity();
		if (et.getType().equals(EntityType.EXPERIENCE_ORB)) {
			ExperienceOrb orb = (ExperienceOrb) et;
			LivingEntity target = e.getTarget();
			if (target instanceof Player && et.getCustomName() != null) {
				String name = et.getCustomName();
				if (name.equals("money")) {
					ConfigStore.setPlayerMoney((Player) target,
							ConfigStore.getPlayerMoney((Player) target) + orb.getExperience());
					target.sendMessage(ChatColor.GOLD + "" + orb.getExperience() + ChatColor.WHITE + "원을 획득하셨습니다.");
					orb.remove();
				} else {
					SkillType type = SkillType.valueOf(name);
					if (type != null) {
						type.addExp((Player) target, orb.getExperience());
						orb.remove();
					} else
						orb.remove();
				}
			} else
				orb.remove();
		}
	}

	@EventHandler
	public void Events(PlayerExpChangeEvent e) {
		e.setAmount(0);
	}

	@EventHandler
	public void Events(EntityShootBowEvent e) {
		LivingEntity shooter = e.getEntity();
		Entity etPro = e.getProjectile();
		ItemStack bow = e.getBow();
		Items item = Items.valueOf(bow);
		if (shooter instanceof Player)
			if (item != null && item instanceof BowItems) {
				Player player = (Player) shooter;
				BowItems bowitem = (BowItems) item;
				etPro.setMetadata("damage", new FixedMetadataValue(Core.getCore(), bowitem.getDamage()));
				etPro.setVelocity(etPro.getVelocity().multiply(bowitem.getArrowSpeed()));
				etPro.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
				etPro.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
				if (!HashMapStore.isActive4Delay(player) && Skill.Hunting.active4.hasDuration(player)) {
					WeaponType type = WeaponType.getWeaponType(bow);
					if (type != null) {
						HashMapStore.setActive4Delay(player);
						switch (type) {
						case Bow: {
							float power = (float) etPro.getVelocity().length();
							for (int i = 0; i < 3; i++) {
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
								Arrow arrow = etPro.getWorld().spawnArrow(etPro.getLocation(),
										etPro.getVelocity().clone(), power, 10);
								arrow.setMetadata("damage",
										new FixedMetadataValue(Core.getCore(), bowitem.getDamage()));
								arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
								arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
								arrow.setPickupStatus(PickupStatus.DISALLOWED);
							}
						}
							break;
						case Crossbow: {
							double damage = bowitem.getDamage();
							float power = (float) etPro.getVelocity().length();
							Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
								player.getWorld().playSound(player.getLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1f, 1f);
								Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
								arrow.setVelocity(arrow.getVelocity().normalize().multiply(power));
								arrow.setMetadata("damage", new FixedMetadataValue(Core.getCore(), damage));
								arrow.setMetadata("no_player", new FixedMetadataValue(Core.getCore(), true));
								arrow.setMetadata("no_delay", new FixedMetadataValue(Core.getCore(), true));
								arrow.setPickupStatus(PickupStatus.DISALLOWED);
							}, 10);
						}
							break;
						default:
							break;
						}
					}
				}
			}
	}
}
