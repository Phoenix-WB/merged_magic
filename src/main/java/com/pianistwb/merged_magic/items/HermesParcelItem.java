package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import com.pianistwb.merged_magic.loot.LootTables;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class HermesParcelItem extends Item {
	private static final MutableComponent DELIVER = Component
			.translatable("tooltip.merged_magic.hermes_parcel.deliver");

	public HermesParcelItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(DELIVER);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
			InteractionHand usedHand) {
		if (interactionTarget.getType().equals(EntityTypes.VILLAGER)) {
			Level level = player.level();

			if (!level.isClientSide()) {
				interactionTarget.dropFromGiftLootTable((ServerLevel) level, LootTables.HERMES_SHOES_GIFT, (serverLevel,
						thrownStack) -> BehaviorUtils.throwItem(interactionTarget, thrownStack, player.position()));

			}
			interactionTarget.playSound(SoundEvents.VILLAGER_YES);

			stack.shrink(1);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
