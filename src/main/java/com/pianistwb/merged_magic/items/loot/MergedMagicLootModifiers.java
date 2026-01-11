package com.pianistwb.merged_magic.items.loot;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.pianistwb.merged_magic.MergedMagic;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MergedMagicLootModifiers {
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister
			.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MergedMagic.MODID);

	public static final Supplier<MapCodec<SimpleMagicAdditionLootModifier>> SIMPLE_MAGIC_ADDITION_LOOT_MODIFIER = LOOT_MODIFIERS
			.register("simple_magic_addition_loot_modifier", () -> SimpleMagicAdditionLootModifier.CODEC);
}
