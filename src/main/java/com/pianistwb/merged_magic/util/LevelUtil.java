package com.pianistwb.merged_magic.util;

import java.util.Collections;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class LevelUtil {
	@SuppressWarnings("deprecation")
	public static Iterable<BlockPos> getBlockPosIfLoaded(Level level, AABB aabb) {
		int i = Mth.floor(aabb.minX);
		int j = Mth.floor(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.floor(aabb.maxY);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.floor(aabb.maxZ);
		return level.hasChunksAt(i, k, i1, j, l, j1) ? BlockPos.betweenClosed(aabb) : Collections.emptyList();
	}
}
