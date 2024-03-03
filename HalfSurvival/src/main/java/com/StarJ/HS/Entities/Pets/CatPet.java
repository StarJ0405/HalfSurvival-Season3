package com.StarJ.HS.Entities.Pets;

import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Cat.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.StarJ.HS.Systems.GUIs;
import com.StarJ.HS.Systems.HashMapStore;

import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowOwner;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSit;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;

public class CatPet extends Pets {
	public CatPet(String key, String customName) {
		super(key, customName);
	}

	@Override
	public LivingEntity spawnEntity(Location loc) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
		HashMapStore.setSpanwable(true);
		CustomEntity entity = new CustomEntity(world);
		world.addFreshEntity(entity, SpawnReason.CUSTOM);
		entity.a(loc.getX(), loc.getY(), loc.getZ());
		HashMapStore.setSpanwable(false);
		LivingEntity livingEntity = (LivingEntity) entity.getBukkitEntity();
		applyDefaultAttributes(livingEntity);
		return livingEntity;
	}

	public void setColor(LivingEntity livingEntity, DyeColor dyeColor, Type type) {
		Cat cat = (Cat) livingEntity;
		cat.setCollarColor(dyeColor);
		if (type != null)
			cat.setCatType(type);
	}

	private class CustomEntity extends EntityCat {
		public CustomEntity(World world) {
			super(EntityTypes.n, world);
		}

		@Override
		protected void B() {
			this.bO.a(2, (PathfinderGoal) new PathfinderGoalSit(this));
			// this.bO.a(5, (PathfinderGoal)new PathfinderGoalCatSitOnBed(this, 1.1D, 8));
			this.bO.a(6, (PathfinderGoal) new PathfinderGoalFollowOwner(this, 1.0D, 5.0F, 1.0F, false));
			// this.bO.a(7, (PathfinderGoal)new PathfinderGoalJumpOnBlock(this, 0.8D));
			this.bO.a(12,
					(PathfinderGoal) new PathfinderGoalLookAtPlayer((EntityInsentient) this, EntityHuman.class, 10.0F));
		}

		@Override
		public boolean a(DamageSource damagesource, float f) {
			if (damagesource.j().a().equalsIgnoreCase("genericKill"))
				return super.a(damagesource, f);
			return false;
		}

		@Override
		public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
			UUID uuid = d();
			if (uuid != null && entityhuman != null && uuid.equals(entityhuman.cw())) {
				Player player = (Player) entityhuman.getBukkitEntity();
				if (player.isSneaking()) {
					z(!ge());
				} else
					GUIs.myPetInventory.openInv(player);
			}
			return EnumInteractionResult.a;
		}
	}

}
