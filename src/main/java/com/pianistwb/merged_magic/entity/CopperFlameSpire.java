package com.pianistwb.merged_magic.entity;

import com.pianistwb.merged_magic.client.ClientParticles;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;

import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class CopperFlameSpire extends Entity {
	public int despawnTicks = 300;

	public CopperFlameSpire(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	public CopperFlameSpire(Level level, Vec3 position, int despawnTicks) {
		super(EntityTypeRegistry.COPPER_FLAME_SPIRE.get(), level);
		this.setPos(position);
		this.despawnTicks = despawnTicks;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level().isClientSide()) {
			if (this.tickCount % 2 == 0) {
				ClientParticles.runCopperFlameSpireParticles(this.level(), this.position());
			}
		} else {
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()).forEach(entity -> {
            	entity.igniteForSeconds(8.0F);
            });
			
			if (--despawnTicks <= 0) {				
				this.discard();
			}
		}
	}
	

	@Override
	protected void defineSynchedData(Builder builder) {
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
		return false;
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
