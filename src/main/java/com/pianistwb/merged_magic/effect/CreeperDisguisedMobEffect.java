package com.pianistwb.merged_magic.effect;

import com.pianistwb.merged_magic.util.LevelUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CreeperDisguisedMobEffect extends MobEffect {
	public CreeperDisguisedMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		if (entity.tickCount % 20 == 0) {
			AABB box = entity.getHitbox().inflate(1.0D);
			for (BlockPos pos : LevelUtil.getBlockPosIfLoaded(level, box)) {
				BlockState state = level.getBlockState(pos);
				if (state.is(BlockTags.LEAVES)) {
					entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 21, 0, false, false));
					break;
				}
			}
		}

		return true;
	}
}
