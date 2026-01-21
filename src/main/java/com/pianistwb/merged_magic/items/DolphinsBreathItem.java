package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class DolphinsBreathItem extends MergableItem {
	public final MergeStat<Integer> waterBreathDuration = new MergeStat<>(300, 600, 900);
	
	public DolphinsBreathItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack) && player.isInWater()
				&& level.getBlockState(twoAbove(player.blockPosition())).is(Blocks.AIR)) {
			int mergeLevel = stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();

			player.addDeltaMovement(new Vec3(0, 1, 0));
			player.hurtMarked = true;

			int duration = waterBreathDuration.getMergeStatForLevel(mergeLevel);
			
			if(!level.isClientSide()) {
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration));
			}
			
			player.playSound(SoundEvents.DOLPHIN_JUMP);
			player.playSound(SoundEvents.DOLPHIN_AMBIENT);
			
			cooldowns.addCooldown(stack, duration + 300);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public static BlockPos twoAbove(BlockPos position) {
		return new BlockPos(position.getX(), position.getY() + 2, position.getZ());
	}
}
