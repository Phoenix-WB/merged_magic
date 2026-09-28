package com.pianistwb.merged_magic.entity.projectile;

import com.pianistwb.merged_magic.entity.CopperFlameSpire;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CopperFlame extends ThrowableProjectile {
	public int despawnTicks = 300;

	public CopperFlame(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
	}

	public CopperFlame(Level level, Entity entity, int despawnTicks) {
		this(EntityTypeRegistry.COPPER_FLAME.get(), level);
		this.setOwner(entity);
		this.setPos(entity.getX(), entity.getEyeY() - 0.1D, entity.getZ());
		this.despawnTicks = despawnTicks;
	}

	@Override
	public void tick() {
		super.tick();

		Level level = this.level();
		Vec3 pos = this.position();
		if (level.isClientSide()) {
			level.addParticle(ParticleTypes.COPPER_FIRE_FLAME, pos.x(), pos.y() + this.getBbHeight() / 2.0F, pos.z(), 0,
					-0.05, 0);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.discard();
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide()) {
			BlockPos hitPos = result.getBlockPos().relative(result.getDirection());
			CopperFlameSpire spire = new CopperFlameSpire(this.level(), Vec3.atBottomCenterOf(hitPos), despawnTicks);
			this.level().addFreshEntity(spire);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		if (!this.level().isClientSide()) {
			CopperFlameSpire spire = new CopperFlameSpire(this.level(), entity.position(), despawnTicks);
			this.level().addFreshEntity(spire);
		}
	}

	@Override
	protected void defineSynchedData(Builder builder) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput input) {
		input.putInt("despawnTicks", despawnTicks);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput output) {
		this.despawnTicks = output.getIntOr("despawnTicks", 300);
	}
}
