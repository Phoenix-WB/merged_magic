package com.pianistwb.merged_magic.util;

import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MergeUtil {
	public static boolean itemLevelsValid(ItemStack stack1, ItemStack stack2) {
		int level1 = stack1.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();
		return level1 == stack2.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel() && level1 < 3;
	}

	public static void levelItem(ItemStack stack) {
		int mergelevel = stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();
		stack.update(DataComponentRegistry.MERGE_LEVEL, new MergeLevel(1), level -> new MergeLevel(mergelevel + 1));
	}

	public static void triggerLevelSound(Player player, ItemStack stack) {
		int level = stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();
		if (level == 3) {
			player.playSound(SoundEvents.TOTEM_USE, 0.15F, 1.3F);
		} else
			player.playSound(SoundEvents.ANVIL_USE, 0.7F, 1.2F);
	}
}
