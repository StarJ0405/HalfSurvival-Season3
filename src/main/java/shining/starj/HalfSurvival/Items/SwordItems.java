package shining.starj.HalfSurvival.Items;

import shining.starj.HalfSurvival.Systems.AttributeModifiers;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SwordItems extends DurableItems {
	private final AttributeModifiers[] attributeModifiers;

	public SwordItems(String key, String displayName, Material material, String[] lores, int durability, double damage,
			double attackSpeed, double moveSpeed) {
		this(key, displayName, material, lores, 0, durability, damage, attackSpeed, moveSpeed);
	}

	public SwordItems(String key, String displayName, Material material, String[] lores, int model, int durability,
			double damage, double attackSpeed, double moveSpeed) {
		super(key, displayName, material, lores, model, durability);
		this.attributeModifiers = new AttributeModifiers[] {
				new AttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE, damage, Operation.ADD_NUMBER,
						EquipmentSlot.HAND),
				new AttributeModifiers(Attribute.GENERIC_ATTACK_SPEED, attackSpeed, Operation.ADD_NUMBER,
						EquipmentSlot.HAND),
				new AttributeModifiers(Attribute.GENERIC_MOVEMENT_SPEED, moveSpeed, Operation.ADD_SCALAR,
						EquipmentSlot.HAND) };
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack item = super.getItemStack();
		for (AttributeModifiers attributeModifiers : attributeModifiers)
			attributeModifiers.apply(item);
		return item;
	}
}
