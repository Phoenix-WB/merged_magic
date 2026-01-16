package com.pianistwb.merged_magic.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MagicUtil {
	public static AABB getSquareRadius(BlockPos centrePos, int radius) {
		return new AABB(centrePos.getX() - radius, centrePos.getY() - radius, centrePos.getZ() - radius,
				centrePos.getX() + 1 + radius, centrePos.getY() + 1 + radius, centrePos.getZ() + 1 + radius);
	}

	public static boolean livingCentreInLine(LivingEntity origin, LivingEntity target, ClipContext.Block block,
			ClipContext.Fluid fluid) {
		float originCentre = origin.getDimensions(origin.getPose()).height() * 0.75F;
		Vec3 vec3 = new Vec3(origin.getX(), origin.getY() + originCentre, origin.getZ());

		float targetCentre = target.getDimensions(target.getPose()).height() * 0.75F;
		Vec3 vec31 = new Vec3(target.getX(), target.getY() + targetCentre, target.getZ());

		return vec31.distanceTo(vec3) > 128.0 ? false
				: origin.level().clip(new ClipContext(vec3, vec31, block, fluid, target))
						.getType() == HitResult.Type.MISS;
	}
}
