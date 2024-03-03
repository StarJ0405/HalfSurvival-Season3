package com.StarJ.HS.Entities.Pets;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.metadata.FixedMetadataValue;

import com.StarJ.HS.Core;
import com.StarJ.HS.Entities.Entities;
import com.StarJ.HS.Systems.AttributeModifiers;
import com.StarJ.HS.Systems.Attributes;

public abstract class Pets extends Entities {
	public Pets(String key, String customName) {
		super(key, customName, new Attributes[] { new Attributes(Attribute.GENERIC_MAX_HEALTH, 1d),
				new Attributes(Attribute.GENERIC_MOVEMENT_SPEED, 0.6d) }, new AttributeModifiers[0]);
	}

	public void setOwner(Player player, LivingEntity livingEntity) {
		if (livingEntity instanceof Tameable) {
			Tameable tameable = (Tameable) livingEntity;
			tameable.setOwner(player);
			String name = livingEntity.getCustomName();
			name = ChatColor.GOLD + player.getName() + ChatColor.WHITE + "님의 " + name;
			livingEntity.setCustomName(name);
		}
	}

	@Override
	public void applyDefaultAttributes(LivingEntity livingEntity) {
		for (Attributes attributes : this.attributes)
			attributes.apply(livingEntity);
		for (AttributeModifiers attributeModifiers : this.attributeModifiers)
			attributeModifiers.apply(livingEntity);
		livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		livingEntity.setMetadata("type", new FixedMetadataValue(Core.getCore(), this.key));
		if (customName != null && !customName.equals("")) {
			livingEntity.setCustomName(this.customName);
			livingEntity.setCustomNameVisible(true);
		}
	}

	public static DyeColor getRandomColor() {
		DyeColor[] dyeColors = DyeColor.values();
		Random r = new Random();
		return dyeColors[r.nextInt(dyeColors.length)];
	}

	public static Pets getRandomType() {
		Pets[] pets = new Pets[] { Entities.DOG_PET, Entities.CAT_PET };
		Random r = new Random();
		return pets[r.nextInt(pets.length)];
	}

	public static Type getRandomCatType() {
		Type[] types = Type.values();
		Random r = new Random();
		return types[r.nextInt(types.length)];
	}

	public static int getRandomSize() {
		return Math.min(6 * 9, (Math.max(1, (int) Math.sqrt(new Random().nextInt(6 * 6 * 9 * 9 + 1)))));
	}
}
