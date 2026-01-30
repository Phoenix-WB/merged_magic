package com.pianistwb.merged_magic.items;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.pianistwb.merged_magic.datacomponents.IntegerData;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.util.ComponentUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class CursedPaintingItem extends MergableItem {
	private static final MutableComponent CAUTION = Component
			.translatable("tooltip.merged_magic.cursed_painting.caution").withStyle(ChatFormatting.DARK_RED);

	public final MergeStat<Integer> repair = new MergeStat<>(6, 3, 2);

	public CursedPaintingItem(Properties properties) {
		super(ItemUtil.brokenItem(properties.component(DataComponentRegistry.MERGE_STATE, IntegerData.DEFAULT)
				.component(DataComponentRegistry.MERGE_INDEX, IntegerData.DEFAULT)
				.component(DataComponentRegistry.STORED_ENTITIES, CustomData.EMPTY), 18));
		addMergeStat(StatNames.PROJECTILES, Component.empty(), repair, stat -> ComponentUtil.literal(18 / stat));
	}

	@Override
	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		// Divide damage on merge to fix this issue and keep stored projectiles
		stackedOn.set(DataComponentRegistry.STORED_ENTITIES, CustomData.EMPTY);
		return stackedOn;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
		tooltipAdder.accept(CAUTION);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			player.startUsingItem(hand);

			return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		int mergeLevel = ItemUtil.getMergeLevel(stack);
		int mergeState = ItemUtil.getMergeState(stack);

		if (mergeState == 0) {
			AABB box = livingEntity.getHitbox().inflate(1.0D);

			List<Projectile> projectiles = level.getEntitiesOfClass(Projectile.class, box,
					entity -> !isExcludedProjectiles(entity));

			for (Projectile proj : projectiles) {
				int itemHealth = ItemUtil.getItemHealth(stack);

				if (itemHealth <= stack.getMaxDamage()) {
					int rep = repair.getStat(mergeLevel);
					int index = (itemHealth + rep) / rep;

					ItemUtil.saveEntityToStack(proj, stack, "proj" + (itemHealth + rep) / rep, "LeftOwner");
					ItemUtil.setMergeIndex(stack, index);
					ItemUtil.healStack(stack, rep);
				} else {
					break;
				}

				if (ItemUtil.isItemFullhealth(stack)) {
					break;
				}
			}
		} else if (mergeState == 1) {
			if (remainingUseDuration != 72000 && remainingUseDuration % 25 == 0) {
				int mergeIndex = ItemUtil.getMergeIndex(stack);

				Optional<Entity> entity = ItemUtil.loadEntityFromStack(level, stack, "proj" + mergeIndex, true);

				if (entity.isPresent() && entity.get() instanceof Projectile projectile) {
					level.addFreshEntity(projectile);
					projectile.setOwner(livingEntity);
					projectile.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F,
							(float) projectile.getDeltaMovement().length(), 1.3F);
				}

				ItemUtil.setMergeIndex(stack, Math.max(mergeIndex - 1, 0));
				ItemUtil.damageStack(stack, repair.getStat(mergeLevel));
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		int mergeState = ItemUtil.getMergeState(stack);

		if (mergeState == 0) {
			if (ItemUtil.isItemHealthy(stack)) {
				ItemUtil.setMergeState(stack, 1);
			}
		} else if (mergeState == 1) {
			if (!ItemUtil.isItemHealthy(stack)) {
				ItemUtil.setMergeState(stack, 0);

				if (livingEntity instanceof Player player) {
					player.getCooldowns().addCooldown(stack, 600);
				}
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	public static boolean isExcludedProjectiles(Entity entity) {
		EntityType<?> type = entity.getType();
		return type == EntityType.FISHING_BOBBER || type == EntityType.TRIDENT;
	}

	@Override
	public boolean isDurable() {
		return true;
	}
}
