package shining.starj.HalfSurvival.Skills.Fishing;

import shining.starj.HalfSurvival.Core;
import shining.starj.HalfSurvival.Skills.Skill;
import shining.starj.HalfSurvival.Skills.UsableSkill;
import shining.starj.HalfSurvival.Systems.ConfigStore;
import shining.starj.HalfSurvival.Systems.HashMapStore;
import shining.starj.HalfSurvival.Systems.ItemBlockDisplay;
import shining.starj.HalfSurvival.Systems.SkillType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.*;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Active1 extends UsableSkill {

    public Active1(String key, String displayName, Skill[] preSkills, double cooldown, double duration,
                   useSlot useSlot) {
        super(SkillType.fishing, key, displayName, preSkills, cooldown, duration, useSlot);
    }

    @Override
    public long getCooldown(Player player) {
        return (long) (super.getCooldown(player)
                * (Skill.Fishing.transform_right1.confirmChance(player) ? Skill.Fishing.transform_right1.getEffect()
                : 1));
    }

    @Override
    public List<String> getLore(Player player) {
        List<String> lore = new ArrayList<String>();
        lore.add(useSlot.getLore());
        lore.add(ChatColor.WHITE + "지속시간이 지나면 물고기가 잡힙니다.");
        if (Skill.Fishing.transform_left1.hasLearn(player))
            lore.add(ChatColor.GREEN + "두 종류 포획");
        lore.add(ChatColor.GRAY + "물고기 잡힌 후 우클릭시 회수 가능");
        if (Skill.Fishing.upgrade1.hasLearn(player))
            lore.add(ChatColor.GRAY + "돌 낚시대와 동일한 확률");
        else
            lore.add(ChatColor.GRAY + "나무 낚시대와 동일한 확률");
        return lore;
    }

    @Override
    public boolean use(Player player) {
        if (super.use(player)) {
            Block b = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
            if (b == null || !b.getType().equals(Material.WATER)) {
                setLastUseTime(player, 0);
                HashMapStore.setCooldown(player, this, 0);
                player.sendMessage(ChatColor.RED + "물을 하나를 대상으로 해야합니다");
            } else {
                HashMapStore.setDuration(player, this, duration);
                ItemStack fishTrap = new ItemStack(Material.COBWEB);
                ItemMeta meta = fishTrap.getItemMeta();
                meta.setCustomModelData(1111111);
                fishTrap.setItemMeta(meta);
                Location base = b.getLocation();
                //
                Interaction interaction = (Interaction) base.getWorld().spawnEntity(base.clone().add(0.5, 0, 0.5),
                        EntityType.INTERACTION);
                ConfigStore.<String>setEntityConfig(interaction, "type", "FishTrap");
                interaction.setInteractionHeight(1);
                interaction.setInteractionWidth(1);
                interaction.setResponsive(true);
                //
                for (ItemBlockDisplay ibd : ItemBlockDisplay.values()) {
                    ItemDisplay itemDisplay = ibd.createDisplay(base, fishTrap);
                    ConfigStore.<String>setEntityConfig(itemDisplay, "type", "FishTrap_itemBlock");
                    ConfigStore.<String>setEntityConfig(interaction, ibd.name(), itemDisplay.getUniqueId().toString());
                }
                int luck = 0;
                if (Skill.Fishing.transform_middle1.confirmChance(player)) {
                    luck = (int) player.getAttribute(Attribute.GENERIC_LUCK).getBaseValue();
                    player.sendMessage(Skill.Fishing.transform_middle1.getDisplayName() + ChatColor.WHITE + "으로 행운 "
                            + ChatColor.YELLOW + luck + ChatColor.WHITE + " 적용");
                }
                //
                TextDisplay textDisplay = (TextDisplay) base.getWorld().spawnEntity(base.clone().add(0.5, 0, 0.5),
                        EntityType.TEXT_DISPLAY);
                textDisplay.setText(ChatColor.GOLD + player.getName() + ChatColor.GREEN + "의 통발"
                        + (luck > 0 ? ChatColor.YELLOW + " (+" + luck + ")" : ""));
                textDisplay.setTransformation(new Transformation(new Vector3f(0f, 1f, 0f),
                        new Quaternionf(0f, 0f, 0f, 1f), new Vector3f(1f, 1f, 1f), new Quaternionf(0f, 0f, 0f, 1f)));
                textDisplay.setBillboard(Billboard.VERTICAL);
                textDisplay.setAlignment(TextAlignment.CENTER);
                ConfigStore.<String>setEntityConfig(textDisplay, "type", "FishTrap_text");
                ConfigStore.<String>setEntityConfig(interaction, "text", textDisplay.getUniqueId().toString());
                //
                player.playSound(player.getEyeLocation(), Sound.BLOCK_WOOD_PLACE, 1f, 1f);
                String[] lootTables = Skill.Fishing.upgrade1.confirmChance(player) ? Skill.Fishing.stoneLootTables
                        : Skill.Fishing.woodenLootTables;

                ItemStack fish = Skill.Fishing.getFish(lootTables, base, player, luck);
                fish.setAmount(4);
                ConfigStore.<ItemStack>setEntityConfig(interaction, "item", fish);
                if (Skill.Fishing.transform_left1.confirmChance(player)) {
                    fish = Skill.Fishing.getFish(lootTables, base, player, luck);
                    fish.setAmount(4);
                    ConfigStore.<ItemStack>setEntityConfig(interaction, "item2", fish);
                }
                setFishTrapDuration(interaction, this.duration);
                ConfigStore.<Long>setEntityConfig(interaction, "duration", System.currentTimeMillis() + this.duration);

            }
        }
        return true;
    }

    public void setFishTrapDuration(Interaction interaction, long duration) {
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            Location base = interaction.getLocation().clone().subtract(0.5d, 0, 0.5d);
            ItemDisplay itemDisplay = (ItemDisplay) base.getWorld().spawnEntity(base, EntityType.ITEM_DISPLAY);
            itemDisplay.setTransformation(new Transformation(new Vector3f(0.5f, 0.5f, 0.5f),
                    new Quaternionf(0f, 0f, 0f, 1f), new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf(0f, 0f, 0f, 1f)));
            itemDisplay.setBillboard(Billboard.FIXED);
            itemDisplay.setGlowing(true);
            itemDisplay.setItemStack(ConfigStore.<ItemStack>getEntityConfig(interaction, "item"));
            ConfigStore.<String>setEntityConfig(itemDisplay, "type", "FishTrap_fish");
            ConfigStore.<String>setEntityConfig(interaction, "fish", itemDisplay.getUniqueId().toString());
            if (ConfigStore.hasEntityConfig(interaction, "item2")) {
                itemDisplay = (ItemDisplay) base.getWorld().spawnEntity(base, EntityType.ITEM_DISPLAY);
                itemDisplay.setTransformation(
                        new Transformation(new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf(0f, 0.707f, 0f, 0.707f),
                                new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf(0f, 0f, 0f, 1f)));
                itemDisplay.setBillboard(Billboard.FIXED);
                itemDisplay.setGlowing(true);
                itemDisplay.setItemStack(ConfigStore.<ItemStack>getEntityConfig(interaction, "item2"));
                ConfigStore.<String>setEntityConfig(itemDisplay, "type", "FishTrap_fish");
                ConfigStore.<String>setEntityConfig(interaction, "fish2", itemDisplay.getUniqueId().toString());
            }

        }, (int) (duration * 20d / 1000));
    }

    @Override
    public void setDurationTime(Player player, double duration) {
        super.setDurationTime(player, duration);
        if (duration > 0) {
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.FAST_DIGGING, (int) (duration * 20), 0, true, false, false));
        } else
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
