package shining.starj.HalfSurvival.Entities.Pets;

import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowOwner;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSit;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import shining.starj.HalfSurvival.Systems.GUIs;
import shining.starj.HalfSurvival.Systems.HashMapStore;

import java.util.Objects;
import java.util.UUID;

public class DogPet extends Pets {
	public DogPet(String key, String customName) {
		super(key, customName);
	}

	@Override
	public LivingEntity spawnEntity(Location loc) {
		World world = ((CraftWorld) Objects.requireNonNull(loc.getWorld())).getHandle();
		HashMapStore.setSpanwable(true);
		CustomEntity entity = new CustomEntity(world);
		world.addFreshEntity(entity, SpawnReason.CUSTOM);
		entity.a(loc.getX(), loc.getY(), loc.getZ());
		HashMapStore.setSpanwable(false);
		LivingEntity livingEntity = (LivingEntity) entity.getBukkitEntity();
		applyDefaultAttributes(livingEntity);
		return livingEntity;
	}

	public void setColor(LivingEntity livingEntity, DyeColor dyeColor) {
		Wolf wolf = (Wolf) livingEntity;
		wolf.setCollarColor(dyeColor);
	}

	private static class CustomEntity extends EntityWolf {
		public CustomEntity(World world) {
			super(EntityTypes.bp, world);
		}

		@Override
		protected void B() {
			this.bO.a(2, new PathfinderGoalSit(this));
			this.bO.a(6,  new PathfinderGoalFollowOwner(this, 1.0D, 5.0F, 1.0F, false));
			this.bO.a(10,
					 new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
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
