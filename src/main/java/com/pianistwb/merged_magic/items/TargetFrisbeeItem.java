package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class TargetFrisbeeItem extends MergableItem {
	public final MergeStat<Float> scaling = new MergeStat<>(1.25F, 1.75F, 2.5F);
	public final MergeStat<Integer> duration = new MergeStat<>(200, 300, 400);
	public final MergeStat<Integer> cooldown = new MergeStat<>(200, 160, 140);

	public TargetFrisbeeItem(Properties properties) {
		super(properties);
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
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
		if (livingEntity instanceof Player player) {
			int mergeLevel = ItemUtil.getMergeStatForLevel(stack);
			int usedTime = this.getUseDuration(stack, livingEntity) - timeLeft;
			float velo = BowItem.getPowerForTime(usedTime) * scaling.getStat(mergeLevel);
			
			if(velo < 0.1) {
				return false;
			}

			TargetFrisbee frisbee = new TargetFrisbee(level, livingEntity, duration.getStat(mergeLevel));
			frisbee.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velo, 0.8F);
			level.addFreshEntity(frisbee);
			
			player.playSound(SoundEvents.ARROW_SHOOT);
			
			player.getCooldowns().addCooldown(stack, cooldown.getStat(mergeLevel));
		}

		return false;
	}
	
	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}
	
    @Override
    public int getUseDuration(ItemStack p_40680_, LivingEntity p_345962_) {
        return 72000;
    }
}
