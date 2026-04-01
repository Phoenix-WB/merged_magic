package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MagicUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;

public class AmethystMirrorItem extends MergableItem {
	public final MergeStat<Integer> radius = new MergeStat<>(3, 4, 7);
	public final MergeStat<Integer> getLightingDamageIncrease = new MergeStat<>(0, 1, 2);

	public AmethystMirrorItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.RADIUS, StatUnits.BLOCKS, radius, stat -> LangUtil.literal(stat));
		addMergeStat(StatNames.DAMAGE_INCREASE, StatUnits.HEARTS, getLightingDamageIncrease,
				stat -> LangUtil.literal(stat / 2F));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.CONSUME;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		if (!level.isClientSide() && livingEntity.tickCount % 10 == 0) {
			BlockPos pos = livingEntity.blockPosition();
			int lighting = level.isBrightOutside() ? level.getBrightness(LightLayer.SKY, pos)
					: level.getBrightness(LightLayer.BLOCK, pos);
			int mergeLevel = ItemUtil.getMergeLevel(stack);
			if (lighting > 8) {
				int radiusBlocks = radius.getStat(mergeLevel);
				AABB radiusBox = MagicUtil.getSquareRadius(pos, radiusBlocks);
				level.getEntities(livingEntity, radiusBox,
						entity -> entity instanceof LivingEntity living && entity.is(EntityTypeTags.BURN_IN_DAYLIGHT)
								&& MagicUtil.livingCentreInLine(livingEntity, living, ClipContext.Block.VISUAL,
										ClipContext.Fluid.ANY))
						.forEach(monster -> {
							if (livingEntity.tickCount % 80 == 0) {
								monster.hurt(level.damageSources().onFire(), getBaseLightingDamage(lighting)
										+ getLightingDamageIncrease.getStat(mergeLevel));
							}

							if (!monster.isOnFire()) {
								monster.igniteForSeconds(8.0F);
							}
						});
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 500;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	public static int getBaseLightingDamage(int lighting) {
		if (lighting < 12) {
			return 0;
		} else if (lighting < 15) {
			return 1;
		}
		return 2;
	}
}
