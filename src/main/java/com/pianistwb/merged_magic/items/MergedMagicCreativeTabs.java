package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MergedMagicCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
			MergedMagic.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MERGED_MAGIC_ITEMS_TAB = TABS.register(
			"merged_magic_items",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.merged_magic_items"))
					.withTabsBefore(CreativeModeTabs.COMBAT)
					.icon(() -> MergedMagicItems.BROOM.get().getDefaultInstance())
					.displayItems((parameters, output) -> {
						output.accept(MergedMagicItems.BROOM.get());
						output.accept(MergedMagicItems.DOLPHINS_BREATH.get());
					}).build());
}
