package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.effect.CocoaBuffMobEffect;

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

	public static final DeferredHolder<MobEffect, CocoaBuffMobEffect> COCOA_BUFF = MOB_EFFECTS.register("cocoa_buff",
			() -> new CocoaBuffMobEffect(MobEffectCategory.BENEFICIAL, 0x422619));

	public static final DeferredHolder<MobEffect, MobEffect> TARGETED = MOB_EFFECTS.register("targeted",
			() -> new MobEffect(MobEffectCategory.HARMFUL, 0xffe7e6));
}
