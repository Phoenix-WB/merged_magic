package com.pianistwb.merged_magic.items;

import org.jspecify.annotations.Nullable;

import com.pianistwb.merged_magic.util.ComponentUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.phys.Vec3;

public class BroomItem extends MergableItem {
	public final MergeStat<Double> initial_velocity = new MergeStat<>(0.6D, 0.9D, 1.1D);
	public final MergeStat<Integer> burnTime = new MergeStat<>(300, 600, 1200);
	public final MergeStat<Integer> cooldown = new MergeStat<>(90, 70, 50);

	public BroomItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.INITIAL_VELOCITY, StatUnits.BLOCKS_PER_TICK, initial_velocity,
				stat -> ComponentUtil.literal(stat));
		addMergeStat(StatNames.BURN_TIME, StatUnits.SECONDS, burnTime, stat -> ComponentUtil.literal(stat / 20F));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> ComponentUtil.literal(stat / 20F));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
			InteractionHand usedHand) {
		ItemCooldowns cooldowns = player.getCooldowns();
		if (!cooldowns.isOnCooldown(stack) && !player.canCollideWith(interactionTarget)) {
			int mergeLevel = ItemUtil.getMergeStatForLevel(stack);

			Vec3 viewVector = player.getViewVector(1.0F);
			double d0 = Math.sqrt(viewVector.x() * viewVector.x() + viewVector.z() * viewVector.z());

			Vec3 newMotion;
			if (d0 < 1.0E-5F) {
				newMotion = Vec3.ZERO;
			} else {
				double velo = initial_velocity.getStat(mergeLevel);
				newMotion = new Vec3(velo * viewVector.x() / d0, 0.3, velo * viewVector.z() / d0);
			}

			interactionTarget.addDeltaMovement(newMotion);

			player.playSound(SoundEvents.BRUSH_GENERIC, 1.0F, 0.65F);

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
		int mergeLevel = ItemUtil.getMergeStatForLevel(itemStack);
		return burnTime.getStat(mergeLevel);
	}
}
