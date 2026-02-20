package com.pianistwb.merged_magic.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface ClientMobEffect {
	public void clientTick(Level level, LivingEntity entity, int amplifier);
}
