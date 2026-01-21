package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

public class JungleVineItem extends MergableItem implements TicksInPlayerHand {
	public final MergeStat<Integer> regrowth = new MergeStat<>(1, 2, 4);

	public JungleVineItem(Properties properties) {
		super(ItemUtil.durableItem(properties, 32));
	}
	
	@Override
	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		stackedOn.setDamageValue(0);
		return stackedOn;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity,
			Consumer<Item> onBroken) {
		return stack.get(DataComponents.DAMAGE).intValue() == 1 ? 0 : amount;
	}

	@Override
	public void tickInHand(Level level, Player player, ItemStack stack) {
		if (!level.isClientSide() && ItemUtil.isItemHealthy(stack)) {
			BlockPos pos = player.blockPosition();
			BlockPos posAbove = pos.above();

			if (level.getBlockState(pos).is(Blocks.AIR)) {
				if (placeVines(level, pos)) {
					ItemUtil.damageStack(stack, 1);
				}

			}

			if (level.getBlockState(posAbove).is(Blocks.AIR)) {
				if (placeVines(level, posAbove)) {
					ItemUtil.damageStack(stack, 1);
				}
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
		if (entity.tickCount % 60 == 0) {
			int mergeLevel = ItemUtil.getMergeStatForLevel(stack);
			ItemUtil.healStack(stack, regrowth.getStat(mergeLevel));
		}
	}
	
	@Override
	public boolean isDurable() {
		return true;
	}

	public static boolean placeVines(Level level, BlockPos pos) {
		BlockState vine = Blocks.VINE.defaultBlockState();
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() == Direction.Axis.Y
					|| !VineBlock.isAcceptableNeighbour(level, pos.relative(dir), dir.getOpposite())) {
				continue;
			}

			vine = vine.setValue(VineBlock.getPropertyForFace(dir), true);
		}

		if (anyTrue(vine)) {
			level.setBlockAndUpdate(pos, vine);
			return true;
		}

		return false;
	}

	private static boolean anyTrue(BlockState state) {
		return state.getValue(VineBlock.NORTH) || state.getValue(VineBlock.EAST) || state.getValue(VineBlock.SOUTH)
				|| state.getValue(VineBlock.WEST);
	}
}
