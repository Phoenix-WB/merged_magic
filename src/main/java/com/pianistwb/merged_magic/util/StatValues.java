package com.pianistwb.merged_magic.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StatValues {
	public static final MutableComponent IN_FRONT = register("in_front");
	public static final MutableComponent TRIANGLE = register("triangle");
	public static final MutableComponent PENTAGON = register("pentagon");

	private static MutableComponent register(String value) {
		return Component.translatable("statValue.merged_magic." + value);
	}
}
