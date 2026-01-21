package com.pianistwb.merged_magic.items;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.util.MagicUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProspectorsPanItem extends MergableItem {
	public final MergeStat<Integer> radius = new MergeStat<>(3, 4, 5);
	public final MergeStat<Integer> cooldown = new MergeStat<>(200, 180, 160);

	public ProspectorsPanItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			int mergeLevel = stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();

			if (!level.isClientSide()) {
				BlockPos playerPos = player.blockPosition().above();

				AABB radiusBox = MagicUtil.getSquareRadius(playerPos, radius.getMergeStatForLevel(mergeLevel));

				BlockPos closestBlock = null;
				double closestBlockDist = 0D;

				for (BlockPos pos : getBlockPosIfLoaded(level, radiusBox)) {
					BlockState state = level.getBlockState(pos);

					if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) && !state.is(BlockTags.REDSTONE_ORES)
							&& (state.is(BlockTags.NEEDS_IRON_TOOL) || state.is(BlockTags.NEEDS_DIAMOND_TOOL))) {
						double otherBlockDist = playerPos.distSqr(pos);

						if (closestBlock == null) {
							closestBlock = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
							closestBlockDist = otherBlockDist;
							continue;
						}

						if (otherBlockDist < closestBlockDist) {
							closestBlock = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
							closestBlockDist = otherBlockDist;
						}
					}
				}

				if (closestBlock != null) {
					int xDist = closestBlock.getX() - playerPos.getX();
					int xSign = xDist < 0 ? -1 : 1;
					xDist = xSign * xDist;

					int yDist = closestBlock.getY() - playerPos.getY();
					int ySign = yDist < 0 ? -1 : 1;
					yDist = ySign * yDist;

					int zDist = closestBlock.getZ() - playerPos.getZ();
					int zSign = zDist < 0 ? -1 : 1;
					zDist = zSign * zDist;

					int x = playerPos.getX();
					int y = playerPos.getY();
					int z = playerPos.getZ();

					for (int i = 0; i < xDist; i++) {
						x += xSign;
						tryBreakPathTo(level, x, y, z, closestBlock);
					}

					for (int i = 0; i < zDist; i++) {
						z += zSign;
						tryBreakPathTo(level, x, y, z, closestBlock);
					}

					for (int i = 0; i < yDist; i++) {
						y += ySign;
						tryBreakPathTo(level, x, y, z, closestBlock);
					}

					Optional<Reference<Enchantment>> silkTouch = level.registryAccess()
							.lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.SILK_TOUCH);
					ItemStack pick = Items.DIAMOND_PICKAXE.getDefaultInstance();
					pick.enchant(silkTouch.get(), 1);

					LootParams.Builder lootParam = new LootParams.Builder((ServerLevel) level)
							.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(closestBlock))
							.withParameter(LootContextParams.TOOL, pick);

					List<ItemStack> loot = level.getBlockState(closestBlock).getDrops(lootParam);
					loot.forEach(item -> {
						ItemEntity droppedLoot = new ItemEntity(level, playerPos.getX(), playerPos.getY(),
								playerPos.getZ(), item);
						level.addFreshEntity(droppedLoot);
					});

					level.removeBlock(closestBlock, false);
				}
			}

			player.playSound(SoundEvents.SAND_BREAK);

			cooldowns.addCooldown(stack, cooldown.getMergeStatForLevel(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public static boolean isBlockMineable(BlockState state, ItemStack stack) {
		return !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
	}

	@SuppressWarnings("deprecation")
	public static Iterable<BlockPos> getBlockPosIfLoaded(Level level, AABB aabb) {
		int i = Mth.floor(aabb.minX);
		int j = Mth.floor(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.floor(aabb.maxY);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.floor(aabb.maxZ);
		return level.hasChunksAt(i, k, i1, j, l, j1) ? BlockPos.betweenClosed(aabb) : Collections.emptyList();
	}

	public static void tryBreakPathTo(Level level, int x, int y, int z, BlockPos pos) {
		if (x == pos.getX() && y == pos.getY() && z == pos.getZ()) {
			return;
		}

		BlockPos breakPos = new BlockPos(x, y, z);
		BlockState breakState = level.getBlockState(breakPos);

		if (breakState.is(BlockTags.MINEABLE_WITH_PICKAXE) && !breakState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
			level.destroyBlock(breakPos, true);
		}
	}
}
