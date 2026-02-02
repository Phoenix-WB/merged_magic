package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.items.TickInPlayerHand;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TotemOfEnduringItem extends MergableItem implements TickInPlayerHand {
	public final MergeStat<Float> remainingHealth = new MergeStat<>(8.0F, 10.0F, 12.0F);
	public final MergeStat<Integer> resistAmplifier = new MergeStat<>(0, 0, 1);
	public final MergeStat<Integer> amplifier = new MergeStat<>(0, 1, 2);
	public final MergeStat<Double> initialVelocity = new MergeStat<>(1.0D, 1.08D, 1.12D);

	public TotemOfEnduringItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.REQUIRED_HEALTH, StatUnits.HEARTS, remainingHealth,
				stat -> LangUtil.literal(stat / 2F));
		addMergeStat(StatNames.AMPLIFIER, Component.empty(), resistAmplifier, stat -> LangUtil.literal(stat + 1));
		addMergeStat(StatNames.LAUNCH_VELOCITY, StatUnits.BLOCKS_PER_TICK, initialVelocity,
				stat -> LangUtil.literal(stat));
	}

	@Override
	public void tickInHand(Level level, Player player, InteractionHand hand, ItemStack thisHand, ItemStack otherHand) {
		if (!level.isClientSide()) {
			int mergeLevel = ItemUtil.getMergeLevel(thisHand);

			if (player.getHealth() <= remainingHealth.getStat(mergeLevel)) {
				player.addEffect(new MobEffectInstance(MobEffectRegistry.ENDURING, 10, amplifier.getStat(mergeLevel),
						false, false));
				player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 10, resistAmplifier.getStat(mergeLevel)));
			}
		}
	}
}
