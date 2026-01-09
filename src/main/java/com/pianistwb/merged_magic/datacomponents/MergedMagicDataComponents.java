package com.pianistwb.merged_magic.datacomponents;

import java.util.function.Supplier;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MergedMagicDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
			.createDataComponents(Registries.DATA_COMPONENT_TYPE, MergedMagic.MODID);

	public static final Supplier<DataComponentType<MergeLevel>> BASIC_MERGE_LEVEL = DATA_COMPONENTS.registerComponentType(
			"basic", builder -> builder.persistent(MergeLevel.BASIC_CODEC).networkSynchronized(MergeLevel.BASIC_STREAM_CODEC));
}
