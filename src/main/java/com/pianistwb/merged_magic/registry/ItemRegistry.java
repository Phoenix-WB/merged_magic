package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.items.AmethystMirrorItem;
import com.pianistwb.merged_magic.items.BroomItem;
import com.pianistwb.merged_magic.items.DolphinsBreathItem;
import com.pianistwb.merged_magic.items.HermesShoesItem;
import com.pianistwb.merged_magic.items.HotCocoaItem;
import com.pianistwb.merged_magic.items.JungleVineItem;
import com.pianistwb.merged_magic.items.ProspectorsPanItem;
import com.pianistwb.merged_magic.items.SpringyPistonItem;
import com.pianistwb.merged_magic.items.TargetFrisbeeItem;
import com.pianistwb.merged_magic.items.TrapseedSackItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MergedMagic.MODID);

	// Items for texture purposes
	public static final DeferredItem<Item> BRONZE_STAR = ITEMS.registerItem("bronze_star", Item::new);

	public static final DeferredItem<Item> SILVER_STAR = ITEMS.registerItem("silver_star", Item::new);

	public static final DeferredItem<Item> GOLD_STAR = ITEMS.registerItem("gold_star", Item::new);

	// Mergable items
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
			JungleVineItem::new);

	public static final DeferredHolder<Item, HotCocoaItem> HOT_COCOA = ITEMS.registerItem("hot_cocoa",
			HotCocoaItem::new);

	public static final DeferredHolder<Item, TargetFrisbeeItem> TARGET_FRISBEE = ITEMS.registerItem("target_frisbee",
			TargetFrisbeeItem::new);

	public static final DeferredHolder<Item, HermesShoesItem> HERMES_SHOES = ITEMS.registerItem("hermes_shoes",
			HermesShoesItem::new);

	public static final DeferredHolder<Item, TrapseedSackItem> TRAPSEED_SACK = ITEMS.registerItem("trapseed_sack",
			TrapseedSackItem::new);

	// Normal Items

	public static final DeferredHolder<Item, Item> CHOCOLATE_MILK_BUCKET = ITEMS.registerItem("chocolate_milk_bucket",
			properties -> new Item(properties.craftRemainder(Items.BUCKET)
					.component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).usingConvertsTo(Items.BUCKET)
					.stacksTo(1)));

	// Spawn Eggs
	public static final DeferredItem<SpawnEggItem> TRAPJAW_SPAWN_EGG = ITEMS.registerItem("trapjaw_spawn_egg",
			properties -> new SpawnEggItem(properties.spawnEgg(EntityTypeRegistry.TRAPJAW.get())));
}
