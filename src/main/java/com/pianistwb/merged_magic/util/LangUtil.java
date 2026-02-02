package com.pianistwb.merged_magic.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LangUtil {
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
	
	public static MutableComponent withMultiply(MutableComponent input) {
		return Component.translatable("langUtil.merged_magic.with_multiply", input);
	}
}
