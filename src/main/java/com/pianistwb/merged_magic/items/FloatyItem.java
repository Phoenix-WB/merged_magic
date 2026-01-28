package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.util.ComponentUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FloatyItem extends MergableItem implements TickInPlayerHand {
	public final MergeStat<Double> acceleration = new MergeStat<>(0.05D, 0.07D, 0.12D);

	public FloatyItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.ACCELERATION, StatUnits.BLOCKS_PER_TICK, acceleration,
				stat -> ComponentUtil.literal(stat));
	}

	@Override
	public void tickInHand(Level level, Player player, InteractionHand hand, ItemStack thisHand, ItemStack otherHand) {
		if (player.isInWater() && !level.getBlockState(player.blockPosition().above()).isAir()) {
			int mergeLevel = ItemUtil.getMergeLevel(thisHand);

			Vec3 mov = player.getDeltaMovement();
			player.setDeltaMovement(
					new Vec3(mov.x(), Math.max(mov.y(), 0D) + acceleration.getStat(mergeLevel), mov.z()));
		}
	}
}
