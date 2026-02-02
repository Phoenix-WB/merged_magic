package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.CopperFlameSpire;
import com.pianistwb.merged_magic.entity.monster.Trapjaw;
import com.pianistwb.merged_magic.entity.projectile.CopperFlame;
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
					.clientTrackingRange(4).updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "target_frisbee"))));

	public static final DeferredHolder<EntityType<?>, EntityType<Trapjaw>> TRAPJAW = ENTITY_TYPES.register("trapjaw",
			() -> EntityType.Builder.<Trapjaw>of(Trapjaw::new, MobCategory.MONSTER).sized(0.25F, 0.8F).eyeHeight(1.0F)
					.clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "trapjaw"))));

	public static final DeferredHolder<EntityType<?>, EntityType<CopperFlame>> COPPER_FLAME = ENTITY_TYPES.register(
			"copper_flame",
			() -> EntityType.Builder.<CopperFlame>of(CopperFlame::new, MobCategory.MISC).sized(0.5F, 0.5F)
					.clientTrackingRange(4).updateInterval(20).build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "copper_flame"))));

	public static final DeferredHolder<EntityType<?>, EntityType<CopperFlameSpire>> COPPER_FLAME_SPIRE = ENTITY_TYPES
			.register("copper_flame_spire",
					() -> EntityType.Builder.<CopperFlameSpire>of(CopperFlameSpire::new, MobCategory.MISC)
							.sized(1.0F, 3.0F).clientTrackingRange(6).build(ResourceKey.create(Registries.ENTITY_TYPE,
									Identifier.fromNamespaceAndPath(MergedMagic.MODID, "copper_flame_spire"))));
}
