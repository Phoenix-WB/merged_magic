package com.pianistwb.merged_magic.registry;

import java.util.function.Supplier;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
			.createDataComponents(Registries.DATA_COMPONENT_TYPE, MergedMagic.MODID);

	public static final Supplier<DataComponentType<MergeLevel>> MERGE_LEVEL = DATA_COMPONENTS.registerComponentType(
			"merge_level", builder -> builder.persistent(MergeLevel.CODEC).networkSynchronized(MergeLevel.STREAM_CODEC));
}
