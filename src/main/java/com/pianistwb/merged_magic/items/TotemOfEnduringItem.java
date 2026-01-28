package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TotemOfEnduringItem extends MergableItem implements TickInPlayerHand {
	public final MergeStat<Float> remainingHealth = new MergeStat<>(6.0F, 8.0F, 12.0F);

	public TotemOfEnduringItem(Properties properties) {
		super(properties);
	}

	@Override
	public void tickInHand(Level level, Player player, InteractionHand hand, ItemStack thisHand, ItemStack otherHand) {
		if (!level.isClientSide()) {
			int mergeLevel = ItemUtil.getMergeLevel(thisHand);
			player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20));
		}
	}
}
