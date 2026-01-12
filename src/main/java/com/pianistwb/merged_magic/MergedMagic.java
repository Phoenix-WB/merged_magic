package com.pianistwb.merged_magic;

import com.mojang.logging.LogUtils;
import com.pianistwb.merged_magic.datacomponents.MergedMagicDataComponents;
import com.pianistwb.merged_magic.effect.MergedMagicMobEffects;
import com.pianistwb.merged_magic.items.MergedMagicCreativeTabs;
import com.pianistwb.merged_magic.items.MergedMagicItems;
import com.pianistwb.merged_magic.items.loot.MergedMagicLootModifiers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(MergedMagic.MODID)
public class MergedMagic {
	public static final String MODID = "merged_magic";
	public static org.slf4j.Logger LOG = LogUtils.getLogger();

	public MergedMagic(IEventBus modEventBus, ModContainer modContainer) {
		MergedMagicItems.ITEMS.register(modEventBus);
		MergedMagicMobEffects.MOB_EFFECTS.register(modEventBus);
		MergedMagicCreativeTabs.TABS.register(modEventBus);
		MergedMagicDataComponents.DATA_COMPONENTS.register(modEventBus);
		MergedMagicLootModifiers.LOOT_MODIFIERS.register(modEventBus);
	}
}
