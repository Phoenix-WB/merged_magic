package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class SwordOfEdenItem extends MergableItem {
	public final MergeStat<Integer> effectDuration = new MergeStat<Integer>(60, 120, 200);
	public final MergeStat<Integer> amplifier = new MergeStat<Integer>(0, 0, 1);

	public SwordOfEdenItem(Properties properties) {
		super(ItemUtil.sword(properties, ToolMaterial.WOOD, 3.0F, -2.4F));
		addMergeStat(StatNames.EFFECT_DURATION, StatUnits.SECONDS, effectDuration, stat -> LangUtil.literal(stat / 20));
		addMergeStat(StatNames.AMPLIFIER, Component.empty(), amplifier, stat -> LangUtil.literal(stat + 1));
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hurtEnemy(stack, target, attacker);
		if (!target.level().isClientSide()) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);
			target.addEffect(new MobEffectInstance(MobEffects.POISON, effectDuration.getStat(mergeLevel),
					amplifier.getStat(mergeLevel)));
		}
	}
}
