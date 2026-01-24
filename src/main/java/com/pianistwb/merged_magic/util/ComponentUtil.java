package com.pianistwb.merged_magic.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ComponentUtil {
	public static DecimalFormat decFormat;

	static {
		decFormat = new DecimalFormat("#.##");
		decFormat.setRoundingMode(RoundingMode.CEILING);
	}

	public static MutableComponent literal(int val) {
		return Component.literal(String.valueOf(val));
	}
	
	public static MutableComponent literal(float val) {
		return Component.literal(decFormat.format(val));
	}

	public static MutableComponent literal(double val) {
		return Component.literal(decFormat.format(val));
	}
}
