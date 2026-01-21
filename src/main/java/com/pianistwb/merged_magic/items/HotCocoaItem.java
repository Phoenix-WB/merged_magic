package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

public class HotCocoaItem extends MergableItem {
	public final MergeStat<Integer> buffDuration = new MergeStat<>(200, 600, 1200);

	public HotCocoaItem(Properties properties) {
		super(ItemUtil.brokenItem(properties.component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK), 1));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (ItemUtil.isItemHealthy(stack)) {
			Consumable consumable = stack.get(DataComponents.CONSUMABLE);
			return consumable.startConsuming(player, stack, hand);
		}

		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		if (!level.isClientSide()) {
			int mergeLevel = ItemUtil.getMergeStatForLevel(stack);

			livingEntity.addEffect(
					new MobEffectInstance(MobEffectRegistry.COCOA_BUFF, buffDuration.getStat(mergeLevel)));
		}

		ItemUtil.damageStack(stack, 1);

		return stack;
	}
	
	@Override
	public boolean isDurable() {
		return true;
	}
}
