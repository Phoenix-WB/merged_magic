package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MergedMagicItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MergedMagic.MODID);

	public static final DeferredHolder<Item, MergableItem> BROOM = ITEMS.registerItem("broom", MergableItem::new);
}
