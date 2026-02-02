package com.pianistwb.merged_magic.util;

import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MagicUtil {
	public static AABB getSquareRadius(BlockPos centrePos, double radius) {
		return new AABB(centrePos.getX() - radius, centrePos.getY() - radius, centrePos.getZ() - radius,
				centrePos.getX() + 1 + radius, centrePos.getY() + 1 + radius, centrePos.getZ() + 1 + radius);
	}

	public static @Nullable Entity getTarget(LivingEntity origin, Predicate<Entity> filter, double scale) {
		HitResult result = ProjectileUtil.getHitResultOnViewVector(origin, filter, scale);
		if(result.getType() == HitResult.Type.ENTITY) {
			return ((EntityHitResult) result).getEntity();
		}
		
		return null;
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

	public static Vec3 getRelativeToEntityView(Entity entity, double scaling, double displacement) {
		if (scaling == 0D) {
			return Vec3.ZERO;
		}

		Vec3 viewVector = entity.getViewVector(1.0F);
		double d0 = Math.sqrt(viewVector.x() * viewVector.x() + viewVector.z() * viewVector.z());

		Vec3 spawnRelative;
		if (d0 < 1.0E-5F) {
			spawnRelative = Vec3.ZERO;
		} else {
			spawnRelative = new Vec3(scaling * viewVector.x() / d0, displacement, scaling * viewVector.z() / d0);
		}

		return spawnRelative;
	}

	public static Vec3 rotateVector(Vec3 vector, double rotation) {
		return new Vec3(vector.x() * Mth.cos(rotation) - vector.z() * Mth.sin(rotation), vector.y(),
				vector.x() * Mth.sin(rotation) + vector.z() * Mth.cos(rotation));
	}
}
