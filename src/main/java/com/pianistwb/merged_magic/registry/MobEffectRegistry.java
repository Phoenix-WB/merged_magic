package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MobEffectRegistry {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,
			MergedMagic.MODID);

	public static final DeferredHolder<MobEffect, MobEffect> CRUTCHED = MOB_EFFECTS.register("crutched",
			() -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x5ff587));
}
