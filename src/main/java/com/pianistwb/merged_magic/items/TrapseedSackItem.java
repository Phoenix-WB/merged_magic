package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.entity.monster.Trapjaw;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;
import com.pianistwb.merged_magic.util.ComponentUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MagicUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;
import com.pianistwb.merged_magic.util.StatValues;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrapseedSackItem extends MergableItem {
	public final MergeStat<Integer> summonCount = new MergeStat<>(1, 3, 5);
	public final MergeStat<Double> radius = new MergeStat<>(1.5D, 2.5D, 3.5D);
	public final MergeStat<Integer> spawningRotations = new MergeStat<>(360, 120, 72);
	public final MergeStat<Integer> cooldown = new MergeStat<>(1200, 1000, 800);

	public TrapseedSackItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.SUMMON_COUNT, Component.empty(), summonCount, stat -> ComponentUtil.literal(stat));
		addMergeStat(StatNames.RADIUS, StatUnits.BLOCKS, radius, stat -> ComponentUtil.literal(stat));
		addMergeStat(StatNames.SPAWN_PLACEMENT, Component.empty(), spawningRotations,
				stat -> getShapeForAngle(stat));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> ComponentUtil.literal(stat / 20F));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			if (!level.isClientSide()) {
				Vec3 spawnRelative = MagicUtil.getRelativeToEntityView(player, radius.getStat(mergeLevel), 0.0D);
				BlockPos playerPos = player.blockPosition();

				int rotation = spawningRotations.getStat(mergeLevel);
				for (int r = 0; r < 360; r += rotation) {
					Vec3 rotatedSpawn = MagicUtil.rotateVector(spawnRelative, Mth.DEG_TO_RAD * r);

					BlockPos spawnPos = null;
					int x = playerPos.getX() + (int) Math.round(rotatedSpawn.x());
					int y = playerPos.getY() - 2;
					int z = playerPos.getZ() + (int) Math.round(rotatedSpawn.z());

					for (int i = 0; i < 5; i++) {
						spawnPos = new BlockPos(x, y + i, z);

						if (level.getBlockState(spawnPos).isAir()) {
							break;
						} else {
							spawnPos = null;
						}
					}

					if (spawnPos != null) {
						Trapjaw trapjaw = EntityTypeRegistry.TRAPJAW.get().spawn((ServerLevel) level, spawnPos,
								EntitySpawnReason.MOB_SUMMONED);
						trapjaw.setOwner(player);
						trapjaw.despawn = true;
						trapjaw.despawnTicks = 600;
					}
				}
			}

			player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
			
			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public static MutableComponent getShapeForAngle(int angle) {
		switch (angle) {
		case 360:
			return StatValues.IN_FRONT;
		case 120:
			return StatValues.TRIANGLE;
		case 72:
			return StatValues.PENTAGON;
		}
		return Component.empty();
	}
}
