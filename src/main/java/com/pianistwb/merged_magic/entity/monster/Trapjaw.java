package com.pianistwb.merged_magic.entity.monster;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import com.pianistwb.merged_magic.registry.DamageSources;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Trapjaw extends Monster implements GeoEntity {
	private static final EntityDataAccessor<Byte> GRABBING_STATE_ID = SynchedEntityData.defineId(Trapjaw.class,
			EntityDataSerializers.BYTE);

	private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.trapjaw.idle");
	private static final RawAnimation GRAB_ANIM = RawAnimation.begin().thenPlayAndHold("animation.trapjaw.grab");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public Entity owner = null;
	public boolean despawn = false;
	public int despawnTicks = 0;
	public static final float BITE_DISTANCE = 6.0F;

	public Trapjaw(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new TrapjawAttackGoal());
		this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, BITE_DISTANCE));
		this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Mob.class, BITE_DISTANCE));
		this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new TrapjawHurtByTargetGoal());
		this.targetSelector.addGoal(2, new TrapjawNearestAttackableTargetGoal<>(Player.class, true));
		this.targetSelector.addGoal(3, new TrapjawNearestAttackableTargetGoal<>(Mob.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.0D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>("Actions", state -> {
			if (this.getGrabbingState() != 0) {
				return state.setAndContinue(GRAB_ANIM);
			}

			return state.setAndContinue(IDLE_ANIM);
		}));
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide() && despawn && --despawnTicks <= 0) {
			this.kill((ServerLevel) this.level());
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entity) {
		float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		DamageSource damagesource = DamageSources.trapJaw(this);
		entity.hurtServer(level, damagesource, f);
		this.playAttackSound();

		return false;
	}

	@Override
	public void knockback(double strength, double x, double z) {
	}

	@Override
	public void die(DamageSource damageSource) {
		if (this.getTarget() != null && this.getTarget().isAlive()) {
			this.getTarget().removeEffect(MobEffectRegistry.TRAPJAWED);
		}

		super.die(damageSource);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	public void setOwner(Entity living) {
		this.owner = living;
	}

	public Entity getOwner() {
		return this.owner;
	}
	
	@Override
	public void setTarget(@Nullable LivingEntity target) {
		LivingEntity oldTarget = this.getTarget();
		if (oldTarget != null && oldTarget.isAlive()) {
			oldTarget.removeEffect(MobEffectRegistry.TRAPJAWED);
		}
		
		super.setTarget(target);
	}

	public void resetGrabbing() {
		this.setGrabbingState((byte) 0);
	}

	public void setGrabbing() {
		this.setGrabbingState((byte) 1);
	}

	public void setGrabbingState(byte grabbingState) {
		this.entityData.set(GRABBING_STATE_ID, grabbingState);
	}

	public byte getGrabbingState() {
		return this.entityData.get(GRABBING_STATE_ID);
	}

	@Override
	protected void playAttackSound() {
		this.playSound(SoundEvents.PHANTOM_BITE);
	}

	@Override
	protected void defineSynchedData(Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GRABBING_STATE_ID, (byte) 0);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput compound) {
		super.addAdditionalSaveData(compound);
		compound.putByte("grabbing", this.getGrabbingState());
		compound.putBoolean("despawn", despawn);
		compound.putInt("despawnTicks", despawnTicks);

		if (owner != null) {
			compound.store("ownerUUID", UUIDUtil.CODEC, owner.getUUID());
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput compound) {
		super.readAdditionalSaveData(compound);
		this.setGrabbingState(compound.getByteOr("grabbing", (byte) 0));
		this.despawn = compound.getBooleanOr("despawn", false);
		this.despawnTicks = compound.getIntOr("despawnTicks", 0);

		Optional<UUID> ownerUUID = compound.read("ownerUUID", UUIDUtil.CODEC);
		if (ownerUUID.isPresent()) {
			Entity living = this.level().getEntity(ownerUUID.get());
			this.setOwner(living);
		}
	}

	class TrapjawAttackGoal extends Goal {
		private int tick = 0;

		public TrapjawAttackGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			LivingEntity livingentity = Trapjaw.this.getTarget();
			return livingentity != null && livingentity.isAlive()
					&& Trapjaw.this.distanceToSqr(livingentity) <= BITE_DISTANCE;
		}

		@Override
		public void start() {
			Trapjaw.this.setGrabbing();
			this.getTarget().addEffect(new MobEffectInstance(MobEffectRegistry.TRAPJAWED, 200, 0, false, false));
			tick = 0;
		}

		@Override
		public void stop() {
			Trapjaw.this.resetGrabbing();
		}

		@Override
		public void tick() {
			LivingEntity target = this.getTarget();
			target.addEffect(new MobEffectInstance(MobEffectRegistry.TRAPJAWED, 200, 0, false, false));

			if (tick % 30 == 0) {
				Trapjaw.this.doHurtTarget((ServerLevel) Trapjaw.this.level(), target);
			}

			tick++;
		}

		private LivingEntity getTarget() {
			return Trapjaw.this.getTarget();
		}
	}

	class TrapjawNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
		public TrapjawNearestAttackableTargetGoal(Class<T> targetType, boolean mustSee) {
			super(Trapjaw.this, targetType, mustSee);
		}

		@Override
		protected void findTarget() {
			ServerLevel serverlevel = getServerLevel(this.mob);
			TargetingConditions conditions = this.targetConditions.range(this.getFollowDistance())
					.selector((living, level) -> !(living instanceof Trapjaw) && !living.is(Trapjaw.this.getOwner()));

			if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
				this.target = serverlevel.getNearestEntity(
						this.mob.level().getEntitiesOfClass(this.targetType,
								this.getTargetSearchArea(this.getFollowDistance()), p_148152_ -> true),
						conditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
			} else {
				this.target = serverlevel.getNearestPlayer(conditions, this.mob, this.mob.getX(), this.mob.getEyeY(),
						this.mob.getZ());
			}
		}
	}
	
	class TrapjawHurtByTargetGoal extends HurtByTargetGoal {
		public TrapjawHurtByTargetGoal(Class<?>... toIgnoreDamage) {
			super(Trapjaw.this, toIgnoreDamage);
		}
		
	    @Override
	    public boolean canUse() {
	        LivingEntity livingEntity = this.mob.getLastHurtByMob();
	        Entity owner = Trapjaw.this.getOwner();
	        
	        return (owner == null || livingEntity != owner) && super.canUse();
	    }
	}
}
