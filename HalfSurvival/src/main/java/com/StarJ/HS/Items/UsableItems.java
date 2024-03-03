package com.StarJ.HS.Items;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class UsableItems extends Items {

	public UsableItems(String key, String displayName, Material material, String[] lores, int model) {
		super(key, displayName, material, lores, model, false);
	}

	public abstract boolean Use(Player player, Block block);

	public abstract boolean Use(Player player, Entity et);

}
