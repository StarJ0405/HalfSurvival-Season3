package shining.starj.HalfSurvival.Entities;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import shining.starj.HalfSurvival.Items.Items;
import shining.starj.HalfSurvival.Systems.AttributeModifiers;
import shining.starj.HalfSurvival.Systems.Attributes;
import shining.starj.HalfSurvival.Systems.HashMapStore;

public class SkeletonArcher extends Entities {

	public SkeletonArcher(String key, String customName) {
		super(key, customName, new Attributes[] { new Attributes(Attribute.GENERIC_MAX_HEALTH, 200) },
				new AttributeModifiers[0]);
	}

	@Override
	public LivingEntity spawnEntity(Location loc) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
		HashMapStore.setSpanwable(true);
		CustomEntity entity = new CustomEntity(world);
		world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
		entity.a(loc.getX(), loc.getY(), loc.getZ());
		HashMapStore.setSpanwable(false);
		LivingEntity livingEntity = (LivingEntity) entity.getBukkitEntity();
		applyDefaultAttributes(livingEntity);
		EntityEquipment equip = livingEntity.getEquipment();
		equip.setItemInMainHand(Items.wooden_bow.getItemStack());
		equip.setItemInMainHandDropChance(0f);
		equip.setHelmet(new ItemStack(Material.ACACIA_BUTTON));
		equip.setHelmetDropChance(0f);
		return livingEntity;
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
	private class CustomEntity extends EntitySkeleton {
		public CustomEntity(World world) {
			super(EntityTypes.aK, world);
		}

		@Override
		protected void gg() {
		}

		@Override
		protected void B() {
//			this.bO.a(5, (PathfinderGoal) new PathfinderGoalRandomStrollLand(this, 1.0D));
//			this.bO.a(6,
//					(PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, EntityHuman.class, 8.0F));
//			this.bO.a(6, (PathfinderGoal) new PathfinderGoalRandomLookaround((EntityInsentient) this));
//			this.bP.a(1, (PathfinderGoal) new PathfinderGoalHurtByTarget(this, new Class[0]));
//			this.bP.a(2, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityInsentient) this,
//					EntityHuman.class, true));
		}
	}
}
