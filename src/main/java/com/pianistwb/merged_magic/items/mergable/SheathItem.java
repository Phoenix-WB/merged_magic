package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class SheathItem extends MergableItem {
	public final MergeStat<Integer> duration = new MergeStat<>(80, 60, 45);
	public final MergeStat<Integer> amplifier = new MergeStat<>(0, 0, 1);

	public SheathItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.HELD_DURATION, StatUnits.SECONDS, duration, stat -> LangUtil.literal(stat / 20F));
		addMergeStat(StatNames.DAMAGE_INCREASE, Component.empty(), amplifier,
				stat -> LangUtil.withMultiply(LangUtil.literal(1 + 1.0 * (1 + stat))));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND).has(DataComponents.WEAPON)) {
			livingEntity.playSound(SoundEvents.ANVIL_PLACE);
			if (!livingEntity.level().isClientSide()) {
				int mergeLevel = ItemUtil.getMergeLevel(stack);
				livingEntity.addEffect(
						new MobEffectInstance(MobEffectRegistry.STRIKING, 70, amplifier.getStat(mergeLevel)));
			}
		}

		if (livingEntity instanceof Player player) {
			player.getCooldowns().addCooldown(stack, 60);
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		int mergeLevel = ItemUtil.getMergeLevel(stack);
		return duration.getStat(mergeLevel);
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}
}
