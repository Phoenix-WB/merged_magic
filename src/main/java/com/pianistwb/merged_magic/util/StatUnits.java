package com.pianistwb.merged_magic.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StatUnits {
	public static final MutableComponent BLOCKS = register("blocks");
	public static final MutableComponent HEARTS = register("hearts");
	public static final MutableComponent BLOCKS_PER_TICK = register("blocks_per_tick");
	public static final MutableComponent SECONDS = register("seconds");
	public static final MutableComponent DURABILITY = register("durability");
	public static final MutableComponent DURABILITY_PER_SECOND = register("durability_per_second");
	public static final MutableComponent VELOCITY_PER_TICK = register("velocity_per_tick");

	private static MutableComponent register(String unit) {
		return Component.translatable("statUnit.merged_magic." + unit);
	}
}
