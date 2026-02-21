package com.pianistwb.merged_magic.effect;

import com.pianistwb.merged_magic.client.ClientParticles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SheathingMobEffect extends MobEffect implements ClientMobEffect {
	public SheathingMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void clientTick(Level level, LivingEntity entity, int amplifier) {
		if (entity.tickCount % 6 == 0) {
			ClientParticles.runRandomParticlesWithinBlockEdges(ParticleTypes.CRIT, level, entity.getEyePosition(), 6, 0,
					0, 0);
		}
	}
}
