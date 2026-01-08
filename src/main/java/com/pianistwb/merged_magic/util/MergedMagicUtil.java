package com.pianistwb.merged_magic.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MergedMagicUtil {
	public static boolean itemLevelsValid(ItemStack stack1, ItemStack stack2) {
		return true;
	}
	
	public static ItemStack levelItem(ItemStack stack) {
		return Items.BREAD.getDefaultInstance();
	}
	
	public static void triggerLevelSound(Player player, ItemStack stack) {
		player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.2F);
	}
}
