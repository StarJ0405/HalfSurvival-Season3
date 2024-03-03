package com.StarJ.HS.Listseners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import com.StarJ.HS.Core;
import com.StarJ.HS.Items.Items;

public class TimeListener implements Listener {

	@EventHandler
	public void Events(FurnaceSmeltEvent e) {
		ItemStack item = e.getResult();
		Items i = Items.valueOf(item);
		Block block = e.getBlock();
		if (i != null)
			if (i.getKey().equals(Items.time.getKey())) {
				Furnace furnace = (Furnace) block.getState();
				if (block.getLocation().equals(Core.getCore().getTimeLocation())) {
					furnace.getInventory().setFuel(new ItemStack(Material.LAVA_BUCKET));
					furnace.getInventory().setResult(new ItemStack(Material.AIR));
					ItemStack time = Items.time.getItemStack();
					time.setAmount(64);
					furnace.getInventory().setSmelting(time);
					World world = block.getWorld();
					// 600
					world.setTime(world.getTime() + 20l);
					if (world.getTime() == 6000l) {
//						world.setTime(18000l);
						Bukkit.broadcastMessage("day");
						confirmWeather(world);
					} else if (world.getTime() == 18000l) {
//						world.setTime(6000l);
						Bukkit.broadcastMessage("night");
						confirmWeather(world);
					}
//					for (Entity et : world.getEntities())
				} else
					block.setType(Material.BEDROCK, true);
			}
	}

	void confirmWeather(World world) {
		Random r = new Random();
		if (r.nextDouble() < 0.1d) {
			world.setStorm(true);
			if (r.nextBoolean())
				world.setThundering(true);
		} else {
			world.setStorm(false);
			world.setThundering(false);
		}
	}

	@EventHandler
	public void Events(FurnaceBurnEvent e) {
		Block block = e.getBlock();
		if (block.getLocation().equals(Core.getCore().getTimeLocation())) {
			Furnace furnace = (Furnace) block.getState();
			furnace.getInventory().setFuel(new ItemStack(Material.LAVA_BUCKET));
		}
	}
}
