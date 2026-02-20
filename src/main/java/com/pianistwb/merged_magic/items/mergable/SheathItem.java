package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
		if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand)) && swordInMainhand(player)) {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		if (!swordInMainhand(livingEntity)) {
			livingEntity.releaseUsingItem();
		}

		if (!level.isClientSide()) {
			livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.SHEATHING, 5, 0, false, false));
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (swordInMainhand(livingEntity)) {
			livingEntity.playSound(SoundEvents.ANVIL_PLACE);
			
			if (!livingEntity.level().isClientSide()) {
				int mergeLevel = ItemUtil.getMergeLevel(stack);
				livingEntity.addEffect(
						new MobEffectInstance(MobEffectRegistry.STRIKING, 70, amplifier.getStat(mergeLevel)));
			}
		}

		return stack;
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (entity instanceof Player player) {
			player.getCooldowns().addCooldown(stack, 60);
		}

		return false;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		int mergeLevel = ItemUtil.getMergeLevel(stack);
		return duration.getStat(mergeLevel);
	}

	private static boolean swordInMainhand(LivingEntity living) {
		return living.getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SWORDS);
	}
}
