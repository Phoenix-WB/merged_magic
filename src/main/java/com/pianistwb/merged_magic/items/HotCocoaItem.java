package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ComponentUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

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
	public final MergeStat<Integer> buffDuration = new MergeStat<>(200, 240, 300);
	public final MergeStat<Integer> damage = new MergeStat<>(15, 5, 3);

	public static final int HOT_COCOA_DURABILITY = 15;

	public HotCocoaItem(Properties properties) {
		super(ItemUtil.brokenItem(properties.component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK),
				HOT_COCOA_DURABILITY));
		addMergeStat(StatNames.EFFECT_DURATION, StatUnits.SECONDS, buffDuration,
				stat -> ComponentUtil.literal(stat / 20F));
		addMergeStat(StatNames.USE_DAMAGE, StatUnits.DURABILITY, damage, stat -> ComponentUtil.literal(stat));
	}

	@Override
	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		if (ItemUtil.isItemHealthy(stackedOn) || ItemUtil.isItemHealthy(carried)) {
			ItemUtil.healStack(stackedOn, HOT_COCOA_DURABILITY);
		}

		return stackedOn;
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
		int mergeLevel = ItemUtil.getMergeLevel(stack);

		if (!level.isClientSide()) {
			livingEntity
					.addEffect(new MobEffectInstance(MobEffectRegistry.COCOA_BUFF, buffDuration.getStat(mergeLevel)));
		}

		ItemUtil.damageStack(stack, damage.getStat(mergeLevel));

		return stack;
	}

	@Override
	public boolean isDurable() {
		return true;
	}
}
