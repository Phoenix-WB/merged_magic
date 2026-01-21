package com.pianistwb.merged_magic.util;

import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemUtil {
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

	public static Item.Properties durableItem(Item.Properties properties, int maxDamage) {
		properties.component(DataComponents.MAX_DAMAGE, maxDamage);
		properties.component(DataComponents.DAMAGE, 0);
		return properties;
	}

	public static Item.Properties brokenItem(Item.Properties properties, int maxDamage) {
		properties.component(DataComponents.MAX_DAMAGE, maxDamage);
		properties.component(DataComponents.DAMAGE, maxDamage);
		return properties;
	}
}
