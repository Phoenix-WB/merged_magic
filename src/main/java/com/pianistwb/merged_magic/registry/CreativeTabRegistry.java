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
					.withTabsBefore(CreativeModeTabs.COMBAT)
					.icon(() -> ItemRegistry.BROOM.get().getDefaultInstance())
					.displayItems((parameters, output) -> {
						//Mergable Items
						output.accept(ItemRegistry.BROOM.get());
						output.accept(ItemRegistry.DOLPHINS_BREATH.get());
						output.accept(ItemRegistry.SPRINGY_PISTON.get());
						output.accept(ItemRegistry.AMETHYST_MIRROR.get());
						output.accept(ItemRegistry.PROSPECTORS_PAN.get());
						output.accept(ItemRegistry.JUNGLE_VINE.get());
						output.accept(ItemRegistry.HOT_COCOA_ITEM.get());
						output.accept(ItemRegistry.TARGET_FRISBEE_ITEM.get());
						output.accept(ItemRegistry.HERMES_SHOES_ITEM.get());
						
						//Normal Items
					}).build());
}
