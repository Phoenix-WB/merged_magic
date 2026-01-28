package com.pianistwb.merged_magic.loot;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTables {
	public static final ResourceKey<LootTable> HERMES_SHOES_GIFT = register("gameplay/hermes_shoes_gift");

	private static ResourceKey<LootTable> register(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MergedMagic.MODID, name));
	}
}
