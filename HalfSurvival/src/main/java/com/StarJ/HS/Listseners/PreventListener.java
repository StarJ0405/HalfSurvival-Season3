package com.StarJ.HS.Listseners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.PortalCreateEvent;

import com.StarJ.HS.Core;
import com.StarJ.HS.Systems.ConfigStore;

public class PreventListener implements Listener {
	@EventHandler
	public void Events(BlockExplodeEvent e) {
		e.blockList().clear();
	}

	@EventHandler
	public void Events(EntityExplodeEvent e) {
		e.blockList().clear();
	}

	@EventHandler
	public void Events(EntityPortalEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void Events(PlayerPortalEvent e) {
		Player player = e.getPlayer();
		TeleportCause cause = e.getCause();
		Location from = e.getFrom();
		switch (cause) {
		case NETHER_PORTAL:
			e.setCancelled(true);
			a: for (int x = -1; x <= 1; x++)
				for (int y = -1; y <= 1; y++)
					for (int z = -1; z <= 1; z++) {
						Location now = from.clone().add(x, y, z);
						if (now.getBlock().getType().equals(Material.NETHER_PORTAL)) {
							from = now;
							break a;
						}
					}
			while (from.clone().add(0, -1, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
				from = from.add(0, -1, 0);
			while (from.clone().add(0, 0, 1).getBlock().getType().equals(Material.NETHER_PORTAL))
				from = from.add(0, 0, 1);
			while (from.clone().add(1, 0, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
				from = from.add(1, 0, 0);
			ConfigStore.addPortal(from);
		// case END_PORTAL:
		// break;
		default:
			Advancement adv = Bukkit.getAdvancement(NamespacedKey
					.fromString("halfsurvival:newfunction/home"));
			player.getAdvancementProgress(adv).awardCriteria("trigger");
			Location to = from.getWorld().getSpawnLocation();
			e.setTo(to);
			Bukkit.getScheduler().runTask(Core.getCore(), () -> {
				player.teleport(to);
			});
		}
	}

	@EventHandler
	public void Events(PortalCreateEvent e) {
		if (e.getBlocks().size() > 0)
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				Location from = e.getBlocks().get(0).getLocation();
				a: for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						for (int z = -1; z <= 1; z++) {
							Location now = from.clone().add(x, y, z);
							if (now.getBlock().getType().equals(Material.NETHER_PORTAL)) {
								from = now;
								break a;
							}
						}
				while (from.clone().add(0, -1, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
					from = from.add(0, -1, 0);
				while (from.clone().add(0, 0, 1).getBlock().getType().equals(Material.NETHER_PORTAL))
					from = from.add(0, 0, 1);
				while (from.clone().add(1, 0, 0).getBlock().getType().equals(Material.NETHER_PORTAL))
					from = from.add(1, 0, 0);
				ConfigStore.addPortal(from);
			}, 1);
	}

	@EventHandler
	public void Events(FoodLevelChangeEvent e) {
		e.setFoodLevel(19);
	}
}