package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpringyPistonItem extends MergableItem {
	public final MergeStat<Double> velocity = new MergeStat<>(0.9D, 1.0D, 1.1D);
	public final MergeStat<Integer> crutching = new MergeStat<>(40, 80, 120);
	public final MergeStat<Integer> cooldown = new MergeStat<>(150, 125, 100);

	public SpringyPistonItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack) && player.onGround()) {
			int mergeLevel = ItemUtil.getMergeStatForLevel(stack);

			player.addDeltaMovement(new Vec3(0, velocity.getStat(mergeLevel), 0));
			player.hurtMarked = true;

			player.playSound(SoundEvents.PISTON_EXTEND);

			if (!level.isClientSide()) {
				player.addEffect(new MobEffectInstance(MobEffectRegistry.CRUTCHED, crutching.getStat(mergeLevel)));
			}
			
			player.playSound(SoundEvents.PISTON_EXTEND);

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));
			
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
