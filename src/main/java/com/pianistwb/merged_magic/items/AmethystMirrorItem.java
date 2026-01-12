package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.util.EntityUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class AmethystMirrorItem extends MergableItem {
	private final MergeStat<Integer> radiusChange = new MergeStat<>(1, 2, 5);
	private final MergeStat<Integer> getLightingDamageIncrease = new MergeStat<>(0, 1, 2);
	private final MergeStat<Integer> cooldown = new MergeStat<>(300, 240, 180);

	public AmethystMirrorItem(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			BlockPos pos = player.blockPosition();
			int lighting = level.getLightEngine().getRawBrightness(pos, 0);
			int mergeLevel = stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();

			if (!level.isClientSide()) {
				if (lighting > 8) {
					int radiusChangeBlocks = radiusChange.getMergeStatForLevel(mergeLevel);
					AABB radius = new AABB(pos.getX() - 5 - radiusChangeBlocks, pos.getY() - 5 - radiusChangeBlocks,
							pos.getZ() - 5 - radiusChangeBlocks, pos.getX() + 6 + radiusChangeBlocks,
							pos.getY() + 6 + radiusChangeBlocks, pos.getZ() + 6 + radiusChangeBlocks);
					level.getEntities(player, radius,
							entity -> entity instanceof LivingEntity living
									&& entity.getType().is(EntityTypeTags.BURN_IN_DAYLIGHT)
									&& EntityUtil.livingCentreInLine(player, living, ClipContext.Block.VISUAL,
											ClipContext.Fluid.ANY))
							.forEach(monster -> {
								ProjectileUtil.getHitResultOnMoveVector(player, null);

								monster.hurt(level.damageSources().onFire(), getBaseLightingDamage(lighting)
										+ getLightingDamageIncrease.getMergeStatForLevel(mergeLevel));
								monster.igniteForSeconds(8.0F);
							});
				}
			}

			player.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE);

			cooldowns.addCooldown(stack, cooldown.getMergeStatForLevel(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public static int getBaseLightingDamage(int lighting) {
		if (lighting < 12) {
			return 1;
		} else if (lighting < 15) {
			return 2;
		}
		return 5;
	}
}
