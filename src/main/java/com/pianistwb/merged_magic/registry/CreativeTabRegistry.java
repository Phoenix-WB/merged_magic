package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
			MergedMagic.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MERGED_MAGIC_ITEMS_TAB = TABS.register(
			"merged_magic_items",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.merged_magic_items"))
					.withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> ItemRegistry.BROOM.get().getDefaultInstance())
					.displayItems((parameters, output) -> {
						// Mergable Items
						output.accept(ItemRegistry.BROOM.get());
						output.accept(ItemRegistry.DOLPHINS_BREATH.get());
						output.accept(ItemRegistry.SPRINGY_PISTON.get());
						output.accept(ItemRegistry.AMETHYST_MIRROR.get());
						output.accept(ItemRegistry.PROSPECTORS_PAN.get());
						output.accept(ItemRegistry.EVERGROWING_VINE.get());
						output.accept(ItemRegistry.HOT_COCOA.get());
						output.accept(ItemRegistry.TARGET_FRISBEE.get());
						output.accept(ItemRegistry.HERMES_SHOES.get());
						output.accept(ItemRegistry.TRAPSEED_SACK.get());
						output.accept(ItemRegistry.FLOATY.get());
						output.accept(ItemRegistry.TOTEM_OF_ENDURING.get());
						output.accept(ItemRegistry.CURSED_PAINTING.get());
						output.accept(ItemRegistry.CREEPER_DISGUISE.get());
						output.accept(ItemRegistry.SWORD_OF_EDEN.get());

						// Normal Items
						output.accept(ItemRegistry.CHOCOLATE_MILK_BUCKET.get());
						output.accept(ItemRegistry.AIR_BUCKET.get());
						output.accept(ItemRegistry.HERMES_PARCEL.get());
						output.accept(ItemRegistry.BOTANY_MEAL.get());

						// Spawn Eggs
						output.accept(ItemRegistry.TRAPJAW_SPAWN_EGG.get());
					}).build());
}
