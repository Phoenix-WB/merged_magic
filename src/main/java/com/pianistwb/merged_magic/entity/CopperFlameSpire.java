package com.pianistwb.merged_magic.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pianistwb.merged_magic.client.ClientParticles;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
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

		RandomSource random = this.level().getRandom();
		if (random.nextInt(24) == 0) {
			this.level().playLocalSound(this.blockPosition().getX() + 0.5, this.blockPosition().getY() + 0.5,
					this.blockPosition().getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.AMBIENT,
					1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}

		if (this.level().isClientSide()) {
			if (this.tickCount % 2 == 0) {
				ClientParticles.runRandomParticlesWithinBlock(ParticleTypes.COPPER_FIRE_FLAME, this.level(),
						this.position());
				ClientParticles.runRandomParticlesWithinBlock(ParticleTypes.COPPER_FIRE_FLAME, this.level(),
						this.position().add(0, 1, 0));
				ClientParticles.runRandomParticlesWithinBlock(ParticleTypes.COPPER_FIRE_FLAME, this.level(),
						this.position().add(0, 2, 0));
			}
		} else {
			if (this.tickCount != 0 && this.tickCount % 30 == 0) {
				this.getSurroundingBlocks().forEach(pos -> {
					if (random.nextFloat() <= 0.33F) {
						BlockState state = this.level().getBlockState(pos);
						Optional<BlockState> nextState = WeatheringCopper.getNext(state.getBlock())
								.map(oldBlock -> oldBlock.withPropertiesOf(state));
						nextState.ifPresent(update -> this.level().setBlockAndUpdate(pos, update));
					}
				});
			}

			this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()).forEach(entity -> {
				entity.igniteForSeconds(8.0F);
			});

			if (--despawnTicks <= 0) {
				this.discard();
			}
		}
	}

	public List<BlockPos> getSurroundingBlocks() {
		List<BlockPos> posList = new ArrayList<>();
		BlockPos origin = this.blockPosition();
		posList.add(origin.below());
		posList.add(origin.above(3));
		surroundingIncluding(posList, origin);
		surroundingIncluding(posList, origin.above());
		surroundingIncluding(posList, origin.above(2));

		return posList;
	}

	private static void surroundingIncluding(List<BlockPos> posList, BlockPos pos) {
		posList.add(pos);
		posList.add(pos.relative(Direction.NORTH));
		posList.add(pos.relative(Direction.EAST));
		posList.add(pos.relative(Direction.SOUTH));
		posList.add(pos.relative(Direction.WEST));
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
