package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

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
import net.minecraft.world.phys.Vec3;

public class DolphinsBreathItem extends MergableItem {
	public final MergeStat<Integer> waterBreathDuration = new MergeStat<>(300, 600, 900);
	public final MergeStat<Integer> cooldown = new MergeStat<>(600, 900, 1200);

	public DolphinsBreathItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.EFFECT_DURATION, StatUnits.SECONDS, waterBreathDuration,
				stat -> LangUtil.literal(stat / 20));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack) && player.isInWater()
				&& level.getBlockState(twoAbove(player.blockPosition())).isAir()) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			player.addDeltaMovement(new Vec3(0, 1, 0));
			player.hurtMarked = true;

			if (!level.isClientSide()) {
				player.addEffect(
						new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathDuration.getStat(mergeLevel)));
			}

			player.playSound(SoundEvents.DOLPHIN_JUMP);
			player.playSound(SoundEvents.DOLPHIN_AMBIENT);

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public static BlockPos twoAbove(BlockPos position) {
		return new BlockPos(position.getX(), position.getY() + 2, position.getZ());
	}
}
