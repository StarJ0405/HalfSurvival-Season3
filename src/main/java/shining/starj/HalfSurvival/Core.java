package shining.starj.HalfSurvival;

import shining.starj.HalfSurvival.Commands.*;
import shining.starj.HalfSurvival.Entities.Pets.CatPet;
import shining.starj.HalfSurvival.Entities.Pets.DogPet;
import shining.starj.HalfSurvival.Entities.Pets.Pets;
import shining.starj.HalfSurvival.Items.Items;
import shining.starj.HalfSurvival.Listseners.*;
import shining.starj.HalfSurvival.Recipes.CustomRecipe;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.ConfigStore.PetInfo;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Core extends JavaPlugin {
	private static Core core;

	public static Core getCore() {
		return core;
	}

	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 시작되었습니다.");
		core = this;
		//
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new PreventListener(), this);
		pm.registerEvents(new TimeListener(), this);
		//
		getCommand("exp").setExecutor(new ExpCommand());
		getCommand("home").setExecutor(new HomeCommand());
		getCommand("item").setExecutor(new ItemCommand());
		getCommand("mypet").setExecutor(new MyPetCommand());
		getCommand("money").setExecutor(new MoneyCommand());
		getCommand("movetonetural").setExecutor(new moveToNeturalCommand());
		getCommand("skill").setExecutor(new SkillCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("sudo").setExecutor(new SudoCommand());
		getCommand("test").setExecutor(new TestCommand());
		getCommand("text").setExecutor(new TextCommand());
		//
		initial();
		//
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 정상적으로 불러와졌습니다.");
	}

	private void initial() {
		Bukkit.createWorld(new WorldCreator("town").type(WorldType.NORMAL));
		//
		CustomRecipe.removeVanilla();
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
			world.setGameRule(GameRule.DISABLE_RAIDS, true);
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			world.setGameRule(GameRule.DO_ENTITY_DROPS, true);
			world.setGameRule(GameRule.DO_FIRE_TICK, false);
			world.setGameRule(GameRule.DO_INSOMNIA, false);
			world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
			world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
			world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
			world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
			world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
			world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			world.setGameRule(GameRule.KEEP_INVENTORY, false);
			world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
			for (Entity et : world.getEntities())
				if (!(et instanceof Player))
					if (et instanceof LivingEntity) {
						LivingEntity le = (LivingEntity) et;
						if (!ConfigStore.hasEntityConfig(le, "type"))
							le.remove();
					} else if (et instanceof ItemDisplay || et instanceof BlockDisplay || et instanceof TextDisplay) {
						if (!ConfigStore.hasEntityConfig(et, "type"))
							et.remove();
					} else if (et instanceof Interaction) {
						if (ConfigStore.hasEntityConfig(et, "type")) {
							String type = ConfigStore.<String>getEntityConfig(et, "type");
							if (type != null)
								switch (type) {
								case "FishTrap":
									if (!ConfigStore.hasEntityConfig(et, "fish")) {
										Long duration = ConfigStore.<Long>getEntityConfig(et, "duration");
										Skill.Fishing.active1.setFishTrapDuration((Interaction) et,
												duration == null || System.currentTimeMillis() - duration < 0 ? 0
														: System.currentTimeMillis() - duration);
									}
									break;
								}
						} else
							et.remove();
					} else if (et instanceof Arrow) {
						Arrow arrow = (Arrow) et;
						if (!arrow.getPickupStatus().equals(PickupStatus.ALLOWED))
							et.remove();
					}
		}
		Block block = getTimeLocation().getBlock();
		block.setType(Material.FURNACE);
		Furnace furnace = (Furnace) block.getState();
		furnace.getInventory().setFuel(new ItemStack(Material.LAVA_BUCKET));
		furnace.getInventory().setResult(new ItemStack(Material.AIR));
		ItemStack time = Items.time.getItemStack();
		time.setAmount(64);
		furnace.getInventory().setSmelting(time);

		for (CustomRecipe recipe : CustomRecipe.values())
			recipe.regist();
		Skill.Mining.values();
		Skill.Farming.values();
		Skill.Fishing.values();
		Skill.Hunting.values();
		for (Player player : Bukkit.getOnlinePlayers()) {
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
		ConfigStore.confirmBlockConfig();
		ConfigStore.confirmPortals();
		ConfigStore.confirmEntityConfig();
	}

	public Location getTimeLocation() {
		Location loc = Bukkit.getWorlds().get(0).getSpawnLocation().getBlock().getLocation();
		loc.setY(-64);
		return loc;
	}

	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			HashMapStore.resetCooldown(player);
			HashMapStore.resetDuration(player);
			LivingEntity pet = ConfigStore.getPlayerPet(player);
			if (pet != null)
				pet.remove();
		}
		//
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 종료되었습니다.");
	}

}
