package com.pianistwb.merged_magic;

import com.mojang.logging.LogUtils;
import com.pianistwb.merged_magic.registry.CreativeTabRegistry;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;
import com.pianistwb.merged_magic.registry.ItemRegistry;
import com.pianistwb.merged_magic.registry.LootModifierRegistry;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(MergedMagic.MODID)
public class MergedMagic {
	public static final String MODID = "merged_magic";
	public static org.slf4j.Logger LOG = LogUtils.getLogger();

	public MergedMagic(IEventBus modEventBus, ModContainer modContainer) {
		ItemRegistry.ITEMS.register(modEventBus);
		EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
		MobEffectRegistry.MOB_EFFECTS.register(modEventBus);
		CreativeTabRegistry.TABS.register(modEventBus);
		DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
		LootModifierRegistry.LOOT_MODIFIERS.register(modEventBus);
	}
}
