package com.StarJ.HS.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.StarJ.HS.Entities.Pets.CatPet;
import com.StarJ.HS.Entities.Pets.DogPet;
import com.StarJ.HS.Entities.Pets.Pets;
import com.StarJ.HS.Systems.ConfigStore;
import com.StarJ.HS.Systems.ConfigStore.PetInfo;
import com.StarJ.HS.Systems.GUIs;

public class MyPetCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length >= 1)
				if (args[0].equalsIgnoreCase("on")) {
					LivingEntity livingEntity = ConfigStore.getPlayerPet(player);
					if (livingEntity != null)
						livingEntity.remove();
					PetInfo info = ConfigStore.getPlayerPetInfo(player);
					Pets pets = null;
					DyeColor color = null;
					Type type = null;
					if (info == null) {
						pets = Pets.getRandomType();
						color = Pets.getRandomColor();
						if (pets instanceof DogPet)
							ConfigStore.setPlayerPetType(player, pets, color, 1, null);
						else {
							type = Pets.getRandomCatType();
							ConfigStore.setPlayerPetType(player, pets, color, 1, type);
						}
					} else {
						pets = info.getPets();
						color = info.getColor();
						type = info.getType();
					}
					livingEntity = pets.spawnEntity(player.getLocation());
					pets.setOwner(player, livingEntity);
					if (pets instanceof DogPet)
						((DogPet) pets).setColor(livingEntity, color);
					else if (pets instanceof CatPet)
						((CatPet) pets).setColor(livingEntity, color, type);
					ConfigStore.setPlayerPet(player, livingEntity);
					ConfigStore.setPlayerConfig(player, "pets.status", true);
					player.closeInventory();
					Advancement adv = Bukkit.getAdvancement(new NamespacedKey("halfsurvival", "newfunction/mypet"));
					if (adv != null)
						player.getAdvancementProgress(adv).awardCriteria("trigger");
					return true;
				} else if (args[0].equalsIgnoreCase("off")) {
					LivingEntity livingEntity = ConfigStore.getPlayerPet(player);
					if (livingEntity != null)
						livingEntity.remove();
					ConfigStore.setPlayerConfig(player, "pets.status", null);
					return true;
				}
			GUIs.myPet.openInv(player);
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		final List<String> list = new ArrayList<String>();
		int len = args.length;
		if (len == 1) {
			if (args[len - 1].equals("") || "on".startsWith(args[len - 1].toLowerCase()))
				list.add("on");
			if (args[len - 1].equals("") || "off".startsWith(args[len - 1].toLowerCase()))
				list.add("off");
		}
		return list;
	}
}
