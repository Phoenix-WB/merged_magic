package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.items.AmethystMirrorItem;
import com.pianistwb.merged_magic.items.BroomItem;
import com.pianistwb.merged_magic.items.DolphinsBreathItem;
import com.pianistwb.merged_magic.items.JungleVineItem;
import com.pianistwb.merged_magic.items.ProspectorsPanItem;
import com.pianistwb.merged_magic.items.SpringyPistonItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MergedMagic.MODID);

	// Items for texture purposes
	public static final DeferredItem<Item> BRONZE_STAR = ITEMS.registerItem("bronze_star", Item::new);

	public static final DeferredItem<Item> SILVER_STAR = ITEMS.registerItem("silver_star", Item::new);

	public static final DeferredItem<Item> GOLD_STAR = ITEMS.registerItem("gold_star", Item::new);

	// Actual items
	public static final DeferredHolder<Item, BroomItem> BROOM = ITEMS.registerItem("broom", BroomItem::new);

	public static final DeferredHolder<Item, DolphinsBreathItem> DOLPHINS_BREATH = ITEMS.registerItem("dolphins_breath",
			DolphinsBreathItem::new);

	public static final DeferredHolder<Item, SpringyPistonItem> SPRINGY_PISTON = ITEMS.registerItem("springy_piston",
			SpringyPistonItem::new);

	public static final DeferredHolder<Item, AmethystMirrorItem> AMETHYST_MIRROR = ITEMS.registerItem("amethyst_mirror",
			AmethystMirrorItem::new);

	public static final DeferredHolder<Item, ProspectorsPanItem> PROSPECTORS_PAN = ITEMS.registerItem("prospectors_pan",
			ProspectorsPanItem::new);

	public static final DeferredHolder<Item, JungleVineItem> JUNGLE_VINE = ITEMS.registerItem("jungle_vine",
			properties -> new JungleVineItem(properties.durability(32)));
}
