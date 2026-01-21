package com.pianistwb.merged_magic.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class CocoaBuffMobEffect extends MobEffect {
	public CocoaBuffMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		if(entity.isFreezing()) {
			entity.setTicksFrozen(0);
		}
		
		entity.removeEffect(MobEffects.SLOWNESS);
		
		return true;
	}
}
