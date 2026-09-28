package com.pianistwb.merged_magic.entity.monster;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager.ControllerRegistrar;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
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
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Trapjaw extends Monster implements GeoEntity {
	private static final EntityDataAccessor<Byte> GRABBING_STATE_ID = SynchedEntityData.defineId(Trapjaw.class,
			EntityDataSerializers.BYTE);

	private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.trapjaw.idle");
	private static final RawAnimation GRABBING_ANIM = RawAnimation.begin()
			.thenPlayAndHold("animation.trapjaw.grabbing");
	private static final RawAnimation UNGRABBING_ANIM = RawAnimation.begin()
			.thenPlayAndHold("animation.trapjaw.ungrabbing");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public Entity owner = null;
	public boolean villagerOwner = false;

	public boolean despawn = false;
	public int despawnTicks = 0;
	public static final float BITE_DISTANCE = 2.236F;
	public int tickUngrabbing = 0;

	public Trapjaw(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new TrapjawAttackGoal());
		this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, BITE_DISTANCE * 3.0F));
		this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Mob.class, BITE_DISTANCE * 3.0F));
		this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new TrapjawHurtByTargetGoal());
		this.targetSelector.addGoal(2, new TrapjawNearestAttackableTargetGoal<>(Player.class, true));
		this.targetSelector.addGoal(2, new TrapjawNearestAttackableTargetGoal<>(Mob.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.FOLLOW_RANGE, BITE_DISTANCE);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide()) {
			if (despawn && --despawnTicks <= 0) {
				this.kill((ServerLevel) this.level());
			}
		}

		if (this.getGrabbingState() == 2 && tickUngrabbing++ >= 5) {
			this.resetGrabbing();
			this.tickUngrabbing = 0;
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
	public void knockback(double power, double xd, double zd, DamageSource source, float damage) {
	}

	@Override
	public void die(DamageSource damageSource) {
		if (this.getTarget() != null && this.getTarget().isAlive()) {
			this.getTarget().removeEffect(MobEffectRegistry.TRAPJAWED);
		}

		super.die(damageSource);
	}

	@Override
	public boolean shouldDropExperience() {
		return this.getOwner() == null;
	}

	@Override
	public boolean canBeCollidedWith(@Nullable Entity entity) {
		return this.isAlive();
	}

	public void setOwner(Entity living) {
		this.owner = living;
	}

	public Entity getOwner() {
		return this.owner;
	}

	public boolean hasVillagerOwner() {
		return this.villagerOwner;
	}

	public void withVillagerOwner() {
		this.villagerOwner = true;
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

	public void setUngrabbing() {
		this.setGrabbingState((byte) 2);
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
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>("Actions", state -> {
			if (this.getGrabbingState() == 1) {
				return state.setAndContinue(GRABBING_ANIM);
			} else if (this.getGrabbingState() == 2) {
				return state.setAndContinue(UNGRABBING_ANIM);
			}

			return state.setAndContinue(IDLE_ANIM);
		}));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput input) {
		super.addAdditionalSaveData(input);
		input.putByte("grabbing", this.getGrabbingState());
		input.putBoolean("despawn", despawn);
		input.putInt("despawnTicks", despawnTicks);
		input.putBoolean("villagerOwner", villagerOwner);

		if (owner != null) {
			input.store("ownerUUID", UUIDUtil.CODEC, owner.getUUID());
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput output) {
		super.readAdditionalSaveData(output);
		this.setGrabbingState(output.getByteOr("grabbing", (byte) 0));
		this.despawn = output.getBooleanOr("despawn", false);
		this.despawnTicks = output.getIntOr("despawnTicks", 0);
		this.villagerOwner = output.getBooleanOr("villagerOwner", false);

		Optional<UUID> ownerUUID = output.read("ownerUUID", UUIDUtil.CODEC);
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
			return Trapjaw.this.getGrabbingState() != 2 && livingentity != null && livingentity.isAlive()
					&& Trapjaw.this.distanceToSqr(livingentity) <= BITE_DISTANCE * BITE_DISTANCE;
		}

		@Override
		public void start() {
			Trapjaw.this.setGrabbing();
			this.getTarget().addEffect(new MobEffectInstance(MobEffectRegistry.TRAPJAWED, 10, 0, false, false));
			tick = 0;
		}

		@Override
		public void stop() {
			Trapjaw.this.setUngrabbing();
		}

		@Override
		public void tick() {
			LivingEntity target = this.getTarget();
			target.addEffect(new MobEffectInstance(MobEffectRegistry.TRAPJAWED, 10, 0, false, false));

			if (tick++ % 30 == 0) {
				Trapjaw.this.doHurtTarget((ServerLevel) Trapjaw.this.level(), target);
			}

			Trapjaw.this.getLookControl().setLookAt(target.getX(), Trapjaw.this.getEyeY(), target.getZ());
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
		public boolean canUse() {
			return super.canUse() && this.villagerOwnerCheck();
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

		private boolean villagerOwnerCheck() {
			if (Trapjaw.this.hasVillagerOwner() && this.target instanceof Villager villager) {
				this.target = null;
				return false;
			}
			return true;
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
