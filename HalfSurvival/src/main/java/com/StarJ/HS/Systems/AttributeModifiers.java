package com.StarJ.HS.Systems;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeModifiers extends Attributes {
	protected final UUID uuid;
	protected final String name;
	protected final Operation operation;
	protected final EquipmentSlot slot;

	public AttributeModifiers(Attribute attribute, double amount, UUID uuid, String name, Operation operation,
			EquipmentSlot slot) {
		super(attribute, amount);
		this.uuid = uuid;
		this.name = name;
		this.operation = operation;
		this.slot = slot;
	}

	public AttributeModifiers(Attribute attribute, double amount, UUID uuid, Operation operation, EquipmentSlot slot) {
		this(attribute, amount, uuid, uuid.toString(), operation, slot);
	}

	public AttributeModifiers(Attribute attribute, double amount, Operation operation, EquipmentSlot slot) {
		this(attribute, amount, UUID.randomUUID(), operation, slot);
	}

	public AttributeModifiers(Attribute attribute, double amount, UUID uuid, String name, Operation operation) {
		this(attribute, amount, uuid, name, operation, null);
	}

	public AttributeModifiers(Attribute attribute, double amount, UUID uuid, Operation operation) {
		this(attribute, amount, uuid, uuid.toString(), operation, null);
	}

	public AttributeModifiers(Attribute attribute, double amount, Operation operation) {
		this(attribute, amount, UUID.randomUUID(), operation);
	}

	private AttributeModifier getAttributeModifer() {
		if (this.slot == null)
			return new AttributeModifier(this.uuid, this.name, this.amount, this.operation);
		else
			return new AttributeModifier(this.uuid, this.name, this.amount, this.operation, this.slot);
	}

	@Override
	public void apply(LivingEntity livingEntity) {
		livingEntity.getAttribute(this.attribute).addModifier(getAttributeModifer());
	}

	public void apply(ItemStack item) {
		if (item != null) {
			ItemMeta meta = item.getItemMeta();
			meta.addAttributeModifier(this.attribute, getAttributeModifer());
			item.setItemMeta(meta);
		}
	}
}
