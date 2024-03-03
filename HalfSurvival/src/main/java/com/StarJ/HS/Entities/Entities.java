package com.StarJ.HS.Entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.StarJ.HS.Core;
import com.StarJ.HS.Entities.Pets.CatPet;
import com.StarJ.HS.Entities.Pets.DogPet;
import com.StarJ.HS.Systems.AttributeModifiers;
import com.StarJ.HS.Systems.Attributes;

public abstract class Entities {
	private static final List<Entities> list = new ArrayList<Entities>();
	// 펫
	public static final DogPet DOG_PET = new DogPet("dog_pet", "강아지");
	public static final CatPet CAT_PET = new CatPet("cat_pet", "고양이");
	// 몬스터
	public static final SkeletonArcher SKELETON_ARCHER = new SkeletonArcher("skeleton_archer", "스켈레톤 아쳐");
	//
	protected final String key;
	protected final String customName;
	protected final Attributes[] attributes;
	protected final AttributeModifiers[] attributeModifiers;

	public Entities(String key, String customName, Attributes[] attributes) {
		this(key, customName, attributes, new AttributeModifiers[0]);
	}

	public Entities(String key, String customName, Attributes[] attributes, AttributeModifiers[] attributeModifiers) {
		this.key = key;
		this.customName = customName;
		this.attributes = attributes;
		this.attributeModifiers = attributeModifiers;
		//
		list.add(this);
	}

	public String getKey() {
		return key;
	}

	public String getCustomName() {
		return customName;
	}

	public abstract LivingEntity spawnEntity(Location loc);

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
			setNameWithHealth(livingEntity, 0);
		}
	}

	public void setNameWithHealth(LivingEntity livingEntity, double damage) {
		if (livingEntity != null && !livingEntity.isDead())
			livingEntity.setCustomName(getCustomName() + " " + ChatColor.RED
					+ String.format("%.0f", Math.max(0, livingEntity.getHealth() - damage)) + ChatColor.WHITE + " / "
					+ ChatColor.GREEN
					+ String.format("%.0f", livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
	}

	public static Entities valueOf(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata("type"))
			for (MetadataValue value : livingEntity.getMetadata("type"))
				if (value.getOwningPlugin().equals(Core.getCore()))
					return valueOf(value.asString());
		return null;
	}

	public static Entities valueOf(String key) {
		for (Entities entities : list)
			if (entities.key.equals(key))
				return entities;
		return null;
	}

	public static List<Entities> values() {
		return list;
	}
}
