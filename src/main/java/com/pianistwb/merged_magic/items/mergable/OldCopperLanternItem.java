package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.entity.projectile.CopperFlame;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class OldCopperLanternItem extends MergableItem {
	MergeStat<Integer> duration = new MergeStat<>(200, 400, 600);
	MergeStat<Integer> cooldown = new MergeStat<>(200, 140, 80);

	public OldCopperLanternItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.DURATION, StatUnits.SECONDS, duration, stat -> LangUtil.literal(stat / 20));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			player.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		int mergeLevel = ItemUtil.getMergeLevel(stack);

		CopperFlame flame = new CopperFlame(level, livingEntity, duration.getStat(mergeLevel));
		flame.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, 0.6F, 2.1F);
		level.addFreshEntity(flame);

		livingEntity.playSound(SoundEvents.FIRECHARGE_USE);

		if (livingEntity instanceof Player player) {
			player.getCooldowns().addCooldown(stack, cooldown.getStat(mergeLevel));
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 20;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}
}
