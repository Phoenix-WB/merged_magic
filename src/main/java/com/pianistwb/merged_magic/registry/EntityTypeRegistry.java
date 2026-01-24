package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypeRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE,
			MergedMagic.MODID);

	public static final DeferredHolder<EntityType<?>, EntityType<TargetFrisbee>> TARGET_FRISBEE = ENTITY_TYPES.register(
			"target_frisbee",
			() -> EntityType.Builder.<TargetFrisbee>of(TargetFrisbee::new, MobCategory.MISC).sized(0.5F, 0.5F)
					.eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20)
					.build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "target_frisbee"))));
}
