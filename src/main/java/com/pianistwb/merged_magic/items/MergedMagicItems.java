package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.datacomponents.MergedMagicDataComponents;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MergedMagicItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MergedMagic.MODID);

	public static final DeferredItem<Item> BRONZE_STAR = ITEMS.registerItem("bronze_star", Item::new);
	
	public static final DeferredItem<Item> SILVER_STAR = ITEMS.registerItem("silver_star", Item::new);
	
	public static final DeferredItem<Item> GOLD_STAR = ITEMS.registerItem("gold_star", Item::new);

	public static final DeferredHolder<Item, MergableItem> BROOM = ITEMS.registerItem("broom",
			properties -> new MergableItem(
					properties.component(MergedMagicDataComponents.BASIC_MERGE_LEVEL, new MergeLevel(1))));
}
