package com.StarJ.HS.Systems;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class Attributes {
	protected final Attribute attribute;
	protected final double amount;

	public Attributes(Attribute attribute, double amount) {
		this.attribute = attribute;
		this.amount = amount;
	}

	public void apply(LivingEntity livingEntity) {
		livingEntity.getAttribute(this.attribute).setBaseValue(this.amount);
	}

}
