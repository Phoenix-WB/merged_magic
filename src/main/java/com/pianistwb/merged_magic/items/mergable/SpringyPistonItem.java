package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

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
	public final MergeStat<Integer> crutching = new MergeStat<>(30, 40, 50);
	public final MergeStat<Integer> cooldown = new MergeStat<>(300, 250, 200);

	public SpringyPistonItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.LAUNCH_VELOCITY, StatUnits.BLOCKS_PER_TICK, velocity,
				stat -> LangUtil.literal(stat));
		addMergeStat(StatNames.EFFECT_DURATION, StatUnits.SECONDS, crutching, stat -> LangUtil.literal(stat / 20));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack) && player.onGround()) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			player.addDeltaMovement(new Vec3(0, velocity.getStat(mergeLevel), 0));
			player.syncVelocity = true;

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
