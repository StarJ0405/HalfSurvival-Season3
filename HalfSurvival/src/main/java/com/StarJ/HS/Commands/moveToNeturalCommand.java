package com.StarJ.HS.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.StarJ.HS.Core;
import com.StarJ.HS.Systems.ConfigStore;

public class moveToNeturalCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
			// + + -
			Location loc = blockCommandSender.getBlock().getLocation().clone().add(0.5, 3, 0.5);
			Player player = getNearstPlayer(loc);
			if (player != null && player.isOnline())
				player.setMetadata("mtn", new FixedMetadataValue(Core.getCore(), true));

			for (int i = 0; i < 5; i++) {
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(-1, 0, 1), 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(1, 0, -1), 1, 0, 0, 0);
					//
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(2, 1, -2), 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(3, 2, -3), 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(4, 3, -4), 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(5, 4, -5), 1, 0, 0, 0);
					loc.getWorld().spawnParticle(Particle.SONIC_BOOM, loc.clone().add(6, 5, -6), 1, 0, 0, 0);
					loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f);
				}, i * 5);
				Location teleportLoc = loc.clone().add(1, -1, -1);
				teleportLoc.setYaw(-135.3f);
				teleportLoc.setPitch(-21.2f);
				Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
					if (player != null && player.isOnline())
						player.teleport(teleportLoc);
				}, i * 5);
			}
			Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
				if (player != null && player.isOnline()) {
					player.setVelocity(new Vector(0, 1, 0));
					loc.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1, 0, 0, 0);
					Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
						if (player != null && player.isOnline()) {
							player.setVelocity(new Vector(5, 1, -5));
							player.removeMetadata("mtn", Core.getCore());
							Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
								if (player != null && player.isOnline()) {
									player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getEyeLocation(),
											300, 0.5, 0.5, 0.5, 0);
									player.getWorld().playSound(player.getEyeLocation().clone(),
											Sound.BLOCK_PORTAL_TRIGGER, 0.5f, 3f);
									Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
										if (player != null && player.isOnline())
											player.teleport(ConfigStore.getRandomLocation());
									}, 5);
								}
							}, 2);
						}
					}, 10);
				}
			}, 25);
			return true;
		} else
			sender.sendMessage(ChatColor.RED + "이 명령어는 커맨드 블럭으로만 사용 가능합니다.");
		return false;
	}

	public Player getNearstPlayer(Location loc) {
		for (Entity et : loc.getBlock().getWorld().getNearbyEntities(loc, 1, 3, 1))
			if (et instanceof Player && !et.hasMetadata("mtn"))
				return (Player) et;
		return null;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();

		return list;
	}
}
