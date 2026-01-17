package com.pianistwb.merged_magic.util;

import net.minecraft.world.item.ItemStack;

public class ItemUtil {
	public static boolean isItemHealthy(ItemStack stack) {
		return getItemHealth(stack) > 0;
	}

	public static int getItemHealth(ItemStack stack) {
		return stack.getMaxDamage() - stack.getDamageValue();
	}

	public static void damageStack(ItemStack stack, int amount) {
		stack.setDamageValue(Math.min(stack.getDamageValue() + amount, stack.getMaxDamage()));
	}

	public static void healStack(ItemStack stack, int amount) {
		stack.setDamageValue(Math.max(stack.getDamageValue() - amount, 0));
	}
}
