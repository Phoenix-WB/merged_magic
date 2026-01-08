package com.pianistwb.merged_magic.items;

import net.minecraft.world.item.Item;

public class MergableItem extends Item {
	public MergableItem(Properties properties) {
		super(properties.stacksTo(1));
	}
}
