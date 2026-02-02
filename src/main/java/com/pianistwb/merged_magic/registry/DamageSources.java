package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class DamageSources {
	public static final ResourceKey<DamageType> TRAPJAWED = ResourceKey.create(Registries.DAMAGE_TYPE,
			Identifier.fromNamespaceAndPath(MergedMagic.MODID, "trapjawed"));

	public static DamageSource trapJaw(Entity entity) {
		return new DamageSource(entity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(TRAPJAWED), entity);
	}
}
