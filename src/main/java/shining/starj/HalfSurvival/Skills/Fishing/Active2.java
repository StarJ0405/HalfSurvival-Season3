package shining.starj.HalfSurvival.Skills.Fishing;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Items.DurableItems;
import shining.starj.HalfSurvival.Items.Items;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.ItemBlockDisplay;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftFishHook;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Active2 extends UsableSkill {

	public Active2(String key, String displayName, Skill[] preSkills, double cooldown, useSlot useSlot) {
		super(SkillType.fishing, key, displayName, preSkills, cooldown, 0, useSlot);
	}

	@Override
	public List<String> getLore(Player player) {
		List<String> lore = new ArrayList<String>();
		lore.add(useSlot.getLore());
		if (Skill.Fishing.upgrade_left2_2.hasLearn(player))
			lore.add(ChatColor.WHITE + "물을 향해 그물을 던져 물고기를 4마리씩 두번 낚습니다.");
		else
			lore.add(ChatColor.WHITE + "물을 향해 그물을 던져 4마리의 물고기를 낚습니다.");
		if (Skill.Fishing.upgrade_left2_1.hasLearn(player))
			lore.add(ChatColor.GREEN + "낚시대에 영향을 받습니다.");
		else
			lore.add(ChatColor.GRAY + "낚시대에 영향을 없이 나무 낚시대와 동일");
		lore.add(Skill.Fishing.active3.getDisplayName() + ChatColor.RED + " 및 " + Skill.Fishing.active4.getDisplayName()
				+ ChatColor.RED + " 미적용");
		return lore;
	}

	@Override
	public boolean use(Player player) {
		Integer stacki = ConfigStore.getPlayerConfig(player, "fishing.skills.transform_left2.stack");
		int stack = stacki != null ? stacki : 0;
		int max = (int) Skill.Fishing.transform_left2.getEffect();
		if (stack == max || super.use(player)) {
			Block b = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
			if (b == null || !b.getType().equals(Material.WATER)) {
				setLastUseTime(player, 0);
				HashMapStore.setCooldown(player, this, 0);
				player.sendMessage(ChatColor.RED + "물을 하나를 대상으로 해야합니다");
			} else {
				if (stack == max) {
					ConfigStore.setPlayerConfig(player, "fishing.skills.transform_left2.stack", 0);
					HashMapStore.setDelay(player);
				}
				ItemStack tool = player.getInventory().getItemInOffHand();
				boolean upgrade_left2_1 = Skill.Fishing.upgrade_left2_1.confirmChance(player);
				List<ItemStack> items = new ArrayList<ItemStack>();
				int times = (int) (Skill.Fishing.upgrade_left2_2.confirmChance(player)
						? Skill.Fishing.upgrade_left2_2.getEffect()
						: 1);
				int amount = 4 * (upgrade_left2_1 ? 2 : 1);
				float luck = 0f;
				if (Skill.Fishing.transform_middle2.confirmChance(player))
					luck = (float) player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue();
				if (Skill.Fishing.upgrade_middle2.confirmChance(player)) {
					times = times * amount + (int) luck;
					amount = 1;
					player.getAttribute(Attribute.GENERIC_LUCK).setBaseValue(0d);
				}
				if (Skill.Fishing.upgrade_middle4.confirmChance(player)) {
					amount *= Skill.Fishing.upgrade_middle4.getEffect();
					player.playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f);
					player.sendMessage(Skill.Fishing.upgrade_middle4.getDisplayName() + ChatColor.WHITE + " 발동");
				}
				Items i = Items.valueOf(tool);
				if (i != null && i instanceof DurableItems)
					((DurableItems) i).modifyItemStack(tool, -1);
				else {
					ItemMeta toolMeta = tool.getItemMeta();
					if (toolMeta instanceof Damageable) {
						Damageable damageable = (Damageable) toolMeta;
						damageable.setDamage(damageable.getDamage() + 1);
						tool.setItemMeta(toolMeta);
						player.getInventory().setItemInOffHand(tool);
					}
				}
				if (Skill.Fishing.transform_right2.confirmChance(player)) {
					times = times * amount + (int) luck;
					amount = 1;
					FishHook hook = HashMapStore.getFishHook(player);
					Random r = new Random();
					for (int time = 0; time < times; time++) {
						Vector dir = player.getLocation().getDirection().clone();
						dir.add(new Vector(r.nextDouble() - 0.5d, r.nextDouble() - 0.5d, r.nextDouble() - 0.5d)
								.normalize().multiply(0.25d));
						FishHook extra_hook = player.launchProjectile(FishHook.class, dir);
						extra_hook.setMetadata("extra", new FixedMetadataValue(Core.getCore(), true));
						Bukkit.getPluginManager().callEvent(
								new PlayerFishEvent(player, null, extra_hook, EquipmentSlot.OFF_HAND, State.FISHING));
						extra_hook.setCustomName(ChatColor.GOLD + player.getName() + ChatColor.RED + "의 추가 찌");
						extra_hook.setCustomNameVisible(true);
						Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
							if (extra_hook.isOnGround()
									|| !extra_hook.getLocation().getBlock().getType().equals(Material.WATER)) {
								extra_hook.getWorld().spawnParticle(Particle.ASH, extra_hook.getLocation(), 10, 0.1,
										0.1, 0.1);
								extra_hook.remove();
								Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
									FishHook original = HashMapStore.getFishHook(player);
									if (original != null && !original.isDead())
										((CraftPlayer) player).getHandle().ck = ((CraftFishHook) original).getHandle();
								}, 1);
							}
						}, 40);

					}
					HashMapStore.setFishHook(player, hook);
					if (hook == null)
						((CraftPlayer) player).getHandle().ck = null;
				} else {
					ItemStack net = new ItemStack(Material.COBWEB);
					ItemMeta meta = net.getItemMeta();
					meta.setCustomModelData(1111112);
					net.setItemMeta(meta);
					Location base = b.getLocation();
					ItemDisplay itemDisplay = ItemBlockDisplay.UP.createDisplay(base, net);
					itemDisplay.setItemStack(net);
					Transformation transformation = itemDisplay.getTransformation();
					transformation.getScale().set(5f, 5f, 5f);
					transformation.getTranslation().add(0f, 1f, 0f);
					itemDisplay.setTransformation(transformation);
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						itemDisplay.setInterpolationDelay(0);
						itemDisplay.setInterpolationDuration(20);
						transformation.getScale().set(0.1f, 0.1f, 0.1f);
						transformation.getTranslation().sub(0f, 2f, 0f);
						itemDisplay.setTransformation(transformation);
					}, 5);
					for (int time = 0; time < times; time++) {
						ItemStack fish = upgrade_left2_1 ? Skill.Fishing.getFish(tool, base, player, luck)
								: Skill.Fishing.getFish(Skill.Fishing.woodenLootTables, base, player);
						fish.setAmount(amount);
						items.add(fish);
					}
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						for (ItemStack item : items) {
							Item drop = base.getWorld().dropItem(base, item);
							drop.setOwner(player.getUniqueId());
							drop.setGlowing(true);
						}
						player.playSound(player, Sound.ITEM_BUCKET_FILL_FISH, 1f, 1f);
						itemDisplay.remove();
					}, 5 + 20);
				}
				player.playSound(player, Sound.ENTITY_FISHING_BOBBER_THROW, 1f, 1f);
				if (Skill.Fishing.transform_right2.confirmChance(player)
						&& Skill.Fishing.upgrade_right2.confirmChance(player)) {
					double percent = Skill.Fishing.upgrade_right2.getEffect();
					long remain = getRemainUseTime(player);
					if (remain > 0) {
						remain -= (long) (getCooldown(player) * percent);
						setLastUseTime(player, remain / 1000d);
						HashMapStore.setCooldown(player, this, remain);
						if (remain > 0)
							player.sendMessage(getDisplayName() + ChatColor.WHITE + "의 재사용 대기시간이 "
									+ String.format("%.1f", percent * 100) + "% 감소했습니다." + ChatColor.GRAY + "("
									+ (String.format("%.1f", remain / 1000d)) + ")");
						else
							HashMapStore.sendCooldownMessage(player, this);
					}
				}
			}
		}
		return true;
	}

}
