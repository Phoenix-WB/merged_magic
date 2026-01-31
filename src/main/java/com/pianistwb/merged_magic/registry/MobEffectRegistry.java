package com.pianistwb.merged_magic.registry;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.effect.CocoaBuffMobEffect;
import com.pianistwb.merged_magic.effect.CreeperDisguisedMobEffect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

	public static final DeferredHolder<MobEffect, MobEffect> TRAPJAWED = MOB_EFFECTS.register("trapjawed",
			() -> new MobEffect(MobEffectCategory.HARMFUL, 0x24c73c)
					.addAttributeModifier(Attributes.MOVEMENT_SPEED,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "effect.trapjawed_slow"), -1.0F,
							AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
					.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "effect.trapjawed_resist"), 1.0D,
							AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
					.addAttributeModifier(Attributes.JUMP_STRENGTH,
							Identifier.fromNamespaceAndPath(MergedMagic.MODID, "effect.trapjawed_held"), -1.0F,
							AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	public static final DeferredHolder<MobEffect, MobEffect> ENDURING = MOB_EFFECTS.register("enduring",
			() -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xd9d9d9));

	public static final DeferredHolder<MobEffect, CreeperDisguisedMobEffect> CREEPER_DISGUISED = MOB_EFFECTS
			.register("creeper_disguised", () -> new CreeperDisguisedMobEffect(MobEffectCategory.BENEFICIAL, 0x10b51e));
}
