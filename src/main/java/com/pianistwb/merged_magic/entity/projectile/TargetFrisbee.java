package com.pianistwb.merged_magic.entity.projectile;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager.ControllerRegistrar;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;

import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class TargetFrisbee extends ThrowableProjectile implements GeoEntity {
	private static final RawAnimation SPINNING_ANIM = RawAnimation.begin()
			.thenLoop("animation.target_frisbee.spinning");
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	
	private int duration = 200;

	public TargetFrisbee(EntityType<? extends ThrowableProjectile> entityType, Level level) {
		super(entityType, level);
	}
	
	public TargetFrisbee(Level level, Entity entity, int duration) {
		super(EntityTypeRegistry.TARGET_FRISBEE.get(), level);
		this.setOwner(entity);
		this.setPos(entity.getX(), entity.getEyeY() - 0.1D, entity.getZ());
		this.duration = duration;
	}
	
	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.discard();
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity entity = result.getEntity();
		
		if(!entity.level().isClientSide() && entity instanceof LivingEntity living) {
			living.addEffect(new MobEffectInstance(MobEffectRegistry.TARGETED, duration));
			living.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration));
		}		
	}

	@Override
	protected void defineSynchedData(Builder builder) {
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>("Spinning", state -> {
			return state.setAndContinue(SPINNING_ANIM);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}
	
	@Override
	protected void addAdditionalSaveData(ValueOutput input) {
		super.addAdditionalSaveData(input);
		input.putInt("duration", duration);
	}
	
	@Override
	protected void readAdditionalSaveData(ValueInput output) {
		super.readAdditionalSaveData(output);
		this.duration = output.getIntOr("duration", 200);
	}
}
