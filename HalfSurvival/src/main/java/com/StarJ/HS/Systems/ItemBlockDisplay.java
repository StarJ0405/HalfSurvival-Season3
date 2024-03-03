package com.StarJ.HS.Systems;

import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum ItemBlockDisplay {
	FRONT(new Vector3f(0.5f, 0.5f, 0.1f)), BACK(new Vector3f(0.5f, 0.5f, 0.9f)),
	LEFT(new Vector3f(0.1f, 0.5f, 0.5f), new Quaternionf(0f, -0.707f, 0f, 0.707f)),
	RIGHT(new Vector3f(0.9f, 0.5f, 0.5f), new Quaternionf(0f, -0.707f, 0f, 0.707f)),
	UP(new Vector3f(0.5f, 0.9f, 0.5f), new Quaternionf(0.707f, 0f, 0f, 0.707f)),
	DOWN(new Vector3f(0.5f, 0f, 0.5f), new Quaternionf(0.707f, 0f, 0f, 0.707f));

	private final Vector3f translation;
	private final Quaternionf leftRotation;
	private final Quaternionf rightRotation;

	private ItemBlockDisplay(Vector3f translation) {
		this(translation, new Quaternionf(0f, 0f, 0f, 1f), new Quaternionf(0f, 0f, 0f, 1f));
	}

	private ItemBlockDisplay(Vector3f translation, Quaternionf leftRotation) {
		this(translation, leftRotation, new Quaternionf(0f, 0f, 0f, 1f));
	}

	private ItemBlockDisplay(Vector3f translation, Quaternionf leftRotation, Quaternionf rightRotation) {
		this.translation = translation;
		this.leftRotation = leftRotation;
		this.rightRotation = rightRotation;
	}

	public ItemDisplay createDisplay(Location base, ItemStack item) {
		ItemDisplay display = (ItemDisplay) base.getWorld().spawnEntity(base, EntityType.ITEM_DISPLAY);
		display.setTransformation(
				new Transformation(this.translation, this.leftRotation, new Vector3f(1f, 1f, 1f), this.rightRotation));
		display.setItemStack(item);
		display.setBillboard(Billboard.FIXED);
		return display;
	}
}
