package com.pianistwb.merged_magic.registry;

import java.util.function.Supplier;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.datacomponents.IntegerData;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
			.createDataComponents(Registries.DATA_COMPONENT_TYPE, MergedMagic.MODID);

	public static final Supplier<DataComponentType<MergeLevel>> MERGE_LEVEL = DATA_COMPONENTS.registerComponentType(
			"merge_level",
			builder -> builder.persistent(MergeLevel.CODEC).networkSynchronized(MergeLevel.STREAM_CODEC));

	public static final Supplier<DataComponentType<IntegerData>> MERGE_STATE = DATA_COMPONENTS.registerComponentType(
			"merge_state",
			builder -> builder.persistent(IntegerData.CODEC).networkSynchronized(IntegerData.STREAM_CODEC));

	public static final Supplier<DataComponentType<IntegerData>> MERGE_INDEX = DATA_COMPONENTS.registerComponentType(
			"merge_index",
			builder -> builder.persistent(IntegerData.CODEC).networkSynchronized(IntegerData.STREAM_CODEC));

	@SuppressWarnings("deprecation")
	public static final Supplier<DataComponentType<CustomData>> STORED_ENTITIES = DATA_COMPONENTS.registerComponentType(
			"stored_entities",
			builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC));
}
